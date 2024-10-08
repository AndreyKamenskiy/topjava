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

    private final int id;

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }


    public MealTo(LocalDateTime dateTime, String description, int calories, boolean excess, int id) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        this.id = id;
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
