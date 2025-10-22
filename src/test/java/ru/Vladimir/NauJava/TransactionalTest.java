package ru.Vladimir.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Models.User;
import ru.Vladimir.NauJava.service.UserFileService;
import ru.Vladimir.NauJava.dao.UserRepository;

@SpringBootTest
@Transactional
class TransactionalTest {

    @Autowired
    private UserFileService userFileService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Положительный тест транзакционной операции
     */
    @Test
    @Rollback
    void testSuccessfulTransaction() {
        // Данные для успешного теста
        String username = "testUser";
        String password = "testPass";
        String fileName = "testFile.txt";

        // Выполняем транзакционную операцию
        User user = userFileService.createUserWithFile(username, password, fileName);

        // Проверки
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(username, user.getUsername());
        Assertions.assertEquals(1, user.getFiles().size());

        // Проверяем, что данные сохранились в БД
        User savedUser = userRepository.findById(user.getId()).orElse(null);
        Assertions.assertNotNull(savedUser);
    }

    /**
     * Отрицательный тест транзакционной операции (откат)
     */
    @Test
    @Rollback
    void testFailedTransaction() {
        // Невалидные данные (пустое имя пользователя)
        String username = "";
        String password = "testPass";
        String fileName = "testFile.txt";

        // Проверяем, что операция вызовет исключение
        Assertions.assertThrows(RuntimeException.class, () -> {
            userFileService.createUserWithFile(username, password, fileName);
        });

        // Проверяем, что данные не сохранились в БД
        User user = userRepository.findByUsername(username);
        Assertions.assertNull(user);
    }



}