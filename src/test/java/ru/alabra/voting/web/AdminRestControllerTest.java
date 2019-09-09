package ru.alabra.voting.web;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.alabra.voting.model.Menu;
import ru.alabra.voting.model.Restaurant;
import ru.alabra.voting.model.Role;
import ru.alabra.voting.model.User;
import ru.alabra.voting.repository.CrudUserRepository;

import javax.annotation.PostConstruct;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alabra.voting.TestData.*;
import static ru.alabra.voting.TestData.M3;
import static ru.alabra.voting.TestUtil.userHttpBasic;
import static ru.alabra.voting.web.MenuRestController.REST_URL_RESTAURANT_MENU;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 28.08.2019
 */
class AdminRestControllerTest extends AbstractRestControllerTest {

    private static final String REST_URL = AdminRestController.REST_URL + '/';

    @Autowired
    private CrudUserRepository repository;

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonUtil.writeValue(ADMIN)));
    }

    @Test
    void findAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(ADMIN, USER, USER2, USER3))));
    }

    @Test
    void create() throws Exception {
        User created = new User(null, "User 55", "user55@mail.ru", "534534535", Role.ROLE_USER);

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(jsonUtil, created, created.getPassword())));

        MvcResult result = action.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        User returned = jsonUtil.readValue(contentAsString, User.class);
        created.setId(returned.getId());

        assertMatch(action, created);
        assertMatchIgnoringFields(new String[]{"password"}, repository.findAll(), ADMIN, USER, USER2, USER3, created);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + USER_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());

        assertMatchIgnoringFields(new String[]{"password"}, repository.findAll(), Arrays.asList(ADMIN, USER, USER2, USER3));
    }

    @Test
    void update() throws Exception {

        User updated = new User(USER);
        updated.setName("updated");

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + USER_ID)
                .with(userHttpBasic(ADMIN))
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithPassword(jsonUtil, updated, updated.getPassword())))
                .andExpect(status().isNoContent());

        assertMatch(repository.findById(USER_ID).orElse(null), updated);
    }
}