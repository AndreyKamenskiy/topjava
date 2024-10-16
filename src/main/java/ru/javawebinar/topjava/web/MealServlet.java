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

import static org.springframework.util.StringUtils.hasText;

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
        log.info("Close spring context");
        appCtx.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        DateTimeFilter filter = new DateTimeFilter(request);
        String id = request.getParameter("id");
        Integer mealId = hasText(id) ? Integer.parseInt(id) : null;
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
        response.sendRedirect(filter.getUrl("meals"));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        String action = request.getParameter("action");
        DateTimeFilter filter = new DateTimeFilter(request);
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete id={}", id);
                controller.delete(id);
                response.sendRedirect(filter.getUrl("meals"));
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher(filter.getUrl("/mealForm.jsp")).forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", controller.filter(filter.getFromDate(), filter.getToDate(),
                        filter.getFromTime(), filter.getToTime()));
                request.getRequestDispatcher(filter.getUrl("/meals.jsp")).forward(request, response);
                break;
        }
    }

    private static class DateTimeFilter {

        private final LocalDate fromDate;
        private final LocalDate toDate;
        private final LocalTime fromTime;
        private final LocalTime toTime;

        public DateTimeFilter(HttpServletRequest request) {
            String dateStr = request.getParameter("fromDate");
            fromDate = hasText(dateStr) ? LocalDate.parse(dateStr) : null;
            dateStr = request.getParameter("toDate");
            toDate = hasText(dateStr) ? LocalDate.parse(dateStr) : null;
            String timeStr = request.getParameter("fromTime");
            fromTime = hasText(timeStr) ? LocalTime.parse(timeStr) : null;
            timeStr = request.getParameter("toTime");
            toTime = hasText(timeStr) ? LocalTime.parse(timeStr) : null;
        }

        public String getUrl(String url) {
            return String.format(
                    "%s?fromDate=%s&toDate=%s&fromTime=%s&toTime=%s",
                    url,
                    fromDate == null ? "" : fromDate,
                    toDate == null ? "" : toDate,
                    fromTime == null ? "" : fromTime,
                    toTime == null ? "" : toTime
            );
        }

        public LocalDate getFromDate() {
            return fromDate;
        }

        public LocalDate getToDate() {
            return toDate;
        }

        public LocalTime getFromTime() {
            return fromTime;
        }

        public LocalTime getToTime() {
            return toTime;
        }

    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
