package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.web.AppInitializer.CALORIES_PER_DAY_ATTRIBUTE;
import static ru.javawebinar.topjava.web.AppInitializer.MEALS_ATTRIBUTE;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String MEALS_TO_ATTRIBUTE = "mealsToList";

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        List<Meal> meals = Collections.emptyList();
        int caloriesPerDay = 0;
        try {
            meals = (List<Meal>) getServletContext().getAttribute(MEALS_ATTRIBUTE);
            caloriesPerDay = (int) getServletContext().getAttribute(CALORIES_PER_DAY_ATTRIBUTE);
        } catch (Exception ignore) {
        }
        List<MealTo> mealToList = MealsUtil.filteredByStreams(meals, null, null, caloriesPerDay)
                .stream()
                .sorted(Comparator.comparing(MealTo::getDateTime))
                .collect(Collectors.toList());
        log.debug("Created {} MealTo", mealToList.size());
        request.setAttribute(MEALS_TO_ATTRIBUTE, mealToList);
        log.debug("forward to meals");
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
