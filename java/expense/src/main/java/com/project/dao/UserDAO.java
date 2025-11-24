package com.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import com.project.util.Database;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDAO {
    public int findIdByUsernameAndPassword(String username, String password){
        int id = -1;
        Connection connection = Database.getConnection();
        String sql  = "SELECT id FROM users WHERE username=? AND user_password=? AND user_role LIKE('Manager')";
        try{
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) id = rs.getInt("id");
        } catch (Exception e) {
        }
        return id;
    }

    public int createUser(String username, String password){
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
            if(rs.next())id = rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
}
