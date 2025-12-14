package ru.Vladimir.NauJava.dao.custom;


import ru.Vladimir.NauJava.Models.FileEntity;
import ru.Vladimir.NauJava.Models.User;
import jakarta.persistence.criteria.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

public class FileCustomRepositoryImpl implements FileCustomRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<FileEntity> findFilesByUserAndDateRange(String username, Date startDate, Date endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileEntity> cq = cb.createQuery(FileEntity.class);
        Root<FileEntity> fileRoot = cq.from(FileEntity.class);
        Join<FileEntity, User> userJoin = fileRoot.join("owner");

        Predicate usernamePredicate = cb.equal(userJoin.get("username"), username);
        Predicate datePredicate = cb.between(fileRoot.get("creationDate"), startDate, endDate);

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
        Predicate contentTypePredicate = cb.equal(fileRoot.get("fileType"), contentType);

        cq.where(cb.and(sizePredicate, contentTypePredicate));

        return entityManager.createQuery(cq).getResultList();
    }
}