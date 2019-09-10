package ru.alabra.voting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.alabra.voting.model.Role;
import ru.alabra.voting.model.User;
import ru.alabra.voting.repository.CrudUserRepository;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alabra.voting.TestData.*;
import static ru.alabra.voting.TestUtil.userHttpBasic;

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
    public void getForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isForbidden());
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

        assertMatchIgnoringFields(new String[]{"password"}, repository.findAll(), Arrays.asList(ADMIN, USER2, USER3));
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

        assertMatchIgnoringFields(new String[]{"password"}, repository.findById(USER_ID).orElse(null), updated);
    }
}