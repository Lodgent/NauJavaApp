package ru.Vladimir.NauJava.Services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// Дополнительный сервис для работы с файловым хранилищем
@Service
public class FileStorageService {
    private final String storagePath = "storage/";

    public void saveFile(String fileId, byte[] content) {
        try {
            Files.write(Paths.get(storagePath + fileId), content);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения файла", e);
        }
    }

    public byte[] loadFile(String fileId) {
        try {
            return Files.readAllBytes(Paths.get(storagePath + fileId));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла", e);
        }
    }

    public void deleteFile(String fileId) {
        try {
            Files.delete(Paths.get(storagePath + fileId));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка удаления файла", e);
        }
    }
}
