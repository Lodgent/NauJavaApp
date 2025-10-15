package ru.Vladimir.NauJava;
import java.util.List;

public interface FileDao {
    FileEntity save(FileEntity file);
    FileEntity findById(Long id);
    List<FileEntity> findAll();
    void deleteById(Long id);
}
