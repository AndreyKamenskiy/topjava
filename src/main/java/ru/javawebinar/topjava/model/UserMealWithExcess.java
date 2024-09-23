package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    private final DayStatistic dayStatistic;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        this.dayStatistic = null;
    }

    public UserMealWithExcess(UserMeal meal, DayStatistic dayStatistic) {
        dateTime = meal.getDateTime();
        calories = meal.getCalories();
        description = meal.getDescription();
        this.dayStatistic = dayStatistic;
        excess = false;
    }

    public boolean isExcess() {
        return dayStatistic != null ? dayStatistic.isExcess() : excess;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
