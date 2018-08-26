package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.contentJson;
import static ru.javawebinar.topjava.TestUtil.contentJsonArray;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealRestControllerTest extends AbstractControllerTest {
    static final String REST_URL = MealRestController.REST_URL + "/";

    @Autowired
    private MealService mealService;

    @Test
    void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertMatch(mealService.getAll(START_SEQ), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MealsUtil.getWithExceeded(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    void testUpdate() throws Exception {
        Meal updated = getUpdated();

        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON) // задання заголовку Content-Type
                .content(JsonUtil.writeValue(updated)))  // задання тіла запиту
                .andExpect(status().isOk());

        assertMatch(mealService.get(MEAL1_ID, START_SEQ), updated);
    }

    @Test
    void testCreate() throws Exception {
        Meal created = getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)        // задання заголовку Content-Type
                .content(JsonUtil.writeValue(created)))        // задання тіла запиту
                .andExpect(status().isCreated());               // перевірка статусу
        // convert ResultActions to Meal
        Meal returned = TestUtil.readFromJson(action, Meal.class);
        created.setId(returned.getId());

        assertMatch(returned, created);
        assertMatch(mealService.getAll(START_SEQ), created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }


    @Test
    public void testBetween() throws Exception {
        final ResultMatcher jsonArray = contentJsonArray(
                MealsUtil.createWithExceed(MEAL4, true),
                MealsUtil.createWithExceed(MEAL1, false));

        mockMvc.perform(get(REST_URL + "filter")
                .param("startDate", "2015-05-30").param("startTime", "07:00")
                .param("endDate", "2015-05-31").param("endTime", "11:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonArray);
    }

    @Test
    public void testEmptyFilter() throws Exception {
        mockMvc.perform(get(REST_URL + "filter?startDate=&endTime=")) // нічого не фільтруємо
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(contentJson(MealsUtil.getWithExceeded(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), USER.getCaloriesPerDay())));
    }

}
