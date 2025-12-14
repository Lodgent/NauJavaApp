package ru.Vladimir.NauJava.dao;


import ru.Vladimir.NauJava.Models.User;
import ru.Vladimir.NauJava.dao.custom.UserCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends JpaRepository<User, String>, UserCustomRepository {
    // Пример метода поиска с использованием Query Lookup Strategies
    Optional<User> findByUsername(String username);

    // HQL запрос
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findByUsernameHQL(@Param("username") String username);

    // Именованный запрос
    @Query(name = "User.findByUsernameAndStatus")
    Optional<User> findByUsernameNamed(@Param("username") String username);

    // HQL запрос для поиска по части имени
    @Query("SELECT u FROM User u WHERE u.username LIKE :pattern")
    Optional<User> findByUsernameContainingHQL(@Param("pattern") String pattern);

    // Именованный запрос для поиска по части имени
    @Query(name = "User.findByUsernameContaining")
    Optional<User> findByUsernameContainingNamed(@Param("pattern") String pattern);
}