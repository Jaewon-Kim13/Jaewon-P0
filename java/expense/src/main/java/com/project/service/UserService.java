package com.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.project.dao.UserDAO;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserDAO userDao = new UserDAO();

    public int login(String username, String password){
        logger.info("Login attempt for username: {}", username);
        int userId = -1;
        try {
            userId = userDao.findIdByUsernameAndPassword(username, password);
            if(userId != -1) {
                logger.info("Login successful for username: {}, user_id: {}", username, userId);
            } else {
                logger.warn("Login failed for username: {} - invalid credentials", username);
            }
        } catch (Exception e) {
            logger.error("Login failed for username: {}", username, e);
            System.out.println("Login Failed: "+e.getMessage());
        }

        return userId;
    }

    public int createManager(String username, String password){
        logger.info("Manager creation attempt for username: {}", username);
        int userId = -1;
        try {
            userId = userDao.createUser(username, password);
            if(userId != -1) {
                logger.info("Manager created successfully - username: {}, user_id: {}", username, userId);
            } else {
                logger.warn("Manager creation failed for username: {}", username);
            }
        } catch (Exception e) {
            logger.error("Manager creation failed for username: {}", username, e);
            System.out.println("Manager creation Failed: "+e.getMessage());
        }

        return userId;
    }
}
