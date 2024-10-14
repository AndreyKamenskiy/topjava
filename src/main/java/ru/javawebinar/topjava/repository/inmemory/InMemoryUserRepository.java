package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.exception.RepositoryException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);

    private final Map<Integer, User> idToUser = new ConcurrentHashMap<>();

    // email must be uniq. Tested in demo
    private final Map<String, User> emailToUser = new ConcurrentHashMap<>();
    private final Map<Integer, String> idToEmail = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        if (idToUser.containsKey(id)) {
            String email = idToEmail.get(id);
            boolean error = emailToUser.remove(email) == null ||
                    idToEmail.remove(email) == null ||
                    idToUser.remove(id) == null;
            if (error) {
                throw new RepositoryException("User deleting error");
            }
        }
        return false;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            int id = counter.incrementAndGet();
            user.setId(id);
            String email = user.getEmail();
            if (emailToUser.containsKey(email)) {
                throw new IllegalArgumentException(
                        String.format("Saving user error. Email %s is already exists", email));
            }
            idToUser.put(user.getId(), user);
            emailToUser.put(email, user);
            idToEmail.put(id, email);
            return user;
        }
        int id = user.getId();
        if (idToUser.containsKey(id)) {
            String oldEmail = idToEmail.get(id);
            if (oldEmail == null || oldEmail.isEmpty()) {
                throw new RepositoryException("Saving user error. Unable get old email value or value is empty");
            }
            String newEmail = user.getEmail();
            if (!oldEmail.equals(newEmail)) {
                idToEmail.put(id, newEmail);
                emailToUser.remove(oldEmail);
                emailToUser.put(newEmail, user);
            }
            return idToUser.put(id, user);
        }
        return null;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return idToUser.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return idToUser.values()
                .stream()
                .sorted(Comparator.comparing(User::getName))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        // to simplify solution we returning real user bean. Email must not be changed.
        return emailToUser.getOrDefault(email, null);
    }
}
