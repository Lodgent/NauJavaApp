package ru.Vladimir.NauJava.dao.custom;

import ru.Vladimir.NauJava.Models.OperationHistory;
import ru.Vladimir.NauJava.Models.User;
import jakarta.persistence.criteria.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

public class OperationHistoryCustomRepositoryImpl implements OperationHistoryCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<OperationHistory> findByUserAndDateRangeCriteria(String username, Date startDate, Date endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OperationHistory> cq = cb.createQuery(OperationHistory.class);
        Root<OperationHistory> historyRoot = cq.from(OperationHistory.class);
        Join<OperationHistory, User> userJoin = historyRoot.join("user");

        Predicate usernamePredicate = cb.equal(userJoin.get("username"), username);
        Predicate datePredicate = cb.between(historyRoot.get("operationDate"), startDate, endDate);

        cq.where(cb.and(usernamePredicate, datePredicate));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OperationHistory> findByOperationTypeAndStatusCriteria(String operationType, String status) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<OperationHistory> cq = cb.createQuery(OperationHistory.class);
        Root<OperationHistory> historyRoot = cq.from(OperationHistory.class);

        Predicate operationTypePredicate = cb.equal(historyRoot.get("operationType"), operationType);
        Predicate statusPredicate = cb.equal(historyRoot.get("status"), status);

        cq.where(cb.and(operationTypePredicate, statusPredicate));

        return entityManager.createQuery(cq).getResultList();
    }
}

