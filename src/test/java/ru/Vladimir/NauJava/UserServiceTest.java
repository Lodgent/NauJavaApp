package ru.Vladimir.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.Vladimir.NauJava.Models.User;
import ru.Vladimir.NauJava.service.UserFileService;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserFileService userFileService;

    @Test
    void testCreateUserWithFile() {
        // Данные для теста
        String username = "testUser";
        String password = "testPass";
        String fileName = "testFile.txt";

        // Создание пользователя с файлом
        User user = userFileService.createUserWithFile(username, password, fileName);

        // Проверки
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getId());
        Assertions.assertEquals(username, user.getUsername());
        Assertions.assertEquals(1, user.getFiles().size());
        Assertions.assertEquals(fileName, user.getFiles().iterator().next().getFileName());
    }

    @Test
    @Rollback
    void testCreateUserWithInvalidData() {
        // Невалидные данные
        String username = null;
        String password = "testPass";
        String fileName = "testFile.txt";

        // Проверка на исключение
        Assertions.assertThrows(RuntimeException.class, () -> {
            userFileService.createUserWithFile(username, password, fileName);
        });
    }
}