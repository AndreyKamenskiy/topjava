package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management (ARM)
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email0@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName1", "email1@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName2", "email2@mail.ru", "password", Role.ADMIN));
            adminUserController.create(new User(null, "userName", "email3@mail.ru", "password", Role.ADMIN));
            System.out.println(adminUserController.getAll()
                    .stream()
                    .map(user -> user.getName() + ":" + user.getEmail())
                    .collect(Collectors.joining(",\n"))
            );

            MealRestController mealRestController = appCtx.getBean(MealRestController.class);
            mealRestController.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
            mealRestController.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 1), "Завтрак", 500));
            mealRestController.create(new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 2), "Завтрак", 500));
            System.out.println("Meal repository size for current user " + mealRestController.getAll().size());
        }
    }
}
