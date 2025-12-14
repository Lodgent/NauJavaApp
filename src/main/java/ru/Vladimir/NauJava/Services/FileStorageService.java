package ru.Vladimir.NauJava.Services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class FileStorageService {
    private final Path storagePath;

    @Autowired
    public FileStorageService(Environment env) {
        storagePath = Paths.get("storage").toAbsolutePath().normalize();

        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания директории", e);
        }
    }

    public void saveFile(String fileId, byte[] content) {
        try {
            Path filePath = storagePath.resolve(fileId);
            Files.write(filePath, content);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения файла", e);
        }
    }

    public byte[] loadFile(String fileId) {
        try {
            Path filePath = storagePath.resolve(fileId);
            if (!Files.exists(filePath)) {
                throw new RuntimeException("Файл не найден: " + fileId);
            }
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки файла", e);
        }
    }

    public void deleteFile(String fileId) {
        try {
            Path filePath = storagePath.resolve(fileId);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка удаления файла", e);
        }
    }

    public boolean fileExists(String fileId) {
        return Files.exists(storagePath.resolve(fileId));
    }
}
