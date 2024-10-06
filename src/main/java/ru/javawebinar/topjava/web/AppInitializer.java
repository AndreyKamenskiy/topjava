package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@WebListener
public class AppInitializer implements ServletContextListener {
    private static final Logger log = getLogger(AppInitializer.class);

    public static final String MEALS_ATTRIBUTE = "meals";
    public static final String CALORIES_PER_DAY_ATTRIBUTE = "caloriesPerDay";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContextListener.super.contextInitialized(event);
        log.info("Start of the global context initializing");
        ServletContext context = event.getServletContext();
        context.setAttribute(MEALS_ATTRIBUTE, getInitMeal());
        context.setAttribute(CALORIES_PER_DAY_ATTRIBUTE, getCaloriesPerDay());
    }

    private List<Meal> getInitMeal() {
        return Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 5, 10, 0), "Разные годы", 500),
                new Meal(LocalDateTime.of(2021, Month.JANUARY, 5, 10, 1), "Разные годы", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 2, 10, 10), "Разные месяцы", 500),
                new Meal(LocalDateTime.of(2020, Month.MARCH, 2, 10, 11), "Разные месяцы", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 3, 10, 20), "Разные дни", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 4, 10, 21), "Разные дни", 500),
                new Meal(LocalDateTime.of(2020, Month.MARCH, 30, 9, 0), "Завтрак", 500000),
                new Meal(LocalDateTime.of(2020, Month.MARCH, 30, 9, 1), "Завтрак", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 1, 11, 0), "Обед1", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0), "Обед2", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 1, 13, 0), "Обед3", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 1, 14, 0), "Обед4", 501),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 15, 23, 57), "Ночной кофе", 10_000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 15, 23, 58), "Ночной тортик", 10_000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 15, 23, 59), "Ночной стейк", 10_000)
        );
    }

    private int getCaloriesPerDay() {
        return 1000;
    }

}
