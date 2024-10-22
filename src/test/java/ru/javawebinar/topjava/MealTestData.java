package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int USER_MEAL1_ID = START_SEQ + 3;
    public static final int USER_MEAL2_ID = START_SEQ + 5;
    public static final int USER_MEAL3_ID = START_SEQ + 4;

    public static final LocalDate START_TIME = LocalDate.of(2024, 10, 20);
    public static final LocalDate END_TIME = LocalDate.of(2024, 10, 20);

    public static final Meal userMeal1 =
            new Meal(USER_MEAL1_ID, LocalDateTime.of(2024, 10, 20, 9, 51, 0, 0), "завтрак юзера", 500);

    public static final Meal userMeal2 =
            new Meal(USER_MEAL2_ID, LocalDateTime.of(2024, 10, 20, 13, 51, 0, 0), "обед юзера", 500);

    public static final Meal userMeal3 =
            new Meal(USER_MEAL3_ID, LocalDateTime.of(2024, 10, 21, 19, 51, 0, 0), "ужин юзера", 500);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2024, 1, 20, 12, 0, 0, 0), "Новый прием пищи", 500);
    }

    public static Meal getUpdated() {
        return new Meal(USER_MEAL1_ID, LocalDateTime.of(2021, 11, 1, 15, 15, 15, 0), "Измененный обед", 1500);
    }

    public static Meal getNewDuplicate() {
        return new Meal(null, userMeal1.getDateTime(), "второй завтрак юзера", 500);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}
