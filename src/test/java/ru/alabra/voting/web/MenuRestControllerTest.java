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
import ru.alabra.voting.repository.MenuRepository;
import ru.alabra.voting.web.json.JsonUtil;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alabra.voting.TestData.*;
import static ru.alabra.voting.web.MenuRestController.REST_URL_RESTAURANTS;
import static ru.alabra.voting.web.MenuRestController.REST_URL_RESTAURANT_MENU;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 02.09.2019
 */
@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
//@WebAppConfiguration
//@ExtendWith(SpringExtension.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
class MenuRestControllerTest {

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private MenuRepository menuRepository;

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

    @Test
    void testGetByAllRestaurantsAndByDate() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL_RESTAURANTS + "/by-date")
                .param("date", M1.getDate().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(M1, M2, M3))));

    }

    @Test
    void testGetByAllRestaurantsAndByPeriod() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL_RESTAURANT_MENU.replace("{restaurantId}", String.valueOf(IL_ID))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(M4))));

    }

    @Test
    void testGetAllByRestaurant() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL_RESTAURANTS + "/by-period")
                .param("startDate", M1.getDate().toString())
                .param("endDate", M4.getDate().toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(M1, M2, M3, M4))));

    }

     @Test
    void create() throws Exception {
        Menu created = new Menu(null, LocalDate.now(), "new menu", BK);

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL_RESTAURANT_MENU.replace("{restaurantId}", String.valueOf(BK.getId())))
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
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL_RESTAURANT_MENU.replace("{restaurantId}", String.valueOf(BK.getId())) + "/" + M3_ID))
                .andExpect(status().isNoContent());
        assertMatch(menuRepository.findByRestaurantId(BK_ID), Collections.emptyList());
    }

    @Test
    void update() throws Exception {
        Menu updated = new Menu(M3);
        updated.setDescription("updated");

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL_RESTAURANT_MENU.replace("{restaurantId}", String.valueOf(BK.getId())) + "/" + M3_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(menuRepository.findByRestaurantIdAndId(BK_ID, M3_ID).orElse(null), updated);
    }

}