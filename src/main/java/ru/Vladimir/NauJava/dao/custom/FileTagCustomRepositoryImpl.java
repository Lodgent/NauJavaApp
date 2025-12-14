package ru.Vladimir.NauJava.dao.custom;

import ru.Vladimir.NauJava.Models.FileTag;
import jakarta.persistence.criteria.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public class FileTagCustomRepositoryImpl implements FileTagCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Optional<FileTag> findByTagNameCriteria(String tagName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileTag> cq = cb.createQuery(FileTag.class);
        Root<FileTag> tagRoot = cq.from(FileTag.class);

        Predicate tagPredicate = cb.equal(tagRoot.get("tag"), tagName);
        cq.where(tagPredicate);

        List<FileTag> results = entityManager.createQuery(cq).getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileTag> findByTagNameContainingCriteria(String pattern) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileTag> cq = cb.createQuery(FileTag.class);
        Root<FileTag> tagRoot = cq.from(FileTag.class);

        Predicate patternPredicate = cb.like(tagRoot.get("tag"), pattern);
        cq.where(patternPredicate);

        return entityManager.createQuery(cq).getResultList();
    }
}

