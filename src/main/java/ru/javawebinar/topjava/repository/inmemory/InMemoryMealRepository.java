package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepository implements MealRepository {
    // userId to Map(mealId to Meal)
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);
    private final Map<Integer, Meal> emptyMealMap = new ConcurrentHashMap<>();


//    todo: move initialization to another place
    /*{
        MealsUtil.meals.forEach(this::save);
    }*/

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            int id = counter.incrementAndGet();
            meal.setId(id);
            repository.computeIfAbsent(userId, key -> new ConcurrentHashMap<>()).put(id, meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.getOrDefault(userId, emptyMealMap).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        return repository.getOrDefault(userId, emptyMealMap).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        return repository.getOrDefault(userId, emptyMealMap).get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.getOrDefault(userId, emptyMealMap).values();
    }
}

