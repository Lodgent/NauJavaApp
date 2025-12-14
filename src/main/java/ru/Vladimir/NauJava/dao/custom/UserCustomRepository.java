package ru.Vladimir.NauJava.dao.custom;

import ru.Vladimir.NauJava.Models.User;
import java.util.Optional;

public interface UserCustomRepository {
    // Criteria API методы (аналоги именованных и HQL запросов)
    Optional<User> findByUsernameCriteria(String username);
    Optional<User> findByUsernameContainingCriteria(String pattern);
}

