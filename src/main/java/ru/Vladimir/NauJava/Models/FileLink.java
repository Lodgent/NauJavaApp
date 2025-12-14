package ru.Vladimir.NauJava.Models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "file_links")
@NamedQueries({
    @NamedQuery(
        name = "FileLink.findByFileIdAndNotExpired",
        query = "SELECT l FROM FileLink l WHERE l.fileId = :fileId AND l.isExpired = false"
    ),
    @NamedQuery(
        name = "FileLink.findByAccessToken",
        query = "SELECT l FROM FileLink l WHERE l.accessToken = :accessToken"
    )
})
public class FileLink {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String accessToken;

    @Column(name = "file_id", nullable = false)
    private String fileId;

    @Column(nullable = false)
    private long expirationTime;

    @Column(nullable = false)
    private boolean isExpired;

    @Column(nullable = false)
    private Date creationDate;

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
