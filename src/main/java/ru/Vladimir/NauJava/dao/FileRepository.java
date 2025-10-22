package ru.Vladimir.NauJava.dao;


import ru.Vladimir.NauJava.Models.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long>, FileCustomRepository{

    @Query("SELECT f FROM FileEntity f " +
            "JOIN f.user u " +
            "WHERE u.username = :username " +
            "AND f.uploadDate BETWEEN :startDate AND :endDate")
    Iterable<FileEntity> findFilesByUserAndDateRange(
            @Param("username") String username,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}
