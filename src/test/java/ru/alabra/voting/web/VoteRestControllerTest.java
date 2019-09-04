package ru.alabra.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
import ru.alabra.voting.repository.VoteRepository;
import ru.alabra.voting.web.json.JsonUtil;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alabra.voting.TestData.*;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 28.08.2019
 */
@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
//@WebAppConfiguration
//@ExtendWith(SpringExtension.class)
@Transactional
class VoteRestControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private VoteRepository repository;

    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static final CharacterEncodingFilter CHARACTER_ENCODING_FILTER = new CharacterEncodingFilter();

    static {
        CHARACTER_ENCODING_FILTER.setEncoding("UTF-8");
        CHARACTER_ENCODING_FILTER.setForceEncoding(true);
    }

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(CHARACTER_ENCODING_FILTER)
//                .apply(springSecurity())
                .build();
    }

//    @Test
//    void vote() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + M1_ID))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }

    @Test
    void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .param("date", VOTE1.getDate().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(VOTE1, VOTE2, VOTE3, VOTE4))));
    }

    @Test
    void findByDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/by-date")
                .param("date", VOTE1.getDate().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(VOTE1, VOTE2, VOTE3))));
    }

    @Test
    void findByPeriod() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/by-period")
                .param("startDate", VOTE1.getDate().toString())
                .param("endDate", VOTE4.getDate().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(VOTE1, VOTE2, VOTE3, VOTE4))));
    }


    @Test
    void vote() throws Exception {
        LocalDate today = LocalDate.now();
        Vote created = new Vote(null, today, M1, USER);

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
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
        Vote created = new Vote(null, today, M1, USER);

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(created.getMenu().getId())));
        MvcResult result = action.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Vote returned = jsonUtil.readValue(contentAsString, Vote.class);
        created.setId(returned.getId());

        assertMatch(repository.findAll(), VOTE1, VOTE2, VOTE3, VOTE4, created);

        Vote changed = new Vote(null, today, M2, USER);

        action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(changed.getMenu().getId())));
        result = action.andReturn();
        contentAsString = result.getResponse().getContentAsString();

        returned = jsonUtil.readValue(contentAsString, Vote.class);
        changed.setId(returned.getId());

        assertMatch(repository.findAll(), VOTE1, VOTE2, VOTE3, VOTE4, changed);
    }

    @Test
    void changeVoteExpired() {

    }
}