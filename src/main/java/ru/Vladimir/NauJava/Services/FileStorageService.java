package ru.Vladimir.NauJava.Services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.logging.Logger;


@Service
public class FileStorageService {
    private static final Logger logger = Logger.getLogger(FileStorageService.class.getName());
    private final Path storagePath;

    @Autowired
    public FileStorageService(Environment env) {
        storagePath = Paths.get("storage").toAbsolutePath().normalize();
        logger.info("Storage path: " + storagePath.toString());

        try {
            Files.createDirectories(storagePath);
            logger.info("Storage directory created or already exists: " + storagePath);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания директории", e);
        }
    }

    public void saveFile(String fileId, byte[] content) {
        try {
            Path filePath = storagePath.resolve(fileId);
            Files.write(filePath, content);
            logger.info("File saved: " + filePath.toString() + " (size: " + content.length + " bytes)");
        } catch (IOException e) {
            logger.severe("Ошибка сохранения файла " + fileId + ": " + e.getMessage());
            throw new RuntimeException("Ошибка сохранения файла", e);
        }
    }

    public byte[] loadFile(String fileId) {
        try {
            Path filePath = storagePath.resolve(fileId);
            logger.info("Attempting to load file: " + filePath.toString());
            if (!Files.exists(filePath)) {
                logger.warning("File not found: " + filePath.toString());
                throw new RuntimeException("Файл не найден: " + fileId + " (path: " + filePath + ")");
            }
            byte[] content = Files.readAllBytes(filePath);
            logger.info("File loaded successfully: " + filePath.toString() + " (size: " + content.length + " bytes)");
            return content;
        } catch (IOException e) {
            logger.severe("Ошибка загрузки файла " + fileId + ": " + e.getMessage());
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
