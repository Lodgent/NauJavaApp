package ru.Vladimir.NauJava;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Models.User;
import ru.Vladimir.NauJava.dao.FileRepository;
import ru.Vladimir.NauJava.dao.UserRepository;
import ru.Vladimir.NauJava.dao.FileTagRepository;
import ru.Vladimir.NauJava.Models.FileTag;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback
class CustomRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileTagRepository fileTagRepository;

    /**
     * Тест кастомного метода поиска файлов по пользователю и диапазону дат (HQL, Named, Criteria API)
     */
    @Test
    void testFindFilesByUserAndDateRange() {
        // Создаем пользователя
        User user = new User("testUser", "password");
        user = userRepository.save(user);

        // Создаем файлы с разными датами
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_MONTH, -5);
        Date startDate = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 10);
        Date endDate = cal.getTime();

        FileEntity file1 = new FileEntity();
        file1.setFileName("file1.txt");
        file1.setFileType("txt");
        file1.setFileSize(100L);
        file1.setOwner(user);
        file1.setCreationDate(now);
        file1 = fileRepository.save(file1);

        FileEntity file2 = new FileEntity();
        file2.setFileName("file2.txt");
        file2.setFileType("txt");
        file2.setFileSize(200L);
        file2.setOwner(user);
        file2.setCreationDate(now);
        file2 = fileRepository.save(file2);

        // Тестируем HQL запрос
        List<FileEntity> filesHQL = fileRepository.findFilesByUserAndDateRangeHQL(
                user.getUsername(), startDate, endDate);
        Assertions.assertEquals(2, filesHQL.size());

        // Тестируем именованный запрос
        List<FileEntity> filesNamed = fileRepository.findFilesByOwnerAndDateRangeNamed(
                user.getUsername(), startDate, endDate);
        Assertions.assertEquals(2, filesNamed.size());

        // Тестируем Criteria API
        List<FileEntity> filesCriteria = fileRepository.findFilesByUserAndDateRange(
                user.getUsername(), startDate, endDate);
        Assertions.assertEquals(2, filesCriteria.size());
    }

    /**
     * Тест кастомного метода поиска пользователя по имени (HQL, Named, Criteria API)
     */
    @Test
    void testFindUserByUsername() {
        // Создаем пользователя
        User user = new User("testUser", "password");
        user = userRepository.save(user);

        // Тестируем HQL запрос
        User foundHQL = userRepository.findByUsernameHQL("testUser").orElse(null);
        Assertions.assertNotNull(foundHQL);
        Assertions.assertEquals("testUser", foundHQL.getUsername());

        // Тестируем именованный запрос
        User foundNamed = userRepository.findByUsernameNamed("testUser").orElse(null);
        Assertions.assertNotNull(foundNamed);
        Assertions.assertEquals("testUser", foundNamed.getUsername());

        // Тестируем Criteria API
        User foundCriteria = userRepository.findByUsernameCriteria("testUser").orElse(null);
        Assertions.assertNotNull(foundCriteria);
        Assertions.assertEquals("testUser", foundCriteria.getUsername());
    }

    /**
     * Тест кастомного метода поиска тегов по имени (HQL, Named, Criteria API)
     */
    @Test
    void testFindFileTagByName() {
        // Создаем тег
        FileTag tag = new FileTag();
        tag.setTag("important");
        tag = fileTagRepository.save(tag);

        // Тестируем HQL запрос
        FileTag foundHQL = fileTagRepository.findByTagNameHQL("important").orElse(null);
        Assertions.assertNotNull(foundHQL);
        Assertions.assertEquals("important", foundHQL.getTag());

        // Тестируем именованный запрос
        FileTag foundNamed = fileTagRepository.findByTagNameNamed("important").orElse(null);
        Assertions.assertNotNull(foundNamed);
        Assertions.assertEquals("important", foundNamed.getTag());

        // Тестируем Criteria API
        FileTag foundCriteria = fileTagRepository.findByTagNameCriteria("important").orElse(null);
        Assertions.assertNotNull(foundCriteria);
        Assertions.assertEquals("important", foundCriteria.getTag());
    }
}