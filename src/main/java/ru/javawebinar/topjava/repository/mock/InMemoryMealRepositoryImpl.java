package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class InMemoryMealRepositoryImpl implements MealRepository{
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        log.debug("save {}", meal);
        if (meal.getId() == null){
            meal.setId(counter.incrementAndGet());
            return repository.computeIfAbsent(meal.getId(), id -> meal);
        }
        return repository.computeIfPresent(meal.getId(), (id, oldValue) -> meal);
    }

    @Override
    public Meal get(int id) {
        log.debug("get {}", id);
        return repository.get(id);
    }

    @Override
    public boolean delete(int id) {
        log.debug("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public Collection<Meal> getAll() {
        log.debug("getAll");
        return repository.values();
    }

    @Override
    public Collection<Meal> getFiltered(Predicate<Meal> predicate) {
        log.debug("getFiltered {}", predicate);
        return repository.values().stream().filter(predicate).collect(Collectors.toList());
    }
}
