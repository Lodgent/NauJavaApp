package ru.Vladimir.NauJava.Services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Models.FileLink;
import ru.Vladimir.NauJava.Models.User;

import java.nio.file.Files;
import java.nio.file.Path;
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

        User user = userService.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("Пользователь не найден");
        }
        if (!Files.exists(Paths.get(filePath))) {
            throw new RuntimeException("Файл не найден по указанному пути");
        }

        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(filePath));

            FileEntity file = new FileEntity();
            file.setId(UUID.randomUUID().toString());
            file.setFileName(filePath.substring(filePath.lastIndexOf("\\") + 1));
            file.setOwner(user);
            file.setFileSize(fileBytes.length);
            String fileName = file.getFileName();
            if (fileName.contains(".")) {
                file.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));
            } else {
                file.setFileType("unknown");
            }

            fileStorageService.saveFile(file.getId(), fileBytes);

            files.put(file.getId(), file);

            user.addFile(file);
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
            byte[] fileBytes = fileStorageService.loadFile(file.getId());
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

        fileStorageService.deleteFile(fileId);

        User owner = file.getOwner();
        if (owner != null) {
            owner.removeFile(file);
        }
    }

    public List<FileEntity> getAllFiles() {
        return new ArrayList<>(files.values());
    }

    public FileEntity getFileById(String fileId) {
        return files.get(fileId);
    }
}

