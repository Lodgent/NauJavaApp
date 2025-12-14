package ru.Vladimir.NauJava.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "file_tag")
public class FileTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(unique = true, nullable = false)
    private String tag;

    @ManyToMany(mappedBy = "tags")
    private Set<File> files;

    // геттеры и сеттеры
    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Set<File> getFiles() {
        return files;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }
}