package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(authUserId(), id);
    }

    public void update(Meal meal, int id) {
        int userId = authUserId();
        log.info("update {} with id={} by user with id={}", meal, meal.getId(), userId);
        assureIdConsistent(meal, id);
        service.update(userId, meal);
    }

    public Meal create(Meal meal) {
        int userId = authUserId();
        log.info("create {} by user with id={}", meal, userId);
        checkNew(meal);
        return service.create(userId, meal);
    }

    public List<Meal> getAll() {
        int userId = authUserId();
        log.info("getAll by user with id={}", userId);
        return service.getAll(userId);
    }

    public List<MealTo> filter(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) {
        int userId = authUserId();
        log.info("filter by user with id={} from {}:{} to {}:{}", userId, fromDate, fromTime, toDate, toTime);
        return service.filter(userId, authUserCaloriesPerDay(), fromDate, toDate, fromTime, toTime);
    }

}