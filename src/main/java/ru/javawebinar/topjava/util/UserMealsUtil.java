package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.DayStatistic;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {

    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак"
                        , 500), new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime,
                                                            LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateToCalories = new HashMap<>();
        for (UserMeal meal : meals) {
            dateToCalories.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), NumberUtil::sumWithoutOverflow);
        }
        List<UserMealWithExcess> res = new ArrayList<>();
        for (UserMeal meal : meals) {
            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                res.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        dateToCalories.getOrDefault(meal.getDateTime().toLocalDate(), 0) > caloriesPerDay));
            }
        }
        return res;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime,
                                                             LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dateToCalories = meals
                .stream()
                .collect(Collectors.groupingBy(
                        meal -> meal.getDateTime().toLocalDate(),
                        new SummingIntWithoutOverflow()
                ));
        return meals.stream()
                .filter(meal -> isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        dateToCalories.getOrDefault(meal.getDateTime().toLocalDate(), 0) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByOneStream(List<UserMeal> meals, LocalTime startTime,
                                                               LocalTime endTime, int caloriesPerDay) {
        return meals.stream().collect(collectFilteredList(startTime, endTime, caloriesPerDay));
    }

    public static List<UserMealWithExcess> filteredByOneCycle(List<UserMeal> meals, LocalTime startTime,
                                                              LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> res = new ArrayList<>();
        Map<LocalDate, DayStatistic> daysMap = new HashMap<>();
        for (UserMeal meal : meals) {
            DayStatistic stat = daysMap.merge(meal.getDateTime().toLocalDate(), new DayStatistic(caloriesPerDay,
                    meal.getCalories()), (d1, d2) -> d1.addCalories(d2.getCaloriesFact()));
            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                res.add(new UserMealWithExcess(meal, stat));
            }
        }
        return res;
    }

    private static Collector<UserMeal, ?, List<UserMealWithExcess>> collectFilteredList(LocalTime startTime,
                                                                                        LocalTime endTime,
                                                                                        int caloriesPerDay) {

        class DayMeals {
            int calories = 0;
            final List<UserMeal> meals = new ArrayList<>();

            DayMeals(UserMeal meal) {
                calories += meal.getCalories();
                if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                    meals.add(meal);
                }
            }
        }

        BiFunction<DayMeals, DayMeals, DayMeals> mergeDayMeals = (lhs, rhs) -> {
            lhs.calories = NumberUtil.sumWithoutOverflow(lhs.calories, rhs.calories);
            lhs.meals.addAll(rhs.meals);
            return lhs;
        };

        return Collector.<UserMeal, Map<LocalDate, DayMeals>, List<UserMealWithExcess>>of(
                HashMap::new,
                (acc, meal) -> acc.merge(meal.getDateTime().toLocalDate(), new DayMeals(meal), mergeDayMeals),
                (lhs, rhs) -> {
                    for (Map.Entry<LocalDate, DayMeals> entry : rhs.entrySet()) {
                        lhs.merge(entry.getKey(), entry.getValue(), mergeDayMeals);
                    }
                    return lhs;
                },
                m -> m.entrySet()
                        .stream()
                        .flatMap((entry) -> {
                            boolean exceed = entry.getValue().calories > caloriesPerDay;
                            return entry.getValue().meals
                                    .stream()
                                    .map(userMeal -> new UserMealWithExcess(
                                            userMeal.getDateTime(),
                                            userMeal.getDescription(),
                                            userMeal.getCalories(),
                                            exceed
                                    ));
                        })
                        .collect(Collectors.toList())
        );
    }

    private static class SummingIntWithoutOverflow
            implements Collector<UserMeal, int[], Integer> {

        @Override
        public Supplier<int[]> supplier() {
            return () -> new int[1];
        }

        @Override
        public BiConsumer<int[], UserMeal> accumulator() {
            return (acc, meal) -> acc[0] = NumberUtil.sumWithoutOverflow(acc[0], meal.getCalories());
        }

        @Override
        public BinaryOperator<int[]> combiner() {
            return (lhs, rhs) -> {
                lhs[0] = NumberUtil.sumWithoutOverflow(lhs[0], rhs[0]);
                return lhs;
            };
        }

        @Override
        public Function<int[], Integer> finisher() {
            return (a) -> a[0];
        }

        @Override
        public Set<Characteristics> characteristics() {
            return new HashSet<>(Arrays.asList(Characteristics.CONCURRENT, Characteristics.UNORDERED));
        }
    }

}
