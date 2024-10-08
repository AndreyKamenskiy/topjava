package ru.javawebinar.topjava.model.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface MealDao {

    boolean delete(int id);

    void add(LocalDateTime dateTime, String description, int calories);

    List<MealTo> filter(LocalTime startTime, LocalTime endTime, int caloriesPerDay);

    boolean update(LocalDateTime dateTime, String description, int calories, int id);

    Meal getMeal(Integer mealId);
}
