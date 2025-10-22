package ru.Vladimir.NauJava.service;


import ru.Vladimir.NauJava.Models.FileEntity;
import  ru.Vladimir.NauJava.Models.FileTag;
import ru.Vladimir.NauJava.dao.FileRepository;
import ru.Vladimir.NauJava.dao.FileTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class FileTagService {

    private final FileRepository fileRepository;
    private final FileTagRepository fileTagRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public FileTagService(FileRepository fileRepository,
                          FileTagRepository fileTagRepository,
                          PlatformTransactionManager transactionManager) {
        this.fileRepository = fileRepository;
        this.fileTagRepository = fileTagRepository;
        this.transactionManager = transactionManager;
    }

    /**
     * Создает файл и связанные с ним теги в рамках одной транзакции
     * @param file новый файл
     * @param tagNames список названий тегов
     * @return созданный файл с тегами
     */
    public File createFileWithTags(File file, List<String> tagNames) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // Сохраняем файл
            File savedFile = fileRepository.save(file);

            // Создаем теги
            for (String tagName : tagNames) {
                FileTag tag = new FileTag();
                tag.setTag(tagName);
                tag = fileTagRepository.save(tag);

                // Связываем файл с тегом
                savedFile.getTags().add(tag);
            }

            // Сохраняем изменения
            fileRepository.save(savedFile);

            transactionManager.commit(status);
            return savedFile;
        } catch (DataAccessException ex) {
            transactionManager.rollback(status);
            throw ex;
        }
    }
}