package com.exam.course.util;

import com.exam.course.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionManager {
    private static final DatabaseConfig CONFIG = new DatabaseConfig();

    static {
        try {
            Class.forName(CONFIG.getDriver());
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("未找到 MySQL JDBC 驱动，请确认 lib 目录已加入类路径。", exception);
        }
    }

    private ConnectionManager() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                CONFIG.getUrl(),
                CONFIG.getUsername(),
                CONFIG.getPassword()
        );
    }
}
