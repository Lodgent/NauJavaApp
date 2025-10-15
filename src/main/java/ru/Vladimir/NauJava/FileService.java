package ru.Vladimir.NauJava;

import java.util.List;

public interface FileService {

    FileEntity uploadFile(String fileName, String content);

    String generateDownloadLink(Long fileId);

    String downloadFile(String link);

    List<FileEntity> getAllFiles();

}
