package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;
import java.util.function.Predicate;

public interface MealRepository {
    Meal save(Meal meal);

    Meal get(int id);

    boolean delete(int id);

    Collection<Meal> getAll();

    Collection<Meal> getFiltered(Predicate<Meal> predicate);
}
