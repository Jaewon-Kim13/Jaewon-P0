package com.project.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    private static Connection connection = null;
    private static Properties properties;

    static {
        loadProperties();
    }

    private static void loadProperties() {
        properties = new Properties();
        try (InputStream input = Database.class.getClassLoader()
                .getResourceAsStream("app.properties")) {
            if (input == null) {
                logger.error("Unable to find app.properties file");
                return;
            }
            properties.load(input);
            logger.info("Properties loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading properties file", e);
        }
    }

    public static Connection getConnection() {
        if(connection != null) return connection;
        
        try {
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            String driver = properties.getProperty("db.driver");
            
            // Load the MySQL driver
            Class.forName(driver);
            
            logger.info("Establishing database connection to: {}", url);
            connection = DriverManager.getConnection(url, username, password);
            logger.info("Database connection established successfully");
            
            String schemaPath = properties.getProperty("db.schema.path");
            if (schemaPath != null && !schemaPath.isEmpty()) {
                loadSQLFile(schemaPath);
            }
        } catch (SQLException e) {
            logger.error("Failed to establish database connection", e);
        } catch (ClassNotFoundException e) {
            logger.error("MySQL JDBC driver not found", e);
        }
        
        return connection;
    }

    private static void loadSQLFile(String filePath) {
        logger.info("Loading SQL file: {}", filePath);
        Connection conn = getConnection();
        StringBuilder sql = new StringBuilder();
        int statementsExecuted = 0;
        
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
                            statementsExecuted++;
                        }
                    }
                    sql.setLength(0); // Clear the buffer
                }
            }
            logger.info("SQL file loaded successfully - {} statements executed", statementsExecuted);
        } catch (IOException e) {
            logger.error("Failed to read SQL file: {}", filePath, e);
        } catch (SQLException e) {
            logger.error("Failed to execute SQL from file: {}", filePath, e);
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                logger.info("Closing database connection");
                connection.close();
                logger.info("Database connection closed successfully");
            } catch (SQLException e) {
                logger.error("Failed to close database connection", e);
            }
        }
    }
}