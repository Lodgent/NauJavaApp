package ru.Vladimir.NauJava.Services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class FileStorageService {
    private final String storagePath;

    @Autowired
    public FileStorageService(Environment env) {

        storagePath = Paths.get("download/").toAbsolutePath().normalize().toString();

        try {
            Files.createDirectories(Paths.get(storagePath));
        } catch (IOException e) {
            throw new RuntimeException("Ошибка создания директории", e);
        }
    }

    public void saveFile(String fileId, byte[] content) {
        try {

            Files.write(Paths.get(fileId), content);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения файла", e);
        }
    }

    public byte[] loadFile(String fileId) {
        try {
            return Files.readAllBytes(Paths.get(fileId));
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
