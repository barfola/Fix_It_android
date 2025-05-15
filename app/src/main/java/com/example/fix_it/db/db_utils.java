package com.example.fix_it.db;

import android.content.Context;
import android.util.Log;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class db_utils {

    private static db_utils instance;
    private db_utils()
    {

    }

    public static db_utils getInstance()
    {
        if (instance==null)
        {
            instance=new db_utils();
        }
        return instance;
    }

    public static void saveDataToFile(Context context, String fileName, String data) {
        try {
            // Open the file in write mode (MODE_PRIVATE means it's private to the app)
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            // Write the string data as bytes to the file
            fos.write(data.getBytes());

            // Close the file output stream
            fos.close();

            // Log success or handle any other UI updates
            Log.d("FileSave", "Data saved successfully!");
        } catch (IOException e) {
            // Handle the error if something goes wrong
            e.printStackTrace();
            Log.d("FileSave", "Error saving data");
        }
    }

    public static String readDataFromFile(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            // Open the file in read mode
            FileInputStream fis = context.openFileInput(fileName);

            // Read the file character by character
            int character;
            while ((character = fis.read()) != -1) {
                stringBuilder.append((char) character);
            }

            // Close the input stream
            fis.close();

            // Return the data as a String
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
