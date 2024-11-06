package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(profiles = Profiles.JDBC, resolver = ActiveDbProfileResolver.class)
public class JdbcMealServiceTest extends MealServiceTest {

    @Override
    public void delete() {
        super.delete();
    }

    @Override
    public void deleteNotFound() {
        super.deleteNotFound();
    }

    @Override
    public void deleteNotOwn() {
        super.deleteNotOwn();
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void duplicateDateTimeCreate() {
        super.duplicateDateTimeCreate();
    }

    @Override
    public void get() {
        super.get();
    }

    @Override
    public void getNotFound() {
        super.getNotFound();
    }

    @Override
    public void getNotOwn() {
        super.getNotOwn();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void updateNotOwn() {
        super.updateNotOwn();
    }

    @Override
    public void getAll() {
        super.getAll();
    }

    @Override
    public void getBetweenInclusive() {
        super.getBetweenInclusive();
    }

    @Override
    public void getBetweenWithNullDates() {
        super.getBetweenWithNullDates();
    }
}