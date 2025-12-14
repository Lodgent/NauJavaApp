package ru.Vladimir.NauJava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Models.FileLink;
import ru.Vladimir.NauJava.Models.FileTag;
import ru.Vladimir.NauJava.Models.OperationHistory;
import ru.Vladimir.NauJava.Models.User;
import ru.Vladimir.NauJava.dao.FileRepository;
import ru.Vladimir.NauJava.dao.FileLinkRepository;
import ru.Vladimir.NauJava.dao.FileTagRepository;
import ru.Vladimir.NauJava.dao.OperationHistoryRepository;
import ru.Vladimir.NauJava.dao.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/custom")
@Tag(name = "Custom Repository API", description = "API для работы с кастомными методами репозиториев")
public class CustomRepositoryController {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileTagRepository fileTagRepository;

    @Autowired
    private FileLinkRepository fileLinkRepository;

    @Autowired
    private OperationHistoryRepository operationHistoryRepository;

    // FileRepository custom methods
    @Operation(summary = "Поиск файлов по пользователю и диапазону дат", description = "Возвращает список файлов пользователя за указанный период")
    @GetMapping("/files/byUserAndDateRange")
    public ResponseEntity<List<FileEntity>> findFilesByUserAndDateRange(
            @Parameter(description = "Имя пользователя") @RequestParam String username,
            @Parameter(description = "Начальная дата (формат: yyyy-MM-ddTHH:mm:ss)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
            @Parameter(description = "Конечная дата (формат: yyyy-MM-ddTHH:mm:ss)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
        List<FileEntity> files = fileRepository.findFilesByUserAndDateRange(username, startDate, endDate);
        return ResponseEntity.ok(files);
    }

    @Operation(summary = "Поиск файлов по размеру и типу", description = "Возвращает список файлов в указанном диапазоне размеров и заданного типа")
    @GetMapping("/files/bySizeAndType")
    public ResponseEntity<List<FileEntity>> findFilesBySizeAndType(
            @RequestParam Long minSize,
            @RequestParam Long maxSize,
            @RequestParam String contentType) {
        List<FileEntity> files = fileRepository.findFilesBySizeAndContentType(minSize, maxSize, contentType);
        return ResponseEntity.ok(files);
    }

    // UserRepository custom methods
    @GetMapping("/users/byUsernameCriteria")
    public ResponseEntity<User> findByUsernameCriteria(@RequestParam String username) {
        Optional<User> user = userRepository.findByUsernameCriteria(username);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/byUsernameContainingCriteria")
    public ResponseEntity<User> findByUsernameContainingCriteria(@RequestParam String pattern) {
        Optional<User> user = userRepository.findByUsernameContainingCriteria(pattern);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // FileTagRepository custom methods
    @GetMapping("/fileTags/byTagNameCriteria")
    public ResponseEntity<FileTag> findByTagNameCriteria(@RequestParam String tagName) {
        Optional<FileTag> tag = fileTagRepository.findByTagNameCriteria(tagName);
        return tag.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fileTags/byTagNameContainingCriteria")
    public ResponseEntity<List<FileTag>> findByTagNameContainingCriteria(@RequestParam String pattern) {
        List<FileTag> tags = fileTagRepository.findByTagNameContainingCriteria(pattern);
        return ResponseEntity.ok(tags);
    }

    // FileLinkRepository custom methods
    @GetMapping("/fileLinks/byFileIdAndNotExpiredCriteria")
    public ResponseEntity<List<FileLink>> findByFileIdAndNotExpiredCriteria(@RequestParam String fileId) {
        List<FileLink> links = fileLinkRepository.findByFileIdAndNotExpiredCriteria(fileId);
        return ResponseEntity.ok(links);
    }

    @GetMapping("/fileLinks/byAccessTokenCriteria")
    public ResponseEntity<FileLink> findByAccessTokenCriteria(@RequestParam String accessToken) {
        Optional<FileLink> link = fileLinkRepository.findByAccessTokenCriteria(accessToken);
        return link.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // OperationHistoryRepository custom methods
    @GetMapping("/operationHistory/byUserAndDateRangeCriteria")
    public ResponseEntity<List<OperationHistory>> findByUserAndDateRangeCriteria(
            @Parameter(description = "Имя пользователя") @RequestParam String username,
            @Parameter(description = "Начальная дата (формат: yyyy-MM-ddTHH:mm:ss)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
            @Parameter(description = "Конечная дата (формат: yyyy-MM-ddTHH:mm:ss)") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate) {
        List<OperationHistory> history = operationHistoryRepository.findByUserAndDateRangeCriteria(username, startDate, endDate);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/operationHistory/byOperationTypeAndStatusCriteria")
    public ResponseEntity<List<OperationHistory>> findByOperationTypeAndStatusCriteria(
            @RequestParam String operationType,
            @RequestParam String status) {
        List<OperationHistory> history = operationHistoryRepository.findByOperationTypeAndStatusCriteria(operationType, status);
        return ResponseEntity.ok(history);
    }
}

