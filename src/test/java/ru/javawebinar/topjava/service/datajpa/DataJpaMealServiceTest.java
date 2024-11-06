package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.service.MealServiceTest;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "datajpa", resolver = ActiveDbProfileResolver.class)
public class DataJpaMealServiceTest extends MealServiceTest {

    @Test
    @Override
    public void delete() {
        super.delete();
    }

    @Test
    @Override
    public void deleteNotFound() {
        super.deleteNotFound();
    }

    @Test
    @Override
    public void deleteNotOwn() {
        super.deleteNotOwn();
    }

    @Test
    @Override
    public void create() {
        super.create();
    }

    @Test
    @Override
    public void duplicateDateTimeCreate() {
        super.duplicateDateTimeCreate();
    }

    @Test
    @Override
    public void get() {
        super.get();
    }

    @Test
    @Override
    public void getNotFound() {
        super.getNotFound();
    }

    @Test
    @Override
    public void getNotOwn() {
        super.getNotOwn();
    }

    @Test
    @Override
    public void update() {
        super.update();
    }

    @Test
    @Override
    public void updateNotOwn() {
        super.updateNotOwn();
    }

    @Test
    @Override
    public void getAll() {
        super.getAll();
    }

    @Test
    @Override
    public void getBetweenInclusive() {
        super.getBetweenInclusive();
    }

    @Test
    @Override
    public void getBetweenWithNullDates() {
        super.getBetweenWithNullDates();
    }
}