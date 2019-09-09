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
import ru.alabra.voting.model.Restaurant;
import ru.alabra.voting.repository.RestarauntRepository;
import ru.alabra.voting.web.json.JsonUtil;

import javax.annotation.PostConstruct;
import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.alabra.voting.TestData.*;

/**
 * @author Alexander Abramov (alllexe@mail.ru)
 * @version 1
 * @since 30.08.2019
 */
@SpringJUnitWebConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-mvc.xml",
        "classpath:spring/spring-db.xml"
})
//@WebAppConfiguration
//@ExtendWith(SpringExtension.class)
@Transactional
class RestarauntRestControllerTest {

    private static final String REST_URL = RestarauntRestController.REST_URL + '/';

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private RestarauntRepository repository;

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
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonUtil.writeValue(Arrays.asList(MC, KFC, BK, IL))));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + BK_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(jsonUtil.writeValue(BK)));
    }


    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + BK_ID))
                .andExpect(status().isNoContent());
        assertMatch(repository.getAll(), MC, KFC, IL);
    }

    @Test
    void update() throws Exception {
        Restaurant updated = new Restaurant(BK);
        updated.setDescription("updated");

        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(repository.findById(BK_ID).orElse(null), updated);
    }

    @Test
    void save() throws Exception {
        Restaurant created = new Restaurant(null, "King Shrimps", "super duper");

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUtil.writeValue(created)));

        MvcResult result = action.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Restaurant returned = jsonUtil.readValue(contentAsString, Restaurant.class);
        created.setId(returned.getId());

        assertMatch(action, created);
        assertMatch(repository.getAll(), MC, KFC, BK, IL, created);
    }

}