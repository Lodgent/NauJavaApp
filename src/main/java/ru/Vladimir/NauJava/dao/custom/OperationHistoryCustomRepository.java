package ru.Vladimir.NauJava.dao.custom;

import ru.Vladimir.NauJava.Models.OperationHistory;
import java.util.Date;
import java.util.List;

public interface OperationHistoryCustomRepository {
    // Criteria API методы (аналоги именованных и HQL запросов)
    List<OperationHistory> findByUserAndDateRangeCriteria(String username, Date startDate, Date endDate);
    List<OperationHistory> findByOperationTypeAndStatusCriteria(String operationType, String status);
}

