package com.exam.course.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class DatabaseConfig {
    private static final String DEFAULT_URL =
            "jdbc:mysql://localhost:3306/course_selection?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "123456";
    private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";

    private final Properties properties = new Properties();

    public DatabaseConfig() {
        properties.setProperty("db.url", DEFAULT_URL);
        properties.setProperty("db.username", DEFAULT_USERNAME);
        properties.setProperty("db.password", DEFAULT_PASSWORD);
        properties.setProperty("db.driver", DEFAULT_DRIVER);
        loadExternalProperties();
    }

    private void loadExternalProperties() {
        Path path = Path.of("db.properties");
        if (!Files.exists(path)) {
            return;
        }
        try (InputStream inputStream = Files.newInputStream(path)) {
            Properties fileProperties = new Properties();
            fileProperties.load(inputStream);
            fileProperties.forEach((key, value) -> properties.setProperty(String.valueOf(key), String.valueOf(value)));
        } catch (IOException exception) {
            throw new IllegalStateException("读取 db.properties 失败: " + exception.getMessage(), exception);
        }
    }

    public String getUrl() {
        return properties.getProperty("db.url");
    }

    public String getUsername() {
        return properties.getProperty("db.username");
    }

    public String getPassword() {
        return properties.getProperty("db.password");
    }

    public String getDriver() {
        return properties.getProperty("db.driver");
    }
}
