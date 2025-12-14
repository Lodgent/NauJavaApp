package ru.Vladimir.NauJava.dao.custom;


import ru.Vladimir.NauJava.Models.FileEntity;
import java.util.Date;
import java.util.List;

public interface FileCustomRepository {
    // Criteria API методы (аналоги именованных и HQL запросов)
    List<FileEntity> findFilesByUserAndDateRange(String username, Date startDate, Date endDate);
    List<FileEntity> findFilesBySizeAndContentType(Long minSize, Long maxSize, String contentType);
}