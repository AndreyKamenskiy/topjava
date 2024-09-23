package ru.javawebinar.topjava.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;
import static ru.javawebinar.topjava.util.UserMealsUtil.*;

class UserMealsUtilTest {

    private static final String FILTERED_BY_CYCLES = "filteredByCycles";
    private static final String FILTERED_BY_STREAMS = "filteredByStreams";
    private static final String FILTERED_BY_ONE_STREAM = "filteredByOneStream";
    private static final String FILTERED_BY_ONE_CYCLE = "filteredByOneCycle";

    private List<UserMeal> meals;
    private LocalTime from;
    private LocalTime to;
    private int calories;

    @BeforeEach
    void init() {
        meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 5, 10, 0), "Разные годы", 500),
                new UserMeal(LocalDateTime.of(2021, Month.JANUARY, 5, 10, 1), "Разные годы", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 2, 10, 10), "Разные месяцы", 500),
                new UserMeal(LocalDateTime.of(2020, Month.MARCH, 2, 10, 11), "Разные месяцы", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 3, 10, 20), "Разные дни", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 4, 10, 21), "Разные дни", 500),
                new UserMeal(LocalDateTime.of(2020, Month.MARCH, 30, 9, 0), "Завтрак", Integer.MAX_VALUE),
                new UserMeal(LocalDateTime.of(2020, Month.MARCH, 30, 9, 1), "Завтрак", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 1, 11, 0), "Обед1", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0), "Обед2", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 1, 13, 0), "Обед3", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 1, 14, 0), "Обед4", 501),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 15, 23, 57), "Ночной кофе", 10_000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 15, 23, 58), "Ночной тортик", 10_000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 15, 23, 59), "Ночной стейк", 10_000)
        );
    }

    @ParameterizedTest(name = "Empty list test method = {0}")
    @ValueSource(strings = {FILTERED_BY_CYCLES, FILTERED_BY_STREAMS, FILTERED_BY_ONE_STREAM, FILTERED_BY_ONE_CYCLE})
    void emptyListTest(String methodName) {
        meals = emptyList();
        from = LocalTime.of(20, 0);
        to = LocalTime.of(21, 0);
        List<UserMealWithExcess> filteredList = getFilteredList(methodName);
        assertNotNull(filteredList);
        assertTrue(filteredList.isEmpty());
    }

    @ParameterizedTest(name = "Overflow test method = {0}")
    @ValueSource(strings = {FILTERED_BY_CYCLES, FILTERED_BY_STREAMS, FILTERED_BY_ONE_STREAM, FILTERED_BY_ONE_CYCLE})
    void overflowTest(String methodName) {
        from = LocalTime.of(9, 0);
        to = LocalTime.of(9, 2);
        calories = 0;
        List<UserMealWithExcess> filteredList = getFilteredList(methodName);
        assertEquals(2, filteredList.size(), "Wrong size");
        assertIterableEquals(Arrays.asList(true, true),
                filteredList.stream().map(UserMealWithExcess::isExcess).collect(Collectors.toList()),
                "Wrong exceed value");
    }

    @ParameterizedTest(name = "Several dates test {index} from = {0}, to = {1}, method = {2}")
    @CsvSource(value = {
            "10:00, 10:10, filteredByCycles",
            "10:10, 10:20, filteredByCycles",
            "10:20, 10:30, filteredByCycles",
            "10:00, 10:10, filteredByStreams",
            "10:10, 10:20, filteredByStreams",
            "10:20, 10:30, filteredByStreams",
            "10:00, 10:10, filteredByOneStream",
            "10:10, 10:20, filteredByOneStream",
            "10:20, 10:30, filteredByOneStream",
            "10:00, 10:10, filteredByOneCycle",
            "10:10, 10:20, filteredByOneCycle",
            "10:20, 10:30, filteredByOneCycle"
    })
    void severalDatesTest(String fromStr, String toStr, String methodName) {
        from = LocalTime.parse(fromStr);
        to = LocalTime.parse(toStr);
        calories = 500;
        List<Boolean> expected = Arrays.asList(false, false);
        List<UserMealWithExcess> filteredList = getFilteredList(methodName);
        assertEquals(2, filteredList.size(), "Wrong filtered size");
        assertIterableEquals(expected,
                filteredList.stream().map(UserMealWithExcess::isExcess).collect(Collectors.toList()),
                "Wrong exceed value");
    }

    @ParameterizedTest(name = "Exceed test method = {0}")
    @ValueSource(strings = {FILTERED_BY_CYCLES, FILTERED_BY_STREAMS, FILTERED_BY_ONE_STREAM, FILTERED_BY_ONE_CYCLE})
    void exceedTest(String methodName) {
        from = LocalTime.of(11, 0);
        to = LocalTime.of(13, 1);
        calories = 1000;
        List<Boolean> expected = Arrays.asList(true, true, true);
        List<UserMealWithExcess> filteredList = getFilteredList(methodName);
        assertEquals(3, filteredList.size(), "Wrong filtered by cycles size");
        assertIterableEquals(expected,
                filteredList.stream().map(UserMealWithExcess::isExcess).collect(Collectors.toList()),
                "Wrong exceed value");
    }

    @ParameterizedTest(name = "Empty from test {index} to = {0} expected size = {1} method = {2}")
    @CsvSource(value = {
            "09:00, 0, filteredByCycles",
            "09:01, 1, filteredByCycles",
            "09:02, 2, filteredByCycles",
            "09:00, 0, filteredByStreams",
            "09:01, 1, filteredByStreams",
            "09:02, 2, filteredByStreams",
            "09:00, 0, filteredByOneStream",
            "09:01, 1, filteredByOneStream",
            "09:02, 2, filteredByOneStream",
            "09:00, 0, filteredByOneCycle",
            "09:01, 1, filteredByOneCycle",
            "09:02, 2, filteredByOneCycle"
    })
    void emptyFromTest(String fromStr, String sizeStr, String methodName) {
        from = null;
        to = LocalTime.parse(fromStr);
        int expectedSize = Integer.parseInt(sizeStr);
        calories = 1000;
        List<UserMealWithExcess> filteredList = getFilteredList(methodName);
        assertEquals(expectedSize, filteredList.size(), "Wrong size");
    }

    @ParameterizedTest(name = "Empty to test {index} to = {0} expected size = {1} method = {2}")
    @CsvSource(value = {
            "23:59, 1, filteredByCycles",
            "23:58, 2, filteredByCycles",
            "23:57, 3, filteredByCycles",
            "23:59, 1, filteredByStreams",
            "23:58, 2, filteredByStreams",
            "23:57, 3, filteredByStreams",
            "23:59, 1, filteredByOneStream",
            "23:58, 2, filteredByOneStream",
            "23:57, 3, filteredByOneStream",
            "23:59, 1, filteredByOneCycle",
            "23:58, 2, filteredByOneCycle",
            "23:57, 3, filteredByOneCycle"
    })
    void emptyToTest(String toStr, String sizeStr, String methodName) {
        from = LocalTime.parse(toStr);
        to = null;
        int expectedSize = Integer.parseInt(sizeStr);
        calories = 1000;
        List<UserMealWithExcess> filteredList = getFilteredList(methodName);
        assertEquals(expectedSize, filteredList.size(), "Wrong size");
    }

    @ParameterizedTest(name = "Empty from and to test method = {0}")
    @ValueSource(strings = {FILTERED_BY_CYCLES, FILTERED_BY_STREAMS, FILTERED_BY_ONE_STREAM, FILTERED_BY_ONE_CYCLE})
    void emptyFromToTest(String methodName) {
        int expectedSize = meals.size();
        calories = 1000;
        from = null;
        to = null;
        List<UserMealWithExcess> filteredList = getFilteredList(methodName);
        assertEquals(expectedSize, filteredList.size(), "Wrong size");
    }

    List<UserMealWithExcess> getFilteredList(String methodName) {
        switch (methodName) {
            case FILTERED_BY_CYCLES:
                return filteredByCycles(meals, from, to, calories);
            case FILTERED_BY_STREAMS:
                return filteredByStreams(meals, from, to, calories);
            case FILTERED_BY_ONE_STREAM:
                return filteredByOneStream(meals, from, to, calories);
            case FILTERED_BY_ONE_CYCLE:
                return filteredByOneCycle(meals, from, to, calories);
        }
        return null;
    }

}