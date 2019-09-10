package ru.alabra.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import ru.alabra.voting.model.Menu;
import ru.alabra.voting.repository.CrudMenuRepository;
import ru.alabra.voting.web.json.JsonUtil;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alabra.voting.TestData.*;
import static ru.alabra.voting.TestUtil.userHttpBasic;
import static ru.alabra.voting.web.MenuRestController.REST_URL_RESTAURANTS;
import static ru.alabra.voting.web.MenuRestController.REST_URL_RESTAURANT_MENU;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 02.09.2019
 */
class MenuRestControllerTest extends AbstractRestControllerTest {

    @Autowired
    private CrudMenuRepository menuRepository;

    @Test
    void findAllByRestaurantsAndByDate() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL_RESTAURANTS + "/by-date")
                .param("date", M1.getDate().toString())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(M1, M2, M3))));

    }

    @Test
    void findAllByRestaurantsAndByPeriod() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL_RESTAURANT_MENU.replace("{restaurantId}", String.valueOf(IL_ID)))
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Collections.singletonList(M4))));

    }

    @Test
    void findAllByRestaurant() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL_RESTAURANTS + "/by-period")
                .param("startDate", M1.getDate().toString())
                .param("endDate", M4.getDate().toString())
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(M1, M2, M3, M4))));

    }

     @Test
    void create() throws Exception {
        Menu created = new Menu(null, LocalDate.now(), "new menu", BK);

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL_RESTAURANT_MENU.replace("{restaurantId}", String.valueOf(BK.getId())))
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
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL_RESTAURANT_MENU.replace("{restaurantId}", String.valueOf(BK.getId())) + "/" + M3_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());
        assertMatch(menuRepository.findByRestaurantId(BK_ID), Collections.emptyList());
    }

    @Test
    void deleteForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL_RESTAURANT_MENU.replace("{restaurantId}", String.valueOf(BK.getId())) + "/" + M3_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isForbidden());
    }

    @Test
    void update() throws Exception {
        Menu updated = new Menu(M3);
        updated.setDescription("updated");

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL_RESTAURANT_MENU.replace("{restaurantId}", String.valueOf(BK.getId())) + "/" + M3_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(menuRepository.findByRestaurantIdAndId(BK_ID, M3_ID).orElse(null), updated);
    }

}