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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Models.User;
import ru.Vladimir.NauJava.service.FileUploadService;
import ru.Vladimir.NauJava.Services.FileStorageService;
import ru.Vladimir.NauJava.Services.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class FileWebController {

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
        List<FileEntity> files = fileUploadService.getAllFiles();
        model.addAttribute("files", files);
        
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
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Файл не выбран");
            return "redirect:/files";
        }

        // Используем текущего пользователя из сессии, если он залогинен
        String currentUsername = (String) session.getAttribute("username");
        if (currentUsername == null) {
            currentUsername = username != null && !username.isEmpty() ? username : "guest";
        }

        try {
            FileEntity uploadedFile = fileUploadService.uploadFile(file, currentUsername);
            redirectAttributes.addFlashAttribute("success", "Файл " + uploadedFile.getFileName() + " успешно загружен");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при загрузке файла: " + e.getMessage());
        }

        return "redirect:/files";
    }

    @GetMapping("/files/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        Optional<FileEntity> fileOpt = fileUploadService.getFileById(fileId);
        if (fileOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FileEntity fileEntity = fileOpt.get();
        try {
            byte[] fileContent = fileStorageService.loadFile(fileId);
            ByteArrayResource resource = new ByteArrayResource(fileContent);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(fileContent.length)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
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
        String username = (String) session.getAttribute("username");
        session.invalidate();
        if (username != null) {
            redirectAttributes.addFlashAttribute("success", "Вы успешно вышли из системы");
        }
        return "redirect:/files";
    }
}


