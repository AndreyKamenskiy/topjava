package ru.javawebinar.topjava.service.jdbc;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.List;
import java.util.Optional;

import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(JDBC)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JdbcUserServiceTest extends AbstractUserServiceTest {

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setup() {
        Optional.ofNullable(cacheManager.getCache("users")).ifPresent(Cache::clear);
    }

    @Test
    public void dirtyCacheFirstPart() {
        User updated = getUpdated();
        service.update(updated);
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, guest, updated);
    }

    @Test
    public void dirtyCacheSecondPart() {
        List<User> all = service.getAll();
        USER_MATCHER.assertMatch(all, admin, guest, user);
    }

}