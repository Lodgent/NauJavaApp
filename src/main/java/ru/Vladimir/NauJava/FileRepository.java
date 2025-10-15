package ru.Vladimir.NauJava;
import java.util.ArrayList;
import java.util.List;

// Добавляем импорт для @Component
import org.springframework.stereotype.Component;

@Component
public class FileRepository {
    private List<FileEntity> files = new ArrayList<>();
    private Long idCounter = 1L;

    public FileEntity save(FileEntity file) {
        file.setId(idCounter++);
        files.add(file);
        return file;
    }

    public FileEntity findById(Long id) {
        return files.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}

