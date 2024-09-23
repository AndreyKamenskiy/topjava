package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.NumberUtil;

public class DayStatistic {

    private int caloriesFact = 0;
    private final int caloriesPlan;
    private boolean excess = false;

    public DayStatistic(int caloriesPlan, int calories) {
        this.caloriesPlan = caloriesPlan;
        addCalories(calories);
    }

    public DayStatistic addCalories(int calories) {
        caloriesFact = NumberUtil.sumWithoutOverflow(caloriesFact, calories);
        if (caloriesFact > caloriesPlan) {
            excess = true;
        }
        return this;
    }

    public boolean isExcess() {
        return excess;
    }

    public int getCaloriesFact() {
        return caloriesFact;
    }

}
