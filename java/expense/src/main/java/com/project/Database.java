package com.project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static Connection connection = null;

    public static Connection getConnection() {
        if(connection != null) return connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:../persist.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void loadSQLFile(String filePath) {
        Connection conn = getConnection();
        StringBuilder sql = new StringBuilder();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--")) continue;

                sql.append(line).append(" ");
                if (line.endsWith(";")) {
                    String statement = sql.toString().trim();
                    if (!statement.isEmpty()) {
                        try (Statement stmt = conn.createStatement()) {
                            stmt.execute(statement);
                        }
                    }
                    sql.setLength(0); // Clear the buffer
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
