package ru.Vladimir.NauJava.Models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "file_tag")
@NamedQueries({
    @NamedQuery(
        name = "FileTag.findByTagName",
        query = "SELECT t FROM FileTag t WHERE t.tag = :tagName"
    ),
    @NamedQuery(
        name = "FileTag.findByTagNameContaining",
        query = "SELECT t FROM FileTag t WHERE t.tag LIKE :pattern"
    )
})
public class FileTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(unique = true, nullable = false)
    private String tag;

    @ManyToMany(mappedBy = "tags")
    private Set<FileEntity> files = new HashSet<>();

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

    public Set<FileEntity> getFiles() {
        return files;
    }

    public void setFiles(Set<FileEntity> files) {
        this.files = files;
    }
}
