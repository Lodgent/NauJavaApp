package ru.Vladimir.NauJava.dao;

import ru.Vladimir.NauJava.Models.FileTag;
import ru.Vladimir.NauJava.dao.custom.FileTagCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileTagRepository extends JpaRepository<FileTag, Long>, FileTagCustomRepository {
    
    // HQL запрос
    @Query("SELECT t FROM FileTag t WHERE t.tag = :tagName")
    Optional<FileTag> findByTagNameHQL(@Param("tagName") String tagName);

    // Именованный запрос
    @Query(name = "FileTag.findByTagName")
    Optional<FileTag> findByTagNameNamed(@Param("tagName") String tagName);

    // HQL запрос для поиска по части имени
    @Query("SELECT t FROM FileTag t WHERE t.tag LIKE :pattern")
    List<FileTag> findByTagNameContainingHQL(@Param("pattern") String pattern);

    // Именованный запрос для поиска по части имени
    @Query(name = "FileTag.findByTagNameContaining")
    List<FileTag> findByTagNameContainingNamed(@Param("pattern") String pattern);
}