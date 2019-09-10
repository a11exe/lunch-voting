package ru.alabra.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.alabra.voting.model.Restaurant;
import ru.alabra.voting.repository.CrudRestarauntRepository;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alabra.voting.TestData.*;
import static ru.alabra.voting.TestUtil.userHttpBasic;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 30.08.2019
 */
class RestarauntRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = RestarauntRestController.REST_URL + '/';

    @Autowired
    private CrudRestarauntRepository repository;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(MC, KFC, BK, IL))));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + BK_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonUtil.writeValue(BK)));
    }


    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + BK_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertMatch(repository.findAll(), MC, KFC, IL);
    }

    @Test
    void deleteForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + BK_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        Restaurant updated = new Restaurant(BK);
        updated.setDescription("updated");

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + updated.getId())
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(repository.findById(BK_ID).orElse(null), updated);
    }

    @Test
    void save() throws Exception {
        Restaurant created = new Restaurant(null, "King Shrimps", "super duper");

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(created)));

        MvcResult result = action.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Restaurant returned = jsonUtil.readValue(contentAsString, Restaurant.class);
        created.setId(returned.getId());

        assertMatch(action, created);
        assertMatch(repository.findAll(), MC, KFC, BK, IL, created);
    }

}