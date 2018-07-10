package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface MealService {
    Meal create(Meal meal, int userId);

    void delete(int id, int userId) throws NotFoundException;

    Meal get(int id, int userId) throws NotFoundException;

    void update(Meal meal, int userId);

    List<Meal> getAll(int userId);

    List<Meal> getBetweenDateTime(LocalDateTime start, LocalDateTime end, int userId);

    default List<Meal> getBetweenDate(LocalDate srart, LocalDate end, int userId){
        return getBetweenDateTime(LocalDateTime.of(srart, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX), userId);
    }
}