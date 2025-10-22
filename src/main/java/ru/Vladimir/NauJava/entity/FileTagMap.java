package ru.Vladimir.NauJava.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "file_tag_map")
public class FileTagMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileTagMapId;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private FileTag tag;

    // геттеры и сеттеры
    public Long getFileTagMapId() {
        return fileTagMapId;
    }

    public void setFileTagMapId(Long fileTagMapId) {
        this.fileTagMapId = fileTagMapId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public FileTag getTag() {
        return tag;
    }

    public void setTag(FileTag tag) {
        this.tag = tag;
    }
}
