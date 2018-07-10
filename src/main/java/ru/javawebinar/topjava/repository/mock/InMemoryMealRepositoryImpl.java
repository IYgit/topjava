package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.*;
import static ru.javawebinar.topjava.web.SecurityUtil.*;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, authUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save meal {} userId {}", meal, userId);
        checkUserId(authUserId(), userId);

        Map<Integer, Meal> mealMap = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public void delete(int id, int userId) {
        log.info("delete id {} userId {}", id, userId);
        checkUserId(authUserId(), userId);

        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap != null)
            mealMap.remove(id);
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get id {} userId {}", id, userId);
        checkUserId(authUserId(), userId);

        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap == null ? null : mealMap.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll userId {}", userId);
        checkUserId(authUserId(), userId);

        Map<Integer, Meal> mealMap = repository.get(userId);
        return mealMap == null ? null : mealMap.values().stream()
                .sorted((m1, m2) -> {
                    int compare = m2.getDate().compareTo(m1.getDate());
                    return compare == 0 ? m1.getTime().compareTo(m2.getTime()) : compare;
                }).collect(Collectors.toList());
    }
}

