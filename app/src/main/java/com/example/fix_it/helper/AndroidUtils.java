package com.example.fix_it.helper;

import android.content.Intent;
import android.util.Log;

import com.example.fix_it.api_dto.User;

public class AndroidUtils {
    public static void logUserDetails(User user) {
        Log.i("UserDetails", "Session: " + user.getSessionID());
        Log.i("UserDetails", "UUID: " + user.getUuid());
        Log.i("UserDetails", "Password: " + user.getPassword());  // Optional: remove in production
        Log.i("UserDetails", "HashPassword: " + user.getHashPassword());
        Log.i("UserDetails", "AdminLevel: " + user.getAdminLevel());
        Log.i("UserDetails", "Username: " + user.getUserName());
        Log.i("UserDetails", "Message: " + user.getMessage());
    }
}
