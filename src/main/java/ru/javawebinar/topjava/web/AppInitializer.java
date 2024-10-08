package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.dao.MealDao;
import ru.javawebinar.topjava.model.implementation.inmemory.MealDaoInMemoryImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.time.LocalDateTime;
import java.time.Month;

import static org.slf4j.LoggerFactory.getLogger;

@WebListener
public class AppInitializer implements ServletContextListener {
    private static final Logger log = getLogger(AppInitializer.class);

    public static final String MEAL_DAO_IMPLEMENTATION = "mealDaoImplementation";
    public static final String CALORIES_PER_DAY_ATTRIBUTE = "caloriesPerDay";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContextListener.super.contextInitialized(event);
        log.info("Start of the global context initializing");
        ServletContext context = event.getServletContext();
        context.setAttribute(MEAL_DAO_IMPLEMENTATION, MealDaoInMemoryImpl.getInstance());
        context.setAttribute(CALORIES_PER_DAY_ATTRIBUTE, getCaloriesPerDay());
        initMeal(context);
    }

    private void initMeal(ServletContext context) {
        MealDao mealDao = (MealDao) context.getAttribute(MEAL_DAO_IMPLEMENTATION);
        mealDao.add(LocalDateTime.of(2024, Month.OCTOBER, 7, 20, 0), "Ужин", 410);
        mealDao.add(LocalDateTime.of(2024, Month.OCTOBER, 7, 11, 0), "Завтрак", 500);
        mealDao.add(LocalDateTime.of(2024, Month.OCTOBER, 7, 15, 0), "Обед", 800);
        mealDao.add(LocalDateTime.of(2024, Month.OCTOBER, 8, 0, 10), "Ночной дожор", 500);
    }

    private int getCaloriesPerDay() {
        return 1000;
    }

}
