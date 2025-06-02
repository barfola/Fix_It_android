package com.example.fix_it.api;

import android.util.Log;

import com.example.fix_it.api_dto.User;
import com.example.fix_it.api_dto.Users;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class usersApi {

    public usersApi()
    {
    }

    /*
    {
        "userId": "hdfkasdjh2398457",
         "name": "jhon"
    }
    */


    public static Users getUser(String userId)
    {
        Users users=null;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.example.com/users/"+userId)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                Log.i("usersApi","get Users:"+responseData);

                String json = "{\"user_id\":\"John\", \"name\":30}";

                Gson gson = new Gson();
                users = gson.fromJson(json, Users.class);


                System.out.println("Response: " + responseData);
            } else {
                System.out.println("Request failed: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static void getSessionId(String url, SessionCallback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                callback.onSessionError("Network error: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.i("user session id", "session: " + responseData);

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String sessionId = jsonObject.getString("session_id");
                        callback.onSessionReceived(sessionId);
                    } catch (JSONException e) {
                        callback.onSessionError("JSON error: " + e.getMessage());
                    }
                } else {
                    callback.onSessionError("Request failed: " + response.code());
                }
            }
        });
    }

    public static void sendUserToServer(String serverUrl, User user, ServerResponseCallback callback) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String jsonBody = gson.toJson(user);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(serverUrl) // e.g., http://192.168.1.100:5000/receive_user
                .post(body)
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    int statusCode = response.code();
                    Log.i("userApi", "User data sent successfully: " + response.code());
                    callback.onSuccess(response.body().string());
                } else {
                    int statusCode = response.code();
                    Log.e("userApi", "Failed to send user: " + response.code());
                    callback.onFailure(statusCode,"Server responded with code: " + statusCode);
                }
            } catch (IOException e) {
                Log.e("userApi", "Error: " + e.getMessage());
                e.printStackTrace();
                callback.onFailure(-1, "Connection failed: " + e.getMessage());

            }
        }).start(); // Run in background thread
    }

    public static void sendLoginToServer(String serverUrl, String username, String password, ServerResponseCallback callback) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        // Manually create a JSON string or use a map
        Map<String, String> loginData = new HashMap<>();
        loginData.put("userName", username);
        loginData.put("password", password);

        String jsonBody = gson.toJson(loginData);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(serverUrl)
                .post(body)
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    int statusCode = response.code();
                    Log.i("userApi", "Login data sent successfully: " + statusCode);
                    callback.onSuccess(response.body().string());
                } else {
                    int statusCode = response.code();
                    Log.e("userApi", "Login failed: " + statusCode);
                    callback.onFailure(statusCode, "Server responded with code: " + statusCode);
                }
            } catch (IOException e) {
                Log.e("userApi", "Error: " + e.getMessage());
                e.printStackTrace();
                callback.onFailure(-1, "Connection failed: " + e.getMessage());

            }
        }).start();
    }
    public static void sendSessionIdAndUuidToServer(String serverUrl, String uuid, String sessionId, ServerResponseCallback callback) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        // Manually create a JSON string or use a map
        Map<String, String> credentialsData = new HashMap<>();
        credentialsData.put("uuid", uuid);
        credentialsData.put("sessionId", sessionId);

        String jsonBody = gson.toJson(credentialsData);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(serverUrl)
                .post(body)
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    int statusCode = response.code();
                    Log.i("userApi", "Credentials data sent successfully: " + statusCode);
                    callback.onSuccess(response.body().string());
                } else {
                    int statusCode = response.code();
                    Log.e("userApi", "Sending credentials failed: " + statusCode);
                    callback.onFailure(statusCode, "Server responded with code: " + statusCode);
                }
            } catch (IOException e) {
                Log.e("userApi", "Error: " + e.getMessage());
                e.printStackTrace();
                callback.onFailure(0, "Failed to connect to server: " + e.getMessage());

            }
        }).start();
    }

}
