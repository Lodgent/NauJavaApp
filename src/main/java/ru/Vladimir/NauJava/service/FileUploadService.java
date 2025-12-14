package ru.Vladimir.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Models.User;
import ru.Vladimir.NauJava.dao.FileRepository;
import ru.Vladimir.NauJava.dao.UserRepository;
import ru.Vladimir.NauJava.Services.FileStorageService;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FileUploadService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Transactional
    public FileEntity uploadFile(MultipartFile file, String username) throws IOException {
        // Получаем или создаем пользователя
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> {
                    User newUser = new User(username, "default");
                    return userRepository.save(newUser);
                });

        // Создаем FileEntity
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileSize(file.getSize());
        fileEntity.setOwner(user);
        fileEntity.setCreationDate(new Date());

        // Определяем тип файла
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null && originalFilename.contains(".")) {
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            fileEntity.setFileType(extension);
        } else {
            fileEntity.setFileType("unknown");
        }

        // Сохраняем файл в БД
        fileEntity = fileRepository.save(fileEntity);

        // Сохраняем содержимое файла на диск
        fileStorageService.saveFile(fileEntity.getId(), file.getBytes());

        return fileEntity;
    }

    public List<FileEntity> getAllFiles() {
        return fileRepository.findAll();
    }

    public Optional<FileEntity> getFileById(String fileId) {
        return fileRepository.findById(fileId);
    }

    @Transactional
    public void deleteFile(String fileId) {
        Optional<FileEntity> fileOpt = fileRepository.findById(fileId);
        if (fileOpt.isPresent()) {
            FileEntity file = fileOpt.get();
            fileRepository.delete(file);
            fileStorageService.deleteFile(fileId);
        }
    }
}

