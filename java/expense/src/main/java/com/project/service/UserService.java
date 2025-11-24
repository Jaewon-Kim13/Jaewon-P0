package com.project.service;

import com.project.dao.UserDAO;

public class UserService {
    private final UserDAO userDao = new UserDAO();

    public int login(String username, String password){
        int userId = -1;
        try {
            userId = userDao.findIdByUsernameAndPassword(username, password);
        } catch (Exception e) {
            System.out.println("Login Failed: "+e.getMessage());
        }

        return userId;
    }

    public int createManager(String username, String password){
        int userId = -1;
        try {
            userId = userDao.createUser(username, password);
        } catch (Exception e) {
            System.out.println("Manager creation Failed: "+e.getMessage());
        }

        return userId;
    }
}
