package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.service.UserServiceTest;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = "datajpa", resolver = ActiveDbProfileResolver.class)
public class DataJpaUserServiceTest extends UserServiceTest {

    @Test
    @Override
    public void create() {
        super.create();
    }

    @Test
    @Override
    public void duplicateMailCreate() {
        super.duplicateMailCreate();
    }

    @Test
    @Override
    public void delete() {
        super.delete();
    }

    @Test
    @Override
    public void deletedNotFound() {
        super.deletedNotFound();
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
    public void getByEmail() {
        super.getByEmail();
    }

    @Test
    @Override
    public void update() {
        super.update();
    }

    @Test
    @Override
    public void getAll() {
        super.getAll();
    }
}