package ru.Vladimir.NauJava.Models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NamedQueries({
    @NamedQuery(
        name = "User.findByUsernameAndStatus",
        query = "SELECT u FROM User u WHERE u.username = :username"
    ),
    @NamedQuery(
        name = "User.findByUsernameContaining",
        query = "SELECT u FROM User u WHERE u.username LIKE :pattern"
    )
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FileEntity> files = new HashSet<>();

    // Конструктор
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Пустой конструктор для JPA
    public User() {}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<FileEntity> getFiles() {
        return files;
    }

    public void setFiles(Set<FileEntity> files) {
        this.files = files;
    }

    // Метод для добавления файла
    public void addFile(FileEntity file) {
        files.add(file);
        file.setOwner(this);
    }

    // Метод для удаления файла
    public void removeFile(FileEntity file) {
        files.remove(file);
        file.setOwner(null);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", files=" + files.size() +
                '}';
    }
}
