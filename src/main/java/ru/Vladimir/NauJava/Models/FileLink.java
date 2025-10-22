package ru.Vladimir.NauJava.Models;

import java.util.Date;

public class FileLink {
    private String accessToken;
    private String fileId;
    private long expirationTime;
    private boolean isExpired;
    private final Date creationDate;


    public FileLink() {
        this.creationDate = new Date();
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

    // TODO проверки истечения срока действия ссылки
    public boolean checkExpiration() {
        long currentTime = System.currentTimeMillis();
        return currentTime > expirationTime;
    }

    @Override
    public String toString() {
        return "FileLink{" +
                "accessToken='" + accessToken + '\'' +
                ", fileId='" + fileId + '\'' +
                ", expirationTime=" + expirationTime +
                ", isExpired=" + isExpired +
                ", creationDate=" + creationDate +
                '}';
    }
}
