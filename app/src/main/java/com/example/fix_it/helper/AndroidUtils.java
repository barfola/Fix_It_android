package com.example.fix_it.helper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.fix_it.api_dto.ProblemReport;
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

    public static void logProblemReportDetails(ProblemReport problemReport){
        Log.i("problemReportDetails", "Description: " + problemReport.getDescription());
        Log.i("problemReportDetails", "Role: " + problemReport.getRole());
        Log.i("problemReportDetails", "Location: " + problemReport.getLocation());  // Optional: remove in production
        Log.i("problemReportDetails", "Report type: " + problemReport.getReportType());
        Log.i("problemReportDetails", "Uuid: " + problemReport.getUuid());
        Log.i("problemReportDetails", "User: " + problemReport.getUser() );

    }


    public static String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }

    public static Bitmap convertBase64ToBitmap(String base64String) {
        try {
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String replaceUnderscoreWithSpace(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("_", " ");
    }


}
