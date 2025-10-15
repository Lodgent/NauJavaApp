package ru.Vladimir.NauJava.Models;

import java.util.Date;
public class FileEntity {
    private String id;
    private String fileName;
    private String ownerId;
    private byte[] content;
    private long fileSize;
    private String fileType;
    private final Date uploadDate;

    // Конструктор
    public FileEntity() {
        this.uploadDate = new Date();
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
        this.fileSize = content.length;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    // Метод для определения типа файла
    public void detectFileType(String fileName) {
        if (fileName != null) {
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                this.fileType = fileName.substring(dotIndex + 1);
            }
        }
    }

    @Override
    public String toString() {
        return "FileEntity{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", ownerId='" + ownerId + '\'' +
                ", fileSize=" + fileSize +
                ", fileType='" + fileType + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
