package ru.Vladimir.NauJava.dao;


import ru.Vladimir.NauJava.Models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    // Пример метода поиска с использованием Query Lookup Strategies
    User findByUsernameAndStatus(String username, String status);

    // Поиск по части имени пользователя
    User findByUsernameContaining(String username);
}