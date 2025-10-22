package ru.Vladimir.NauJava.Models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "file_links")
public class FileLink {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String accessToken;

    @Column(nullable = false)
    private String fileId;

    @Column(nullable = false)
    private long expirationTime;

    @Column(nullable = false)
    private boolean isExpired;

    @Column(nullable = false)
    private Date creationDate;

    // Связь с файлом
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id", insertable = false, updatable = false)
    private FileEntity file;

    // Конструктор
    public FileLink() {
        this.creationDate = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public FileEntity getFile() {
        return file;
    }

    public void setFile(FileEntity file) {
        this.file = file;
        this.fileId = file.getId();
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    // Метод проверки истечения срока действия ссылки
    public boolean checkExpiration() {
        long currentTime = System.currentTimeMillis();
        boolean expired = currentTime > expirationTime;
        if (expired) {
            setExpired(true);
        }
        return expired;
    }

    @Override
    public String toString() {
        return "FileLink{" +
                "id='" + id + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", fileId='" + fileId + '\'' +
                ", expirationTime=" + expirationTime +
                ", isExpired=" + isExpired +
                ", creationDate=" + creationDate +
                '}';
    }
}
