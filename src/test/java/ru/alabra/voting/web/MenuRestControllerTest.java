package ru.alabra.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.alabra.voting.model.Menu;
import ru.alabra.voting.repository.CrudMenuRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alabra.voting.TestData.*;
import static ru.alabra.voting.TestUtil.userHttpBasic;
import static ru.alabra.voting.web.MenuRestController.REST_URL;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 02.09.2019
 */
class MenuRestControllerTest extends AbstractRestControllerTest {

    @Autowired
    private CrudMenuRepository repository;

    @Test
    void create() throws Exception {
        Menu created = new Menu(null, LocalDate.now(), "new menu", BK);

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(created)));

        MvcResult result = action.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Menu returned = jsonUtil.readValue(contentAsString, Menu.class);
        created.setId(returned.getId());

        assertMatch(action, created);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + M3_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertMatch(repository.findByRestaurantId(BK_ID), Collections.emptyList());
    }

    @Test
    void update() throws Exception {
        Menu updated = new Menu(M3);
        updated.setDescription("updated");

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + "/" + M3_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(repository.findByRestaurantIdAndId(BK_ID, M3_ID).orElse(null), updated);
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/" + M1_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(M1)));
    }

    @Test
    void findByRestaurant() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/find/by-restaurant-id")
                .param("id", String.valueOf(MC_ID))
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(M1))));
    }

    @Test
    void findByDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/find/by-date")
                .param("date", M1.getDate().toString())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(M1, M2, M3))));
    }

    @Test
    void findByPeriod() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "/find/by-period")
                .param("startDate", M1.getDate().toString())
                .param("endDate", M4.getDate().toString())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(M1, M2, M3, M4))));
    }

    @Test
    void deleteForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + "/" + M3_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

}