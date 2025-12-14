package ru.Vladimir.NauJava.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Models.User;
import ru.Vladimir.NauJava.service.FileUploadService;
import ru.Vladimir.NauJava.Services.FileStorageService;
import ru.Vladimir.NauJava.Services.UserService;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Controller
public class FileWebController {
    private static final Logger logger = Logger.getLogger(FileWebController.class.getName());

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/files";
    }

    @GetMapping("/files")
    public String listFiles(Model model, HttpSession session) {
        try {
            List<FileEntity> files = fileUploadService.getAllFiles();
            logger.info("Loaded " + files.size() + " files");
            
            if (!files.isEmpty()) {
                FileEntity firstFile = files.get(0);
                logger.info("First file: " + firstFile.getFileName() + ", owner: " + 
                    (firstFile.getOwner() != null ? firstFile.getOwner().getUsername() : "null") +
                    ", date: " + firstFile.getCreationDate());
            }
            model.addAttribute("files", files);
        } catch (Exception e) {
            logger.severe("Error loading files: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("files", java.util.Collections.emptyList());
            model.addAttribute("error", "Ошибка при загрузке файлов: " + e.getMessage());
        }
        
        String currentUsername = (String) session.getAttribute("username");
        model.addAttribute("currentUser", currentUsername);
        model.addAttribute("isLoggedIn", currentUsername != null);
        
        return "files";
    }

    @PostMapping("/files/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                           @RequestParam(value = "username", required = false) String username,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Файл не выбран");
                return "redirect:/files";
            }

            // Используем текущего пользователя из сессии, если он залогинен
            String currentUsername = (String) session.getAttribute("username");
            if (currentUsername == null) {
                currentUsername = username != null && !username.isEmpty() ? username : "guest";
            }

            FileEntity uploadedFile = fileUploadService.uploadFile(file, currentUsername);
            redirectAttributes.addFlashAttribute("success", "Файл " + uploadedFile.getFileName() + " успешно загружен");
        } catch (MaxUploadSizeExceededException e) {
            redirectAttributes.addFlashAttribute("error", "Файл слишком большой! Максимальный размер: 100MB");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при загрузке файла: " + e.getMessage());
        } catch (Exception e) {
            logger.warning("Error uploading file: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error", "Ошибка при загрузке файла: " + e.getMessage());
        }

        return "redirect:/files";
    }

    @GetMapping("/files/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        logger.info("Attempting to download file with ID: " + fileId);
        
        Optional<FileEntity> fileOpt = fileUploadService.getFileById(fileId);
        if (fileOpt.isEmpty()) {
            logger.warning("File not found in database: " + fileId);
            return ResponseEntity.notFound().build();
        }

        FileEntity fileEntity = fileOpt.get();
        logger.info("File found in database: " + fileEntity.getFileName() + " (ID: " + fileId + ")");
        
        try {
            // Проверяем, существует ли файл на диске
            if (!fileStorageService.fileExists(fileId)) {
                logger.warning("File not found on disk: " + fileId);
                return ResponseEntity.status(404)
                        .body(new ByteArrayResource("Файл не найден на диске".getBytes()));
            }

            logger.info("Loading file content from disk: " + fileId);
            byte[] fileContent = fileStorageService.loadFile(fileId);
            ByteArrayResource resource = new ByteArrayResource(fileContent);

            // Определяем правильный Content-Type на основе расширения файла
            MediaType mediaType = getMediaType(fileEntity.getFileType());
            logger.info("File loaded successfully. Size: " + fileContent.length + " bytes, Type: " + mediaType);

            // Используем RFC 5987 для правильной обработки имен файлов с русскими символами
            String fileName = fileEntity.getFileName();
            String encodedFileName = escapeFileName(fileName);
            
            // Формируем заголовок Content-Disposition с поддержкой UTF-8
            String contentDisposition = String.format(
                "attachment; filename=\"%s\"; filename*=UTF-8''%s",
                fileName.replace("\"", "\\\""), // Экранируем кавычки в ASCII имени
                encodedFileName // URL-encoded имя для UTF-8
            );
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .contentType(mediaType)
                    .contentLength(fileContent.length)
                    .body(resource);
        } catch (Exception e) {
            logger.severe("Error downloading file " + fileId + ": " + e.getMessage());
            e.printStackTrace(); // Логируем ошибку для отладки
            return ResponseEntity.status(500)
                    .body(new ByteArrayResource(("Ошибка при скачивании файла: " + e.getMessage()).getBytes()));
        }
    }

    private MediaType getMediaType(String fileType) {
        if (fileType == null) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        
        String lowerType = fileType.toLowerCase();
        return switch (lowerType) {
            case "png" -> MediaType.IMAGE_PNG;
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "gif" -> MediaType.IMAGE_GIF;
            case "pdf" -> MediaType.APPLICATION_PDF;
            case "txt" -> MediaType.TEXT_PLAIN;
            case "html" -> MediaType.TEXT_HTML;
            case "json" -> MediaType.APPLICATION_JSON;
            case "xml" -> MediaType.APPLICATION_XML;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    private String escapeFileName(String fileName) {
        if (fileName == null) {
            return "file";
        }
        try {
            // Кодируем имя файла для использования в заголовке Content-Disposition
            // Используем URLEncoder для правильной обработки русских и специальных символов
            return URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20"); // Заменяем + на %20 для пробелов
        } catch (Exception e) {
            // Если не удалось закодировать, возвращаем исходное имя с экранированием
            return fileName.replace("\"", "\\\"")
                          .replace("\n", "")
                          .replace("\r", "");
        }
    }

    @PostMapping("/files/delete/{fileId}")
    public String deleteFile(@PathVariable String fileId, RedirectAttributes redirectAttributes) {
        try {
            fileUploadService.deleteFile(fileId);
            redirectAttributes.addFlashAttribute("success", "Файл успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении файла: " + e.getMessage());
        }
        return "redirect:/files";
    }

    @PostMapping("/register")
    public String register(@RequestParam("username") String username,
                          @RequestParam("password") String password,
                          RedirectAttributes redirectAttributes) {
        try {
            if (username == null || username.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Имя пользователя не может быть пустым");
                return "redirect:/files";
            }
            if (password == null || password.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Пароль не может быть пустым");
                return "redirect:/files";
            }
            
            userService.registerUser(username.trim(), password);
            redirectAttributes.addFlashAttribute("success", "Пользователь " + username + " успешно зарегистрирован! Теперь вы можете войти.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка регистрации: " + e.getMessage());
        }
        return "redirect:/files";
    }

    @PostMapping("/login")
    public String login(@RequestParam("username") String username,
                       @RequestParam("password") String password,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        try {
            if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Введите имя пользователя и пароль");
                return "redirect:/files";
            }
            
            if (userService.authenticateUser(username.trim(), password)) {
                session.setAttribute("username", username.trim());
                User user = userService.getUserByUsername(username.trim());
                if (user != null) {
                    session.setAttribute("userId", user.getId());
                }
                redirectAttributes.addFlashAttribute("success", "Вход выполнен успешно! Добро пожаловать, " + username + "!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Неверный логин или пароль");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при входе: " + e.getMessage());
        }
        return "redirect:/files";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        try {
            String username = (String) session.getAttribute("username");
            if (session != null) {
                session.invalidate();
            }
            if (username != null) {
                redirectAttributes.addFlashAttribute("success", "Вы успешно вышли из системы");
            }
        } catch (Exception e) {
            logger.warning("Error during logout: " + e.getMessage());
            // Продолжаем выполнение даже при ошибке
        }
        return "redirect:/files";
    }
}


