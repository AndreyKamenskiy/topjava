package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealDao implements MealDao {

    private final AtomicInteger idCount;
    private final Map<Integer, Meal> meals;

    public InMemoryMealDao() {
        idCount = new AtomicInteger(-1);
        meals = new ConcurrentHashMap<>();
    }

    @Override
    public boolean delete(int id) {
        return meals.remove(id) != null;
    }

    @Override
    public Meal add(Meal meal) {
        int currentId = idCount.incrementAndGet();
        Meal newMeal = new Meal(currentId, meal.getDateTime(), meal.getDescription(), meal.getCalories());
        meals.put(currentId, newMeal);
        return newMeal;
    }

    @Override
    public Meal update(Meal meal) {
        return meals.computeIfPresent(
                meal.getId(),
                (key, oldVal) -> new Meal(key, meal.getDateTime(), meal.getDescription(), meal.getCalories())
        );
    }

    @Override
    public Meal get(int id) {
        return meals.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }
}
