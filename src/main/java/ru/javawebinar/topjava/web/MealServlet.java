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

    ConfigurableApplicationContext appCtx;
    private MealRestController controller;

    @Override
    public void init() {
        log.info("Init spring context");
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        super.destroy();
        log.info("Close spring context");
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        String dateStr = request.getParameter("fromDate");
        LocalDate fromDate = dateStr != null && !dateStr.isEmpty() ? LocalDate.parse(dateStr) : null;
        dateStr = request.getParameter("toDate");
        LocalDate toDate = dateStr != null && !dateStr.isEmpty() ? LocalDate.parse(dateStr) : null;
        String timeStr = request.getParameter("fromTime");
        LocalTime fromTime = timeStr != null && !timeStr.isEmpty() ? LocalTime.parse(timeStr) : null;
        timeStr = request.getParameter("toTime");
        LocalTime toTime = timeStr != null && !timeStr.isEmpty() ? LocalTime.parse(timeStr) : null;

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
        String urlStringDelete = String.format(
                "meals?fromDate=%s&toDate=%s&fromTime=%s&toTime=%s",
                fromDate == null ? "": fromDate,
                toDate == null ? "": toDate,
                fromTime == null ? "": fromTime,
                toTime == null ? "": toTime
        );
        response.sendRedirect(urlStringDelete);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String action = request.getParameter("action");

        String dateStr = request.getParameter("fromDate");
        LocalDate fromDate = dateStr != null && !dateStr.isEmpty() ? LocalDate.parse(dateStr) : null;
        dateStr = request.getParameter("toDate");
        LocalDate toDate = dateStr != null && !dateStr.isEmpty() ? LocalDate.parse(dateStr) : null;
        String timeStr = request.getParameter("fromTime");
        LocalTime fromTime = timeStr != null && !timeStr.isEmpty() ? LocalTime.parse(timeStr) : null;
        timeStr = request.getParameter("toTime");
        LocalTime toTime = timeStr != null && !timeStr.isEmpty() ? LocalTime.parse(timeStr) : null;

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                controller.delete(id);
                String urlStringDelete = String.format(
                        "meals?fromDate=%s&toDate=%s&fromTime=%s&toTime=%s",
                        fromDate == null ? "": fromDate,
                        toDate == null ? "": toDate,
                        fromTime == null ? "": fromTime,
                        toTime == null ? "": toTime
                );
                response.sendRedirect(urlStringDelete);
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                String urlStringUpdate = String.format(
                        "/mealForm.jsp?fromDate=%s&toDate=%s&fromTime=%s&toTime=%s",
                        fromDate == null ? "": fromDate,
                        toDate == null ? "": toDate,
                        fromTime == null ? "": fromTime,
                        toTime == null ? "": toTime
                );
                request.getRequestDispatcher(urlStringUpdate).forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                String urlString = String.format(
                        "/meals.jsp?fromDate=%s&toDate=%s&fromTime=%s&toTime=%s",
                        fromDate == null ? "": fromDate,
                        toDate == null ? "": toDate,
                        fromTime == null ? "": fromTime,
                        toTime == null ? "": toTime
                );
                request.setAttribute("meals", controller.filter(fromDate, toDate, fromTime, toTime));
                request.getRequestDispatcher(urlString).forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
