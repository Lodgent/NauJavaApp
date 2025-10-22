package ru.Vladimir.NauJava.entity;


import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "operation_history")
public class OperationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long operationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Column(nullable = false)
    private String operationType;

    @Column(nullable = false)
    private Date operationDate;

    @Column(nullable = false)
    private String status;

    // геттеры и сеттеры
}