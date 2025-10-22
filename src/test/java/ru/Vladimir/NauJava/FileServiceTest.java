package ru.Vladimir.NauJava;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.service.FileTagService;

import java.util.List;

@SpringBootTest
class FileServiceTest {

    @Autowired
    private FileTagService fileTagService;

    @Test
    void testCreateFileWithTags() {
        // Создание файла
        FileEntity file = new FileEntity();
        file.setFileName("testFile.txt");

        // Теги для файла
        List<String> tagNames = List.of("tag1", "tag2", "tag3");

        // Создание файла с тегами
        FileEntity createdFile = fileTagService.createFileWithTags(file, tagNames);

        // Проверки
        Assertions.assertNotNull(createdFile);
        Assertions.assertNotNull(createdFile.getId());
        Assertions.assertEquals(tagNames.size(), createdFile.getTags().size());
    }

    @Test
    @Rollback
    void testCreateFileWithEmptyTags() {
        // Создание файла
        FileEntity file = new FileEntity();
        file.setFileName("testFile.txt");

        // Пустой список тегов
        List<String> tagNames = List.of();

        // Создание файла без тегов
        FileEntity createdFile = fileTagService.createFileWithTags(file, tagNames);

        // Проверки
        Assertions.assertNotNull(createdFile);
        Assertions.assertTrue(createdFile.getTags().isEmpty());
    }
}