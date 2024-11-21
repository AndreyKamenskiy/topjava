package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.repository.JpaUtil;

public abstract class AbstractUserServiceJpaAndDataJpaTest extends AbstractUserServiceTest {

    @Autowired
    JpaUtil jpaUtil;

    @Before
    public void setup() {
        jpaUtil.clear2ndLevelHibernateCache();
    }

}
