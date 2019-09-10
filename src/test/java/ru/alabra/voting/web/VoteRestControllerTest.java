package ru.alabra.voting.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.alabra.voting.model.Vote;
import ru.alabra.voting.repository.CrudVoteRepository;
import ru.alabra.voting.util.ConfigUtil;
import ru.alabra.voting.web.json.JsonUtil;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alabra.voting.TestData.*;
import static ru.alabra.voting.TestUtil.userHttpBasic;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 28.08.2019
 */
class VoteRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private CrudVoteRepository repository;

    @Autowired
    private ConfigUtil configUtil;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .param("date", VOTE1.getDate().toString())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(VOTE1, VOTE2, VOTE3, VOTE4))));
    }

    @Test
    void findByDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/by-date")
                .with(userHttpBasic(USER))
                .param("date", VOTE1.getDate().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(VOTE1, VOTE2, VOTE3))));
    }

    @Test
    void findByPeriod() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/by-period")
                .with(userHttpBasic(USER))
                .param("startDate", VOTE1.getDate().toString())
                .param("endDate", VOTE4.getDate().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(VOTE1, VOTE2, VOTE3, VOTE4))));
    }


    @Test
    void vote() throws Exception {

        LocalDate today = LocalDate.now();
        configUtil.set_END_VOTING_TIME(today.atTime(LocalTime.MAX).toLocalTime());
        Vote created = new Vote(null, today, M1, USER);

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(created.getMenu().getId())));
        MvcResult result = action.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Vote returned = jsonUtil.readValue(contentAsString, Vote.class);
        created.setId(returned.getId());

        assertMatch(repository.findAll(), VOTE1, VOTE2, VOTE3, VOTE4, created);
    }

    @Test
    void changeVote() throws Exception {
        LocalDate today = LocalDate.now();
        configUtil.set_END_VOTING_TIME(today.atTime(LocalTime.MAX).toLocalTime());
        Vote created = new Vote(null, today, M3, USER);

        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(created.getMenu().getId())));


        Vote changed = new Vote(null, today, M2, USER);

        ResultActions actionReVote = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(changed.getMenu().getId())));
        MvcResult resultReVote = actionReVote.andReturn();
        String contentAsString = resultReVote.getResponse().getContentAsString();

        Vote returned = jsonUtil.readValue(contentAsString, Vote.class);
        changed.setId(returned.getId());

        assertMatch(repository.findAll(), VOTE1, VOTE2, VOTE3, VOTE4, changed);
    }

    @Test
    void voteExpired() throws Exception {
        LocalDate today = LocalDate.now();
        configUtil.set_END_VOTING_TIME(today.atStartOfDay().toLocalTime());
        Vote created = new Vote(null, today, M1, USER);

        mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(USER))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(created.getMenu().getId())))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value("VOTE_REPEAT_ERROR"));
    }
}