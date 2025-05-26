package com.example.fix_it.api;

import android.util.Base64;
import android.util.Log;

import com.example.fix_it.api_dto.ProblemReport;
import com.example.fix_it.api_dto.User;
import com.example.fix_it.api_dto.UserManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.Handler;
import android.os.Looper;

public class ReportApi {

    public ReportApi(){};

    public static void sendReportToServer(String serverUrl, ProblemReport problemReport) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        // Create the report data map
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("uuid", problemReport.getUuid());
        reportData.put("description", problemReport.getDescription());
        reportData.put("role", problemReport.getRole().ordinal());
        reportData.put("location", problemReport.getLocation().ordinal());
        reportData.put("reportType", problemReport.getReportType().ordinal());
        reportData.put("image", problemReport.getImage());

        // Create a nested map for the user details
        Map<String, String> userData = new HashMap<>();
        userData.put("userUuid", problemReport.getUser().getUuid());
        userData.put("sessionID", problemReport.getUser().getSessionID());


        reportData.put("user", userData);

        // Convert to JSON
        String jsonBody = gson.toJson(reportData);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(serverUrl) // e.g., http://192.168.1.100:5000/receive_report
                .post(body)
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.i("ReportApi", "Report data sent successfully: " + response.code());
                } else {
                    Log.e("ReportApi", "Failed to send report: " + response.code());
                }
            } catch (IOException e) {
                Log.e("ReportApi", "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start(); // Run in background thread
    }


    public static void getReportsFromServer(String serverUrl) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(serverUrl + "/get_report") // e.g., http://10.0.2.2:5000/get_report
                .get()
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.i("reportApi", "Received report data: " + responseData);

                    // Parse the response using Gson
                    Gson gson = new Gson();
                    ProblemReport problemReport = gson.fromJson(responseData, ProblemReport.class);

                    // Use the data (e.g., update the UI)
                    Log.i("reportApi", "Report: " + problemReport.getDescription() + " in " + problemReport.getLocation());
                } else {
                    Log.e("reportApi", "Failed to get report: " + response.code());
                }
            } catch (IOException e) {
                Log.e("reportApi", "Error: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public static void getReportsFromServer(String serverUrl, String userUuid, String sessionId, Consumer<List<ProblemReport>> callback) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(serverUrl).newBuilder();
        urlBuilder.addQueryParameter("userUuid", userUuid);
        urlBuilder.addQueryParameter("sessionID", sessionId);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .get()
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseData = response.body().string();

                    // Parse JSON array
                    JSONArray jsonArray = new JSONArray(responseData);
                    List<ProblemReport> reports = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        String uuid = obj.getString("uuid");
                        String description = obj.getString("description");

                        int roleOrdinal = obj.getInt("role");
                        int locationOrdinal = obj.getInt("location");
                        int reportTypeOrdinal = obj.getInt("reportType");


                        String imageBase64 = null;
                        if (!obj.isNull("image")) {
                            imageBase64 = obj.getString("image");
                        }

                        ProblemReport.Role role = ProblemReport.getRoleByOrdinal(roleOrdinal);
                        ProblemReport.Location location = ProblemReport.getLocationByOrdinal(locationOrdinal);
                        ProblemReport.ReportType reportType = ProblemReport.getReportTypeByOrdinal(reportTypeOrdinal);

                        User user = UserManager.getInstance().getUser();
                        ProblemReport report = new ProblemReport(description, role, location, reportType, imageBase64, user);
                        report.setUuid(uuid);
                        reports.add(report);
                    }

                    // Send the result back to main thread via callback
                    new Handler(Looper.getMainLooper()).post(() -> {
                        callback.accept(reports);
                    });

                } else {
                    Log.e("ReportApi", "Failed to fetch reports: " + response.code());
                }
            } catch (Exception e) {
                Log.e("ReportApi", "Error fetching reports: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }



}
