package ru.Vladimir.NauJava.dao.custom;

import ru.Vladimir.NauJava.Models.FileTag;
import java.util.List;
import java.util.Optional;

public interface FileTagCustomRepository {
    // Criteria API методы (аналоги именованных и HQL запросов)
    Optional<FileTag> findByTagNameCriteria(String tagName);
    List<FileTag> findByTagNameContainingCriteria(String pattern);
}

