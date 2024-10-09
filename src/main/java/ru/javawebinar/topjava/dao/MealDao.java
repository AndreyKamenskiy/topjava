package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {

    boolean delete(int id);

    Meal add(Meal meal);

    Meal update(Meal meal);

    Meal get(int id);

    List<Meal> getAll();
}
