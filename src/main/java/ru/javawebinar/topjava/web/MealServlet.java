package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.exception.NoSuchEntityException;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;
import static ru.javawebinar.topjava.web.AppInitializer.CALORIES_PER_DAY_ATTRIBUTE;
import static ru.javawebinar.topjava.web.AppInitializer.MEAL_DAO_IMPLEMENTATION;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String MEALS_TO_ATTRIBUTE = "mealsToList";
    private static final String MEAL_ATTRIBUTE = "meal";
    private static final String ACTION_PARAM = "action";
    private static final String ID_PARAM = "mealId";
    private static final String EDIT_ACTION = "edit";
    private static final String DELETE_ACTION = "delete";
    private static final String CREATE_ACTION = "create";
    private static final String MEALS_PAGE = "/meals.jsp";
    private static final String MEALS_REDIRECT_PAGE = "/meals";
    private static final String EDIT_PAGE = "/editMeal.jsp";

    private MealDao mealDao;
    private int caloriesPerDay;

    @Override
    public void init() {
        mealDao = (MealDao) getServletContext().getAttribute(MEAL_DAO_IMPLEMENTATION);
        caloriesPerDay = (int) getServletContext().getAttribute(CALORIES_PER_DAY_ATTRIBUTE);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        log.debug("process doGet {}?{}", request.getRequestURI(), request.getQueryString());
        String action = request.getParameter(ACTION_PARAM);
        String pageName = MEALS_PAGE;
        if (EDIT_ACTION.equalsIgnoreCase(action)) {
            int mealId = getMealId(request);
            pageName = EDIT_PAGE;
            prepareEditMealAttributes(mealId, request);
        } else if (DELETE_ACTION.equalsIgnoreCase(action)) {
            int mealId = getMealId(request);
            mealDao.delete(mealId);
            response.sendRedirect(request.getContextPath() + MEALS_REDIRECT_PAGE);
            return;
        } else if (CREATE_ACTION.equalsIgnoreCase(action)) {
            pageName = EDIT_PAGE;
        } else {
            prepareMealListAttributes(request);
        }
        log.debug("forward to {}", pageName);
        request.getRequestDispatcher(pageName).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("process doPost {}?{}", request.getRequestURI(), request.getQueryString());
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter(ACTION_PARAM);
        String dateTimeStr = request.getParameter("dateTime");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
        String description = decode(request.getParameter("description"));
        String caloriesStr = request.getParameter("calories");
        int calories = Integer.parseInt(caloriesStr);
        if (EDIT_ACTION.equalsIgnoreCase(action)) {
            mealDao.update(new Meal(getMealId(request), dateTime, description, calories));
        } else if (CREATE_ACTION.equalsIgnoreCase(action)) {
            mealDao.add(new Meal(null, dateTime, description, calories));
        }
        response.sendRedirect(request.getContextPath() + MEALS_REDIRECT_PAGE);
    }

    private int getMealId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter(ID_PARAM));
    }

    private void prepareMealListAttributes(HttpServletRequest request) {
        log.debug("prepare meal list attributes");
        List<MealTo> mealsTo = filteredByStreams(mealDao.getAll(), null, null, caloriesPerDay);
        request.setAttribute(MEALS_TO_ATTRIBUTE, mealsTo);
    }

    private void prepareEditMealAttributes(int mealId, HttpServletRequest request) throws ServletException {
        log.debug("prepare edit meal attributes");
        Meal meal = mealDao.get(mealId);
        if (meal != null) {
            request.setAttribute(MEAL_ATTRIBUTE, meal);
        } else {
            throw new NoSuchEntityException(String.format("A non-existent meal id %d was requested", mealId));
        }
    }

    private String decode(String encoded) throws UnsupportedEncodingException {
        return URLDecoder.decode(encoded, "UTF-8");
    }

}
