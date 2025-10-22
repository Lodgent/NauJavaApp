package ru.Vladimir.NauJava.dao;


import com.filestorage.model.OperationHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationHistoryRepository extends CrudRepository<OperationHistory, Long> {

    // Пример метода с составным поиском
    Iterable<OperationHistory> findByOperationTypeAndStatusAndOperationDateBetween(
            String operationType,
            String status,
            Date startDate,
            Date endDate);
}