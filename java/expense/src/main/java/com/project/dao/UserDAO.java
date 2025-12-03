package com.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.util.Database;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
    
    public int findIdByUsernameAndPassword(String username, String password){
        logger.info("Attempting to find user by username: {}", username);
        int id = -1;
        Connection connection = Database.getConnection();
        String sql  = "SELECT id FROM users WHERE username=? AND user_password=? AND user_role LIKE('Manager')";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                id = rs.getInt("id");
                logger.info("User found with id: {}", id);
            } else {
                logger.warn("No manager user found for username: {}", username);
            }
        } catch (Exception e) {
            logger.error("Error finding user by username: {}", username, e);
        }
        return id;
    }

    public int createUser(String username, String password){
        logger.info("Creating new user with username: {}", username);
        int id = -1;
        String sql = "INSERT INTO users (username, user_password, user_role) VALUES(?, ?, ?)";
        Connection connection = Database.getConnection();
        try{
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, "Manager");
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                id = rs.getInt(1);
                logger.info("User created successfully with id: {}", id);
            }
        } catch (Exception e) {
            logger.error("Failed to create user with username: {}", username, e);
        }
        return id;
    }
}