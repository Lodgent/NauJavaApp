package ru.Vladimir.NauJava.Config;

import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Services.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Vladimir.NauJava.Services.*;

import java.util.List;

@Component
public class FileCommandProcessor {

    private final FileService fileService;
    private final LinkService linkService;
    private final UserService userService;

    @Autowired
    public FileCommandProcessor(FileService fileService, LinkService linkService, UserService userService) {
        this.fileService = fileService;
        this.linkService = linkService;
        this.userService = userService;
    }

    public void processCommand(String input) {
        String[] cmd = input.trim().split(" ");

        switch (cmd[0].toLowerCase()) {
            case "upload" -> {
                if (cmd.length >= 3) {
                    String userId = cmd[1];
                    String filePath = cmd[2];

                    try {
                        fileService.uploadFile(userId, filePath);
                        System.out.println("Файл успешно загружен!");
                    } catch (Exception e) {
                        System.out.println("Ошибка при загрузке файла: " + e.getMessage());
                    }
                } else {
                    System.out.println("Использование: upload <userId> <filePath>");
                }
            }

            case "download" -> {
                if (cmd.length >= 2) {
                    String link = cmd[1];
                    try {
                        fileService.downloadFile(link);
                        System.out.println("Файл успешно скачан!");
                    } catch (Exception e) {
                        System.out.println("Ошибка при скачивании файла: " + e.getMessage());
                    }
                } else {
                    System.out.println("Использование: download <link>");
                }
            }

            case "generate" -> {
                if (cmd.length >= 3) {
                    String fileId = cmd[1];
                    int expiration = Integer.parseInt(cmd[2]);

                    String link = linkService.generateLink(fileId, expiration);
                    System.out.println("Сформирована ссылка: " + link);
                } else {
                    System.out.println("Использование: generate <fileId> <expiration>");
                }
            }

            case "list" -> {
                List<FileEntity> files = fileService.getAllFiles();
                if (files.isEmpty()) {
                    System.out.println("Нет доступных файлов");
                } else {
                    System.out.println("Список файлов:");
                    for (FileEntity file : files) {
                        System.out.println("- " + file.getId() + ": " + file.getFileName());
                    }
                }
            }

            case "delete" -> {
                if (cmd.length >= 2) {
                    try {
                        fileService.deleteFile(cmd[1]);
                        System.out.println("Файл удален!");
                    } catch (Exception e) {
                        System.out.println("Ошибка при удалении файла: " + e.getMessage());
                    }
                }
            }

            case "help" -> {
                System.out.println("Доступные команды:");
                System.out.println("upload <userId> <filePath> - загрузить файл");
                System.out.println("download <link> - скачать файл по ссылке");
                System.out.println("generate <fileId> <expiration> - создать ссылку на файл");
                System.out.println("list - список всех файлов");
                System.out.println("delete <fileId> - удалить файл");
                System.out.println("help - показать справку");
                System.out.println("exit - выход");
            }

            default -> System.out.println("Неизвестная команда. Введите 'help' для справки.");
        }
    }
}
