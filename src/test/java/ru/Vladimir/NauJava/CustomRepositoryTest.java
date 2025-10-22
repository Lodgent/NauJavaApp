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

@SpringBootTest
@Rollback
class CustomRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    /**
     * Тест кастомного метода поиска файлов по владельцу
     */
    @Test
    void testFindFilesByOwner() {
        // Создаем пользователя
        User user = new User();
        user.setUsername("testUser");
        user = userRepository.save(user);

        // Создаем файлы
        FileEntity file1 = new FileEntity();
        file1.setFileName("file1.txt");
        file1.setOwner(user);

        FileEntity file2 = new FileEntity();
        file2.setFileName("file2.txt");
        file2.setOwner(user);

        fileRepository.save(file1);
        fileRepository.save(file2);

        // Ищем файлы по владельцу
        List<FileEntity> files = fileRepository.findFilesByOwner(user.getId());

        // Проверки
        Assertions.assertEquals(2, files.size());
        Assertions.assertTrue(files.contains(file1));
        Assertions.assertTrue(files.contains(file2));
    }

    /**
     * Тест кастомного метода подсчета файлов по типу
     */
    @Test
    void testCountFilesByType() {
        // Создаем файлы разных типов
        FileEntity file1 = new FileEntity();
        file1.setFileName("image.jpg");
        file1.setFileType("image");

        FileEntity file2 = new FileEntity();
        file2.setFileName("doc.pdf");
        file2.setFileType("document");

        FileEntity file3 = new FileEntity();
        file3.setFileName("music.mp3");
        file3.setFileType("audio");

        fileRepository.save(file1);
        fileRepository.save(file2);
        fileRepository.save(file3);

        // Проверяем подсчет
        Assertions.assertEquals(1, fileRepository.countFilesByType("image"));
        Assertions.assertEquals(1, fileRepository.countFilesByType("document"));
        Assertions.assertEquals(1, fileRepository.countFilesByType("audio"));
    }
}