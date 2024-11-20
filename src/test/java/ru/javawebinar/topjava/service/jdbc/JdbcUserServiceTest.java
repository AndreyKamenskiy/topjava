package ru.javawebinar.topjava.service.jdbc;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import java.util.List;

import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(JDBC)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JdbcUserServiceTest extends AbstractUserServiceTest {

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