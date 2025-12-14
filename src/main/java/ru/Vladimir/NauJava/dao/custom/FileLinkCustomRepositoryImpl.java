package ru.Vladimir.NauJava.dao.custom;

import ru.Vladimir.NauJava.Models.FileLink;
import jakarta.persistence.criteria.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public class FileLinkCustomRepositoryImpl implements FileLinkCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<FileLink> findByFileIdAndNotExpiredCriteria(String fileId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileLink> cq = cb.createQuery(FileLink.class);
        Root<FileLink> linkRoot = cq.from(FileLink.class);

        Predicate fileIdPredicate = cb.equal(linkRoot.get("fileId"), fileId);
        Predicate notExpiredPredicate = cb.equal(linkRoot.get("isExpired"), false);
        
        cq.where(cb.and(fileIdPredicate, notExpiredPredicate));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FileLink> findByAccessTokenCriteria(String accessToken) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileLink> cq = cb.createQuery(FileLink.class);
        Root<FileLink> linkRoot = cq.from(FileLink.class);

        Predicate tokenPredicate = cb.equal(linkRoot.get("accessToken"), accessToken);
        cq.where(tokenPredicate);

        List<FileLink> results = entityManager.createQuery(cq).getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}

