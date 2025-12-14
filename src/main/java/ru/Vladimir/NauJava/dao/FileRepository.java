package ru.Vladimir.NauJava.dao;


import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.dao.custom.FileCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RepositoryRestResource(collectionResourceRel = "files", path = "files")
public interface FileRepository extends JpaRepository<FileEntity, String>, FileCustomRepository{

    // HQL запрос
    @Query("SELECT f FROM FileEntity f " +
            "JOIN f.owner u " +
            "WHERE u.username = :username " +
            "AND f.creationDate BETWEEN :startDate AND :endDate")
    List<FileEntity> findFilesByUserAndDateRangeHQL(
            @Param("username") String username,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Именованный запрос
    @Query(name = "FileEntity.findByOwnerAndDateRange")
    List<FileEntity> findFilesByOwnerAndDateRangeNamed(
            @Param("username") String username,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // HQL запрос для поиска по размеру и типу
    @Query("SELECT f FROM FileEntity f " +
            "WHERE f.fileSize BETWEEN :minSize AND :maxSize " +
            "AND f.fileType = :fileType")
    List<FileEntity> findFilesBySizeAndTypeHQL(
            @Param("minSize") Long minSize,
            @Param("maxSize") Long maxSize,
            @Param("fileType") String fileType);

    // Именованный запрос для поиска по размеру и типу
    @Query(name = "FileEntity.findBySizeAndType")
    List<FileEntity> findFilesBySizeAndTypeNamed(
            @Param("minSize") Long minSize,
            @Param("maxSize") Long maxSize,
            @Param("fileType") String fileType);

    // Запрос для загрузки всех файлов с владельцами (fetch join)
    @Query("SELECT DISTINCT f FROM FileEntity f LEFT JOIN FETCH f.owner ORDER BY f.creationDate DESC")
    List<FileEntity> findAllWithOwner();
}
