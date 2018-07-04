package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final InMemoryMealRepositoryImpl repository = new InMemoryMealRepositoryImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        int id = getId(req);
        log.debug("doGet: action = {}; id = {}", action, id);
        // update request from meals.jsp
        if ("update".equals(action)){
            Meal meal = repository.get(id);
            req.setAttribute("meal", meal);
            req.getRequestDispatcher("mealForm.jsp").forward(req, resp);
        }

        // display MealsWithExceed list
        List<MealWithExceed> listWithExceeded = MealsUtil.getListWithExceeded(repository.getAll(),2000);
        req.setAttribute("meals", listWithExceeded);
        req.getRequestDispatcher("meals.jsp").forward(req, resp);
    }

    private int getId(HttpServletRequest req) {
        String id = req.getParameter("id");
        return id == null ? -1 : Integer.valueOf(id);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        int id = getId(req);
        String dateTime = req.getParameter("dateTime");
        String description = req.getParameter("description");
        String calories = req.getParameter("calories");
        log.debug("doPost: action = {}; id = {}; dateTime = {}; description = {}; calories = {}", action, id, dateTime, description, calories);

        // update request from mealForm.jsp
        if ("update".equals(action)){
            // update meal
            Meal meal = new Meal(LocalDateTime.parse(dateTime),description, Integer.valueOf(calories));
            meal.setId(id);
            repository.save(meal);
        }

        resp.sendRedirect("meals");
    }
}
