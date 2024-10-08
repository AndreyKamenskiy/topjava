package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.model.dao.MealDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;
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
    private static final String ERROR_MESSAGE = "errorMess";
    private static final String INFO_MESSAGE = "infoMess";
    private static final String MEALS_PAGE = "/meals.jsp";
    private static final String MEALS_REDIRECT_PAGE = "/meals";
    private static final String EDIT_PAGE = "/editMeal.jsp";


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        log.debug("process doGet {}?{}", request.getRequestURI(), request.getQueryString());
        MealDao mealDao = getMealDao();
        clearMessages(request);
        String action = request.getParameter(ACTION_PARAM);
        Integer mealId = getMealId(request);
        String pageName = MEALS_PAGE;
        if (EDIT_ACTION.equalsIgnoreCase(action)) {
            pageName = EDIT_PAGE;
            prepareEditMealAttributes(mealDao, mealId, request);
        } else if (DELETE_ACTION.equalsIgnoreCase(action)) {
            if (mealId != null && mealDao != null) {
                boolean res = mealDao.delete(mealId);
                if (res) {
                    addMessage(request, INFO_MESSAGE, String.format("Deleted meal with id = %d", mealId));
                } else {
                    addMessage(request, ERROR_MESSAGE, String.format("Can't delete meal with id = %d", mealId));
                }
            }
            response.sendRedirect(request.getContextPath() + MEALS_REDIRECT_PAGE);
            return;
        } else if (CREATE_ACTION.equalsIgnoreCase(action)) {
            pageName = EDIT_PAGE;
        } else {
            prepareMealListAttributes(mealDao, request);
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
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(dateTimeStr);
        } catch (DateTimeParseException ignore) {
        }
        String description = decode(request.getParameter("description"));
        String caloriesStr = request.getParameter("calories");
        Integer calories = null;
        try {
            calories = Integer.parseInt(caloriesStr);
        } catch (NumberFormatException ignore) {
        }
        if (EDIT_ACTION.equalsIgnoreCase(action)) {
            Integer mealId = getMealId(request);
            if (dateTime != null && description != null && !description.isEmpty() && calories != null
                    && mealId != null) {
                getMealDao().update(dateTime, description, calories, mealId);
            }
        } else if (CREATE_ACTION.equalsIgnoreCase(action)) {
            if (dateTime != null && description != null && !description.isEmpty() && calories != null) {
                getMealDao().add(dateTime, description, calories);
            }
        }
        response.sendRedirect(request.getContextPath() + MEALS_REDIRECT_PAGE);
    }

    private Integer getMealId(HttpServletRequest request) {
        String mealIdStr = request.getParameter(ID_PARAM);
        Integer mealId = null;
        try {
            mealId = Integer.parseInt(mealIdStr);
        } catch (NumberFormatException ignore) {
        }
        return mealId;
    }

    private MealDao getMealDao() {
        MealDao mealDao = null;
        try {
            mealDao = (MealDao) getServletContext().getAttribute(MEAL_DAO_IMPLEMENTATION);
        } catch (Exception ignore) {
        }
        return mealDao;
    }

    private void prepareMealListAttributes(MealDao mealDao, HttpServletRequest request) {
        log.debug("prepare meal list attributes");
        List<MealTo> mealsTo = Collections.emptyList();
        int caloriesPerDay;
        try {
            caloriesPerDay = (int) getServletContext().getAttribute(CALORIES_PER_DAY_ATTRIBUTE);
            mealsTo = mealDao.filter(null, null, caloriesPerDay);
        } catch (Exception ignore) {
        }
        mealsTo = mealsTo
                .stream()
                .sorted(Comparator.comparing(MealTo::getDateTime))
                .collect(Collectors.toList());
        request.setAttribute(MEALS_TO_ATTRIBUTE, mealsTo);
    }

    private void prepareEditMealAttributes(MealDao mealDao, Integer mealId, HttpServletRequest request) {
        log.debug("prepare edit meal attributes");
        Meal meal = mealDao.getMeal(mealId);
        if (meal == null) {
            addMessage(request, ERROR_MESSAGE, String.format("Can't find meal with id = %d", mealId));
        } else {
            request.setAttribute(MEAL_ATTRIBUTE, meal);
        }
    }

    private void addMessage(HttpServletRequest request, String type, String mess) {
        request.setAttribute(type, mess);
    }

    private void clearMessages(HttpServletRequest request) {
        request.setAttribute(ERROR_MESSAGE, null);
        request.setAttribute(INFO_MESSAGE, null);
    }

    private String decode(String encoded) {
        String decodedString = null;
        try {
            decodedString = URLDecoder.decode(encoded, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        return decodedString;
    }

}
