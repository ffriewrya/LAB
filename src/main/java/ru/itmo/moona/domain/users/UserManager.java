package ru.itmo.moona.domain.users;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class UserManager {

    private HashMap<Long, User> users;

    private long genId() {
        return users.size() + 1;
    }

    public void register(String login, String password) {
        String pssw = hash(password);
        Long id = genId();
        User newUser = new User(id, login, pssw);
        users.put(id, newUser);
    }

    public void login(String login, String password) {
        User user = findByLogin(login);
        String pssw = hash(password);
        if (user.getPassword().equals(pssw)) {
            CurrentUser.getInstance().login(user);
        }
        throw new IllegalArgumentException("wrong password");
    }

    private User findByLogin(String login) {
        for (User u : users.values()) {
            if (u.getLogin().equals(login)) {
                return u;
            }
        }
        throw new IllegalArgumentException("user with this login doesn't exist");
    }

    private User findById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new IllegalArgumentException("user with this ic doesn't exist");
        }
        return user;
    }

    public String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(64);
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("hashing error");
        }
    }
}
