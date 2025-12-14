package ru.Vladimir.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Models.User;
import ru.Vladimir.NauJava.dao.FileRepository;
import ru.Vladimir.NauJava.dao.UserRepository;
import ru.Vladimir.NauJava.service.UserFileService;

@SpringBootTest
@Rollback
class UserFileServiceTest {

    @Autowired
    private UserFileService userFileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    /**
     * Тест создания пользователя с файлом
     */
    @Test
    void testCreateUserWithFile() {
        // Валидные входные данные
        String username = "testUser";
        String password = "testPass";
        String fileName = "testFile.txt";

        // Создаем пользователя с файлом
        User createdUser = userFileService.createUserWithFile(username, password, fileName);

        // Проверяем создание пользователя
        Assertions.assertNotNull(createdUser);
        Assertions.assertNotNull(createdUser.getId());
        Assertions.assertEquals(username, createdUser.getUsername());
        Assertions.assertEquals(password, createdUser.getPassword());

        // Проверяем создание файла
        Assertions.assertEquals(1, createdUser.getFiles().size());
        FileEntity file = createdUser.getFiles().iterator().next();
        Assertions.assertNotNull(file.getId());
        Assertions.assertEquals(fileName, file.getFileName());
        Assertions.assertEquals(createdUser, file.getOwner());
    }

    /**
     * Тест обновления пользователя и файла
     */
    @Test
    void testUpdateUserAndFile() {
        // Создаем тестового пользователя
        User user = new User("oldUser", "oldPass");
        user = userRepository.save(user);

        // Создаем тестовый файл
        FileEntity file = new FileEntity();
        file.setFileName("oldFile.txt");
        file.setOwner(user);
        file = fileRepository.save(file);
        user.addFile(file);
        userRepository.save(user);

        // Новые данные для обновления
        String newUsername = "updatedUser";
        String newFileName = "updatedFile.txt";

        // Обновляем данные
        User updatedUser = userFileService.updateUserAndFile(user.getId(), newUsername, newFileName);

        // Проверяем обновление пользователя
        Assertions.assertEquals(newUsername, updatedUser.getUsername());

        // Проверяем обновление файла
        Assertions.assertEquals(1, updatedUser.getFiles().size());
        FileEntity updatedFile = updatedUser.getFiles().iterator().next();
        Assertions.assertEquals(newFileName, updatedFile.getFileName());
    }

    /**
     * Тест обработки ошибок при создании
     */
    @Test
    void testCreateUserWithInvalidData() {
        // Невалидные данные
        String invalidUsername = null;
        String password = "testPass";
        String fileName = "testFile.txt";

        // Проверяем исключение
        Assertions.assertThrows(RuntimeException.class, () -> {
            userFileService.createUserWithFile(invalidUsername, password, fileName);
        });
    }

    /**
     * Тест обработки ошибок при обновлении
     */
    @Test
    void testUpdateUserNotFound() {
        // Невалидный ID пользователя
        String nonExistentId = "nonExistentId";
        String newUsername = "newUser";
        String newFileName = "newFile.txt";

        // Проверяем исключение
        Assertions.assertThrows(RuntimeException.class, () -> {
            userFileService.updateUserAndFile(nonExistentId, newUsername, newFileName);
        });
    }
}