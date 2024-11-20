package ru.javawebinar.topjava.service;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import ru.javawebinar.topjava.repository.JpaUtil;

import java.util.Optional;

public abstract class AbstractUserServiceJpaAndDataJpaTest extends AbstractUserServiceTest {

    @Autowired
    JpaUtil jpaUtil;

    @Before
    public void setup() {
        Optional.ofNullable(cacheManager.getCache("users")).ifPresent(Cache::clear);
        jpaUtil.clear2ndLevelHibernateCache();
    }

}
