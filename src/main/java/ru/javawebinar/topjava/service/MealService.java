package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenClosed;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;
    private final UserService userService;

    public MealService(MealRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public Meal create(int userId, Meal meal) {
        return repository.save(checkUserId(userId), meal);
    }

    public Meal update(int userId, Meal meal) {
        return checkNotFoundWithId(repository.save(checkUserId(userId), meal), meal.getId());
    }

    public void delete(int userId, int id) {
        checkNotFoundWithId(repository.delete(checkUserId(userId), id), id);
    }

    public Meal get(int userId, int id) {
        return checkNotFoundWithId(repository.get(checkUserId(userId), id), id);
    }

    public List<Meal> getAll(int userId) {
        return new ArrayList<>(repository.getAll(checkUserId(userId)));
    }

    public List<Meal> filter(int userId, LocalDate fromDate, LocalDate toDate) {
        return getAll(userId).stream()
                .filter(meal -> isBetweenClosed(meal.getDate(), fromDate, toDate))
                .collect(Collectors.toList());
    }

    public List<MealTo> filter(int userId, int caloriesPerDay, LocalDate fromDate, LocalDate toDate,
                               LocalTime fromTime, LocalTime toTime) {
        return getFilteredTos(filter(userId, fromDate, toDate), caloriesPerDay, fromTime, toTime);
    }

    private int checkUserId(int userId) {
        return checkNotFoundWithId(userService.get(userId), userId).getId();
    }

}