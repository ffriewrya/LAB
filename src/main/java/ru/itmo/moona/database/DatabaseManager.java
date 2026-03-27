package ru.itmo.moona.database;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {

    public Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("db.cfg")) {
            if (inputStream == null) {
                throw new IOException();
            }
            properties.load(inputStream);

            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");
            return DriverManager.getConnection(url, user, password);

        } catch (IOException e) {
            throw new RuntimeException("haven't found .cfg file");
        }
    }
}
