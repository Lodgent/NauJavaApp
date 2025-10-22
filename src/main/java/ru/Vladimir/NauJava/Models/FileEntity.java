package ru.Vladimir.NauJava.Models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private long fileSize;

    @Column(nullable = false)
    private Date creationDate;

    @ManyToMany(mappedBy = "files")
    private Set<FileTag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;


    // Конструктор
    public FileEntity() {
        this.creationDate = new Date();
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Set<FileTag> getTags() {
        return tags;
    }

    public void setTags(Set<FileTag> tags) {
        this.tags = tags;
    }

    // Методы для работы с тегами
    public void addTag(FileTag tag) {
        tags.add(tag);
        tag.getFiles().add(this);
    }

    public void removeTag(FileTag tag) {
        tags.remove(tag);
        tag.getFiles().remove(this);
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize=" + fileSize +
                ", creationDate=" + creationDate +
                ", tags=" + tags.size() +
                '}';
    }
}
