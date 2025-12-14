package ru.Vladimir.NauJava.dao.custom;

import ru.Vladimir.NauJava.Models.User;
import jakarta.persistence.criteria.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public class UserCustomRepositoryImpl implements UserCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameCriteria(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> userRoot = cq.from(User.class);

        Predicate usernamePredicate = cb.equal(userRoot.get("username"), username);
        cq.where(usernamePredicate);

        List<User> results = entityManager.createQuery(cq).getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameContainingCriteria(String pattern) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> userRoot = cq.from(User.class);

        Predicate patternPredicate = cb.like(userRoot.get("username"), pattern);
        cq.where(patternPredicate);

        List<User> results = entityManager.createQuery(cq).getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}

