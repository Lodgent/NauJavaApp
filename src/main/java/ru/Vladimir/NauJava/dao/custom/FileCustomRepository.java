package ru.Vladimir.NauJava.dao.custom;


import ru.Vladimir.NauJava.Models.FileEntity;
import java.util.Date;
import java.util.List;

public interface FileCustomRepository {
    List<FileEntity> findFilesByUserAndDateRange(String username, Date startDate, Date endDate);
    List<FileEntity> findFilesBySizeAndContentType(Long minSize, Long maxSize, String contentType);
}