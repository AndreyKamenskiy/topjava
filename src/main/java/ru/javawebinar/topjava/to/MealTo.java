package ru.javawebinar.topjava.to;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MealTo(
            @JsonProperty("id") Integer id,
            @JsonProperty("dateTime") LocalDateTime dateTime,
            @JsonProperty("description") String description,
            @JsonProperty("calories") int calories,
            @JsonProperty("excess") boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
