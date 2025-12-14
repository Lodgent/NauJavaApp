package ru.Vladimir.NauJava.dao.custom;

import ru.Vladimir.NauJava.Models.FileLink;
import java.util.List;
import java.util.Optional;

public interface FileLinkCustomRepository {
    // Criteria API методы (аналоги именованных и HQL запросов)
    List<FileLink> findByFileIdAndNotExpiredCriteria(String fileId);
    Optional<FileLink> findByAccessTokenCriteria(String accessToken);
}

