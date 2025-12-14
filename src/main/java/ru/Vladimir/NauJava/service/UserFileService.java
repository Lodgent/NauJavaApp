package ru.Vladimir.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Models.User;
import ru.Vladimir.NauJava.dao.FileRepository;
import ru.Vladimir.NauJava.dao.UserRepository;

@Service
public class UserFileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    /**
     * Метод для создания пользователя с начальным файлом
     * @param username имя пользователя
     * @param password пароль пользователя
     * @param fileName имя создаваемого файла
     * @return созданный пользователь с файлом
     */
    @Transactional
    public User createUserWithFile(String username, String password, String fileName) {
        // Создаем пользователя
        User user = new User(username, password);
        user = userRepository.save(user);

        // Создаем файл
        FileEntity file = new FileEntity();
        file.setFileName(fileName);
        file.setOwner(user);
        file.setFileSize(0L);
        // Определяем тип файла из расширения
        if (fileName.contains(".")) {
            file.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));
        } else {
            file.setFileType("unknown");
        }
        file = fileRepository.save(file);

        // Добавляем файл к пользователю
        user.addFile(file);

        return userRepository.save(user);
    }

    /**
     * Метод для обновления информации о пользователе и его файле
     * @param userId ID пользователя
     * @param newUsername новое имя пользователя
     * @param newFileName новое имя файла
     * @return обновленный пользователь
     */
    @Transactional
    public User updateUserAndFile(String userId, String newUsername, String newFileName) {
        // Получаем пользователя
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("Пользователь не найден"));

        // Обновляем информацию о пользователе
        user.setUsername(newUsername);

        // Обновляем информацию о файле
        if (!user.getFiles().isEmpty()) {
            FileEntity file = user.getFiles().iterator().next();
            file.setFileName(newFileName);
            fileRepository.save(file);
        }

        return userRepository.save(user);
    }
}