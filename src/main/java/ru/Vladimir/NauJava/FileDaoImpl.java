package ru.Vladimir.NauJava;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileDaoImpl implements FileDao {
    @Autowired
    private FileRepository fileRepository;

    @Override
    public FileEntity save(FileEntity file) {
        return fileRepository.save(file);
    }

    @Override
    public FileEntity findById(Long id) {
        return null;
    }

    @Override
    public List<FileEntity> findAll() {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    // TODO Остальные методы
}