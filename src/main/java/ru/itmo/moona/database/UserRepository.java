package ru.itmo.moona.database;

import ru.itmo.moona.domain.users.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private final DatabaseManager manager;

    public UserRepository(DatabaseManager manager) {
        this.manager = manager;
    }

    private String hash(String password) {
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

    public boolean authenticate(String password, User user) {
        if (user == null) {
            throw new IllegalArgumentException("haven't found any user");
        }
        return user.getPassword().equals(hash(password));
    }

    public void save(String username, String password) throws SQLException {
        String sql = "INSERT INTO users (login, hashed_password) VALUES (?, ?)";

        try (Connection connection = manager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, hash(password));

            statement.executeUpdate();
        }

    }

    public User getUser(String login) throws SQLException {
        String sql = "SELECT * FROM users WHERE login = ?";

        try (Connection connection = manager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, login);

            try (ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    return new User(set.getLong("id"), set.getString("login"), set.getString("hashed_password"));
                } else {
                    throw new IllegalArgumentException("haven't found any user");
                }
            }

        }
    }
}
