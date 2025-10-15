package ru.Vladimir.NauJava.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.Vladimir.NauJava.Models.FileLink;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class LinkService {
    private final Map<String, FileLink> linkMap = new HashMap<>();
    private final FileService fileService;
    private final Random random = new Random();

    @Autowired
    public LinkService(@Lazy FileService fileService) {
        this.fileService = fileService;
    }

    public String generateLink(String fileId, int expirationMinutes) {
        String token = generateRandomToken();
        FileLink link = new FileLink();
        link.setFileId(fileId);
        link.setAccessToken(token);
        link.setExpirationTime(System.currentTimeMillis() + (long) expirationMinutes * 60 * 1000);

        linkMap.put(token, link);
        return token;
    }

    public boolean isValidLink(String token) {
        FileLink link = linkMap.get(token);
        if (link == null) return false;

        return System.currentTimeMillis() < link.getExpirationTime();
    }

    public FileLink getLink(String link) {
        return linkMap.get(link);
    }    
    
    private String generateRandomToken() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }
}

