package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenClosed;

@Repository
public class InMemoryMealRepository implements MealRepository {
    // userId to Map(mealId to Meal)
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        for (int i = 0; i < MealsUtil.userMeals.length; i++) {
            for (Meal meal : MealsUtil.userMeals[i]) {
                save(i, meal);
            }
        }
    }

    @Override
    public Meal save(int userId, Meal meal) {
        if (meal.isNew()) {
            int id = counter.incrementAndGet();
            meal.setId(id);
            repository.computeIfAbsent(userId, key -> new ConcurrentHashMap<>()).put(id, meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repository.getOrDefault(userId, Collections.emptyMap()).computeIfPresent(meal.getId(),
                (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        return repository.getOrDefault(userId, Collections.emptyMap()).remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        return repository.getOrDefault(userId, Collections.emptyMap()).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getOrDefault(userId, Collections.emptyMap())
                .values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> filter(int userId, LocalDate fromDate, LocalDate toDate) {
        return repository.getOrDefault(userId, Collections.emptyMap())
                .values()
                .stream()
                .filter(meal -> isBetweenClosed(meal.getDate(), fromDate, toDate))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

