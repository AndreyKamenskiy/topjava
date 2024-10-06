package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;

public class MealTo {
    private final LocalDateTime dateTime;

    @SuppressWarnings("unused")
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    private final String description;

    @SuppressWarnings("unused")
    public String getDescription() {
        return description;
    }

    private final int calories;

    @SuppressWarnings("unused")
    public int getCalories() {
        return calories;
    }

    private final boolean excess;

    @SuppressWarnings("unused")
    public boolean getExcess() {
        return excess;
    }

    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
