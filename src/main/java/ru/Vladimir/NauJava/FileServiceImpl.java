package ru.Vladimir.NauJava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileDao fileDao;

    @Override
    public FileEntity uploadFile(String fileName, String content) {
        FileEntity file = new FileEntity();
        file.setFileName(fileName);
        file.setFileContent(content);
        return fileDao.save(file);
    }

    @Override
    public String generateDownloadLink(Long fileId) {
        // Логика генерации ссылки
        return "link-" + fileId;
    }

    @Override
    public String downloadFile(String link) {
        // Логика скачивания
        return "Содержимое файла";
    }

    @Override
    public List<FileEntity> getAllFiles() {
        return fileDao.findAll();
    }
}
