package ru.Vladimir.NauJava.dao.custom;


import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Models.User;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@Repository
public class FileCustomRepositoryImpl implements FileCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<FileEntity> findFilesByUserAndDateRange(String username, Date startDate, Date endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileEntity> cq = cb.createQuery(FileEntity.class);
        Root<FileEntity> fileRoot = cq.from(FileEntity.class);
        Join<FileEntity, User> userJoin = fileRoot.join("user");

        Predicate usernamePredicate = cb.equal(userJoin.get("username"), username);
        Predicate datePredicate = cb.between(fileRoot.get("uploadDate"), startDate, endDate);

        cq.where(cb.and(usernamePredicate, datePredicate));

        return entityManager.createQuery(cq).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<FileEntity> findFilesBySizeAndContentType(Long minSize, Long maxSize, String contentType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileEntity> cq = cb.createQuery(FileEntity.class);
        Root<FileEntity> fileRoot = cq.from(FileEntity.class);

        Predicate sizePredicate = cb.between(fileRoot.get("fileSize"), minSize, maxSize);
        Predicate contentTypePredicate = cb.equal(fileRoot.get("contentType"), contentType);

        cq.where(cb.and(sizePredicate, contentTypePredicate));

        return entityManager.createQuery(cq).getResultList();
    }
}