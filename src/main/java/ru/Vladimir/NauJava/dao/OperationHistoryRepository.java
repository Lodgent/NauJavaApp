package ru.Vladimir.NauJava.dao;


import ru.Vladimir.NauJava.Models.OperationHistory;
import ru.Vladimir.NauJava.dao.custom.OperationHistoryCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OperationHistoryRepository extends JpaRepository<OperationHistory, Long>, OperationHistoryCustomRepository {

    // HQL запрос
    @Query("SELECT o FROM OperationHistory o WHERE o.user.username = :username " +
           "AND o.operationDate BETWEEN :startDate AND :endDate")
    List<OperationHistory> findByUserAndDateRangeHQL(
            @Param("username") String username,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    // Именованный запрос
    @Query(name = "OperationHistory.findByUserAndDateRange")
    List<OperationHistory> findByUserAndDateRangeNamed(
            @Param("username") String username,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

    // HQL запрос для поиска по типу операции и статусу
    @Query("SELECT o FROM OperationHistory o WHERE o.operationType = :operationType " +
           "AND o.status = :status")
    List<OperationHistory> findByOperationTypeAndStatusHQL(
            @Param("operationType") String operationType,
            @Param("status") String status);

    // Именованный запрос для поиска по типу операции и статусу
    @Query(name = "OperationHistory.findByOperationTypeAndStatus")
    List<OperationHistory> findByOperationTypeAndStatusNamed(
            @Param("operationType") String operationType,
            @Param("status") String status);
}