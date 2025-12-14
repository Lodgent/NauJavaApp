package ru.Vladimir.NauJava.Models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "operation_history")
@NamedQueries({
    @NamedQuery(
        name = "OperationHistory.findByUserAndDateRange",
        query = "SELECT o FROM OperationHistory o WHERE o.user.username = :username " +
                "AND o.operationDate BETWEEN :startDate AND :endDate"
    ),
    @NamedQuery(
        name = "OperationHistory.findByOperationTypeAndStatus",
        query = "SELECT o FROM OperationHistory o WHERE o.operationType = :operationType " +
                "AND o.status = :status"
    )
})
public class OperationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long operationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private FileEntity file;

    @Column(nullable = false)
    private String operationType;

    @Column(nullable = false)
    private Date operationDate;

    @Column(nullable = false)
    private String status;

    // геттеры и сеттеры
    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FileEntity getFile() {
        return file;
    }

    public void setFile(FileEntity file) {
        this.file = file;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


