package com.example.fix_it.api;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.fix_it.LoginActivity;
import com.example.fix_it.api_dto.ProblemReport;
import com.example.fix_it.api_dto.User;
import com.example.fix_it.api_dto.UserManager;
import com.example.fix_it.helper.AndroidUtils;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ReportApi {

    public ReportApi(){};

    public static void sendReportToServer(String serverUrl, ProblemReport problemReport, Context context, ServerResponseCallback callback) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        HttpUrl.Builder urlBuilder = HttpUrl.parse(serverUrl).newBuilder();
        urlBuilder.addQueryParameter("userUuid", problemReport.getUser().getUuid().trim());
        urlBuilder.addQueryParameter("sessionID", problemReport.getUser().getSessionID().trim());

        // Create the report data map
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("uuid", problemReport.getUuid());
        reportData.put("description", problemReport.getDescription());
        reportData.put("role", problemReport.getRole().ordinal());
        reportData.put("location", problemReport.getLocation().ordinal());
        reportData.put("reportType", problemReport.getReportType().ordinal());
        reportData.put("image", problemReport.getImage());

        String jsonBody = gson.toJson(reportData);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .post(body)
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                String responseBody = response.body() != null ? response.body().string() : "";

                if (response.isSuccessful()) {
                    Log.i("ReportApi", "Report data sent successfully: " + response.code());
                    new Handler(Looper.getMainLooper()).post(() -> callback.onSuccess(responseBody));
                } else {
                    Log.e("ReportApi", "Failed to send report: " + response.code());
                    new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(response.code(), responseBody));

                    if (response.code() == 401) {
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(context, "Session expired. Please log in again.", Toast.LENGTH_LONG).show();
                            UserManager.getInstance().clearUser();

                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        });
                    }
                }
            } catch (IOException e) {
                Log.e("ReportApi", "Error: " + e.getMessage());
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> callback.onFailure(500, e.getMessage()));
            }
        }).start();
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



    public static void getReportsFromServer(String serverUrl, String userUuid, String sessionId, Context context, Consumer<List<ProblemReport>> callback) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

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

                    // Send the result back to main thre\ad via callback
                    new Handler(Looper.getMainLooper()).post(() -> {
                        callback.accept(reports);
                    });

                } else {
                    Log.e("ReportApi", "Failed to fetch reports: " + response.code());
                    if (response.code() == 401) {
                        // Session expired, go back to login
                        new Handler(Looper.getMainLooper()).post(() -> {
                            Toast.makeText(context, "Session expired. Please log in again.", Toast.LENGTH_LONG).show();

                            // Clear user session
                            UserManager.getInstance().clearUser(); // You should implement this

                            Intent intent = new Intent(context, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            context.startActivity(intent);
                        });
                    }
                }
            } catch (Exception e) {
                Log.e("ReportApi", "Error fetching reports: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }


    public static void deleteReportFromServer(String serverUrl, String reportUuid, String sessionId, String userUuid, Runnable onSuccess) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = HttpUrl.parse(serverUrl).newBuilder()
                .addQueryParameter("uuid", reportUuid.trim())
                .addQueryParameter("sessionId", sessionId.trim())
                .addQueryParameter("userUuid", userUuid.trim())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ReportApi", "Failed to delete report: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.i("ReportApi", "Report deleted successfully");
                    new Handler(Looper.getMainLooper()).post(onSuccess);
                } else {
                    Log.e("ReportApi", "Failed to delete report: " + response.code());
                }
            }
        });
    }

    public static void getImageBase64ByUuid(String serverUrl, String imageUuid, Context context, Consumer<String> callback) {
        OkHttpClient client = new OkHttpClient();

        HttpUrl url = HttpUrl.parse(serverUrl).newBuilder()
                .addQueryParameter("uuid", imageUuid.trim())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        new Thread(() -> {
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String jsonString = response.body().string();
                    response.close();

                    JSONObject json = new JSONObject(jsonString);
                    String base64Image = json.optString("image", "");

                    new Handler(Looper.getMainLooper()).post(() -> {
                        callback.accept(base64Image);
                    });
                } else {

                    Log.e("ReportApi", "Failed to fetch image: " + response.code());
                    response.close();
                    new Handler(Looper.getMainLooper()).post(() -> callback.accept(null));

                }
            } catch (Exception e) {
                Log.e("ReportApi", "Error fetching image: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }










}
