package com.example.fix_it.api;

import android.util.Log;

import com.example.fix_it.api_dto.ProblemReport;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ReportApi {

    public ReportApi(){};

    public void sendReportToServer(String serverUrl, ProblemReport problemReport) {
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String jsonBody = gson.toJson(problemReport);

        RequestBody body = RequestBody.create(jsonBody, JSON);
        Request request = new Request.Builder()
                .url(serverUrl + "/receive_report") // e.g., http://192.168.1.100:5000/receive_user
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

    public void getReportFromServer(String serverUrl) {
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

}
