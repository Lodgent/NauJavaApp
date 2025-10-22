package ru.Vladimir.NauJava.Services;


import org.springframework.stereotype.Service;
import ru.Vladimir.NauJava.Models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, User> users = new HashMap<>();

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Ошибка хеширования пароля", e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public void registerUser(String username, String password) {
        if (users.containsKey(username)) {
            throw new RuntimeException("Пользователь уже существует");
        }

        String encodedPassword = hashPassword(password);
        User user = new User(username, encodedPassword);
        users.put(username, user);
    }

    public boolean authenticateUser(String username, String password) {
        User user = users.get(username);
        if (user == null) return false;

        return hashPassword(password).equals(user.getPassword());
    }

    public User getUserById(String userId) {
        return users.get(userId);
    }
}




