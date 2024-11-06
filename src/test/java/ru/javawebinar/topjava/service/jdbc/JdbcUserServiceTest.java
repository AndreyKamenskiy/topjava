package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.service.UserServiceTest;

@ActiveProfiles(profiles = Profiles.JDBC, resolver = ActiveDbProfileResolver.class)
public class JdbcUserServiceTest extends UserServiceTest {

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void duplicateMailCreate() {
        super.duplicateMailCreate();
    }

    @Override
    public void delete() {
        super.delete();
    }

    @Override
    public void deletedNotFound() {
        super.deletedNotFound();
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
    public void getByEmail() {
        super.getByEmail();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void getAll() {
        super.getAll();
    }
}