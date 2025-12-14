package ru.Vladimir.NauJava.entity;


import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private Date registrationDate;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "user")
    private Set<File> files;

    @OneToMany(mappedBy = "user")
    private Set<OperationHistory> operations;

    // геттеры и сеттеры
}