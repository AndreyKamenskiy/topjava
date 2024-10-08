package ru.javawebinar.topjava.model.implementation.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.model.dao.MealDao;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;

public class MealDaoInMemoryImpl implements MealDao {

    private static int idCount = 0;
    private static final Map<Integer, Meal> meals = new ConcurrentHashMap<>();

    private static MealDaoInMemoryImpl instance;

    private MealDaoInMemoryImpl() {
    }

    public static MealDaoInMemoryImpl getInstance() {
        if (instance == null) {
            instance = new MealDaoInMemoryImpl();
        }
        return instance;
    }

    @Override
    public boolean delete(int id) {
        return meals.remove(id) != null;
    }

    @Override
    public void add(LocalDateTime dateTime, String description, int calories) {
        meals.put(idCount, new Meal(dateTime, description, calories, idCount++));
    }

    @Override
    public boolean update(LocalDateTime dateTime, String description, int calories, int id) {
        Meal meal = meals.get(id);
        if (meal != null) {
            meals.put(id, new Meal(dateTime, description, calories, id));
            return true;
        }
        return false;
    }

    @Override
    public List<MealTo> filter(LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return filteredByStreams(new ArrayList<>(meals.values()), startTime, endTime, caloriesPerDay);
    }

    @Override
    public Meal getMeal(Integer mealId) {
        Meal meal = meals.get(mealId);
        if (meal == null) {
            return null;
        }
        return new Meal(meal.getDateTime(), meal.getDescription(), meal.getCalories(), meal.getId());
    }
}
