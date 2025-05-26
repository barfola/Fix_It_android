package com.example.fix_it.helper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.example.fix_it.api_dto.User;

import java.io.ByteArrayOutputStream;

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


    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


}
