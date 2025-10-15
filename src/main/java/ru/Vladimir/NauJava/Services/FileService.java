package ru.Vladimir.NauJava.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Models.FileLink;
import ru.Vladimir.NauJava.Models.User;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.*;


@Service
public class FileService {
    private final Map<String, FileEntity> files = new HashMap<>();
    private final LinkService linkService;
    private final UserService userService;
    private final FileStorageService fileStorageService;

    @Autowired
    public FileService(@Lazy LinkService linkService,
                       @Lazy UserService userService,
                       @Lazy FileStorageService fileStorageService) {
        this.linkService = linkService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    public void uploadFile(String userId, String filePath) {
        // Проверка прав пользователя
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }

        // Чтение файла с диска
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

            FileEntity file = new FileEntity();
            file.setId(UUID.randomUUID().toString());
            file.setFileName(filePath);
            file.setOwnerId(userId);
            file.setContent(fileBytes);

            // Сохранение файла в хранилище
            fileStorageService.saveFile(file.getId(), fileBytes);

            files.put(file.getId(), file);
            user.getFiles().add(file.getId());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке файла", e);
        }
    }

    public void downloadFile(String link) {
        if (!linkService.isValidLink(link)) {
            throw new RuntimeException("Недействительная ссылка");
        }

        FileLink fileLink = linkService.getLink(link);
        FileEntity file = files.get(fileLink.getFileId());

        try {
            // Загрузка файла из хранилища
            byte[] fileBytes = fileStorageService.loadFile(file.getId());

            // Логика сохранения файла на диск
            Files.write(Paths.get("download/" + file.getFileName()), fileBytes);
            System.out.println("Файл " + file.getFileName() + " успешно скачан");
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при скачивании файла", e);
        }
    }

    public void deleteFile(String fileId) {
        if (!files.containsKey(fileId)) {
            throw new RuntimeException("Файл не найден");
        }

        FileEntity file = files.remove(fileId);

        // Удаление из хранилища
        fileStorageService.deleteFile(fileId);

        // Обновление списка файлов у владельца
        User owner = userService.getUserById(file.getOwnerId());
        owner.getFiles().remove(fileId);
    }

    public List<FileEntity> getAllFiles() {
        return new ArrayList<>(files.values());
    }

    public FileEntity getFileById(String fileId) {
        return files.get(fileId);
    }
}

