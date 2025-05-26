package com.example.fix_it.api_dto;

import com.example.fix_it.api_dto.User;

public class UserManager {
    private static UserManager instance;
    private User currentUser;

    private UserManager() {}

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public User getUser() {
        return currentUser;
    }

    public void clearUser() {
        currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}