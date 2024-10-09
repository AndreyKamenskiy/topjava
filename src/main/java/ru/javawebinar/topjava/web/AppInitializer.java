package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.InMemoryMealDao;
import ru.javawebinar.topjava.model.Meal;

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
        MealDao mealDao = new InMemoryMealDao();
        context.setAttribute(MEAL_DAO_IMPLEMENTATION, mealDao);
        context.setAttribute(CALORIES_PER_DAY_ATTRIBUTE, getCaloriesPerDay());
        initMeal(mealDao);
    }

    private void initMeal(MealDao mealDao) {
        mealDao.add(new Meal(null, LocalDateTime.of(2024, Month.OCTOBER, 7, 20, 0), "Ужин", 410));
        mealDao.add(new Meal(null, LocalDateTime.of(2024, Month.OCTOBER, 7, 11, 0), "Завтрак", 500));
        mealDao.add(new Meal(null, LocalDateTime.of(2024, Month.OCTOBER, 7, 15, 0), "Обед", 800));
        mealDao.add(new Meal(null, LocalDateTime.of(2024, Month.OCTOBER, 8, 0, 10), "Ночной дожор", 500));
    }

    private int getCaloriesPerDay() {
        return 1000;
    }

}
