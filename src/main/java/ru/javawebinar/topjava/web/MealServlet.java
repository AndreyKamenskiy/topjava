package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;

    // user depended filters will be implemented later
    private final LocalDate[] fromDate = new LocalDate[1];
    private final LocalDate[] toDate = new LocalDate[1];
    private final LocalTime[] fromTime = new LocalTime[1];
    private final LocalTime[] toTime = new LocalTime[1];

    @Override
    public void init() {
        log.info("Init spring");
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            controller = appCtx.getBean(MealRestController.class);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if ("filter".equals(action)) {
            String dateStr = request.getParameter("fromDate");
            fromDate[0] = dateStr != null && !dateStr.isEmpty() ? LocalDate.parse(dateStr) : null;
            dateStr = request.getParameter("toDate");
            toDate[0] = dateStr != null && !dateStr.isEmpty() ? LocalDate.parse(dateStr) : null;
            String timeStr = request.getParameter("fromTime");
            fromTime[0] = timeStr != null && !timeStr.isEmpty() ? LocalTime.parse(timeStr) : null;
            timeStr = request.getParameter("toTime");
            toTime[0] = timeStr != null && !timeStr.isEmpty() ? LocalTime.parse(timeStr) : null;
        } else {
            String id = request.getParameter("id");
            Integer mealId = id == null || id.isEmpty() ? null : Integer.parseInt(id);

            Meal meal = new Meal(mealId,
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")));

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            if (meal.isNew()) {
                controller.create(meal);
            } else {
                controller.update(meal, mealId);
            }
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", controller.filter(fromDate[0], toDate[0], fromTime[0], toTime[0]));
                request.setAttribute("fromDate", fromDate);
                request.setAttribute("toDate", toDate);
                request.setAttribute("fromTime", fromTime);
                request.setAttribute("toTime", toTime);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
