package ru.Vladimir.NauJava.dao;

import ru.Vladimir.NauJava.Models.FileLink;
import ru.Vladimir.NauJava.dao.custom.FileLinkCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileLinkRepository extends JpaRepository<FileLink, String>, FileLinkCustomRepository {
    
    // HQL запрос
    @Query("SELECT l FROM FileLink l WHERE l.fileId = :fileId AND l.isExpired = false")
    List<FileLink> findByFileIdAndNotExpiredHQL(@Param("fileId") String fileId);

    // Именованный запрос
    @Query(name = "FileLink.findByFileIdAndNotExpired")
    List<FileLink> findByFileIdAndNotExpiredNamed(@Param("fileId") String fileId);

    // HQL запрос для поиска по токену доступа
    @Query("SELECT l FROM FileLink l WHERE l.accessToken = :accessToken")
    Optional<FileLink> findByAccessTokenHQL(@Param("accessToken") String accessToken);

    // Именованный запрос для поиска по токену доступа
    @Query(name = "FileLink.findByAccessToken")
    Optional<FileLink> findByAccessTokenNamed(@Param("accessToken") String accessToken);
}