package ru.Vladimir.NauJava.entity;


import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false)
    private Date uploadDate;

    @Column(nullable = false)
    private String storagePath;

    @OneToMany(mappedBy = "file")
    private Set<FileLink> links;

    @ManyToMany
    @JoinTable(
            name = "file_tag_map",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<FileTag> tags;

    // геттеры и сеттеры
}