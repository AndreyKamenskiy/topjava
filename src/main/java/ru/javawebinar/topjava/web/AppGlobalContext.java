package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public class AppGlobalContext {
    //todo: remake to ServletContext
    /**
     *  global list of meals
     */
    private static List<Meal> meals;
    public static void setMeals(List<Meal> meals) {
        AppGlobalContext.meals = meals;
    }

    public static List<Meal> getMeals() {
        return meals;
    }

    /**
     *  The global property of calories per day to excess
     */
    private static int caloriesPerDay;

    public static void setCaloriesPerDay(int caloriesPerDay) {
        AppGlobalContext.caloriesPerDay = caloriesPerDay;
    }

    public static int getCaloriesPerDay() {
        return caloriesPerDay;
    }
}
