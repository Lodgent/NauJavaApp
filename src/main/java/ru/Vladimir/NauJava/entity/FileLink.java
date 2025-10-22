package ru.Vladimir.NauJava.entity;


import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "file_link")
public class FileLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @Column(unique = true, nullable = false)
    private String linkCode;

    @Column(nullable = false)
    private Date expirationDate;

    @Column(nullable = false)
    private Date createdAt;

    @Column(nullable = false)
    private boolean isActive;

    // геттеры и сеттеры
}