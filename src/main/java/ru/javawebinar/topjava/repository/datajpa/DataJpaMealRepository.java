package ru.javawebinar.topjava.repository.datajpa;

import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Logger log = getLogger(DataJpaMealRepository.class);

    private final CrudMealRepository crudMealRepository;
    private final CrudUserRepository crudUserRepository;


    public DataJpaMealRepository(CrudMealRepository crudMealRepository, CrudUserRepository crudUserRepository) {
        this.crudMealRepository = crudMealRepository;
        this.crudUserRepository = crudUserRepository;
    }

    // Transactional annotation reduce db queries by 1
    @Override
    @Transactional()
    public Meal save(Meal meal, int userId) {
        User user = crudUserRepository.getReferenceById(userId);
        meal.setUser(user);
        log.trace("after set user");
        if (meal.isNew()) {
            return crudMealRepository.save(meal);
        } else {
            Meal exists = crudMealRepository
                    .findById(meal.getId()).orElse(null);
            log.trace("after findById");
            if (exists != null && exists.getUser().getId() == userId) {
                return crudMealRepository.save(meal);
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudMealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudMealRepository
                .findById(id)
                .filter(meal -> meal.getUser().getId() == userId)
                .orElse(null);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.findAllByUserOrderByDateTimeDesc(crudUserRepository.getReferenceById(userId));
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudMealRepository.getBetween(userId, startDateTime, endDateTime);
    }
}
