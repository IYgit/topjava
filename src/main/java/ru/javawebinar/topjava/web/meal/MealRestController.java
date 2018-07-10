package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

public class MealRestController{
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);
    private MealService service;


    public Meal create(Meal meal) {
        log.info("create meal {}",meal);
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) throws NotFoundException {
        log.info("delete id {}", id);
        service.delete(id, authUserId());
    }

    public Meal get(int id) throws NotFoundException {
        log.info("get id {}", id);
        return service.get(id, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update meal {}, id {}", meal, id);
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }

    public List<MealWithExceed> getAll() {
        log.info("getAll ");
        return MealsUtil.getWithExceeded(service.getAll(authUserId()), authUserCaloriesPerDay());
    }

    public List<MealWithExceed> getBetweenDateTime(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        log.info("getBetweenDateTime startDate {}, endDate {}, startTime {}, endTime {}",startDate, endDate, startTime, endTime);
        List<Meal> betweenDate = service.getBetweenDate(startDate == null ? LocalDate.MIN : startDate,
                endDate == null ? LocalDate.MAX : endDate, authUserId());

        return MealsUtil.getFilteredWithExceeded(betweenDate, authUserCaloriesPerDay(),
                startTime == null ? LocalTime.MIN : startTime,
                endTime == null ? LocalTime.MAX : endTime);
    }
}