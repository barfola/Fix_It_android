package com.example.fix_it;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fix_it.api.ApiConfiguration;
import com.example.fix_it.api.ServerResponseCallback;
import com.example.fix_it.api.usersApi;
import com.example.fix_it.api_dto.User;
import com.example.fix_it.api_dto.UserManager;
import com.example.fix_it.db.db_utils;
import com.example.fix_it.helper.AndroidUtils;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiConfiguration.init(this);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ApiConfiguration apiConfiguration = ApiConfiguration.getInstance();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Log.d("Handler", "This runs after a 3-second delay");
            String serverIP = apiConfiguration.getServerIP();
            String uuid = apiConfiguration.getUserUUID();
            String sessionId = apiConfiguration.getSessionId();

            Log.i(TAG, "serverIP: " + (serverIP != null ? serverIP : "null"));
            Log.i(TAG, "uuid: " + (uuid != null ? uuid : "null"));
            Log.i(TAG, "sessionId: " + (sessionId != null ? sessionId : "null"));

            if (TextUtils.isEmpty(uuid) || TextUtils.isEmpty(sessionId) || TextUtils.isEmpty(serverIP)) {
                Log.i(TAG, "Missing credentials or serverIP. Redirecting to sign-in.");
                navigateTo(signInActivity.class);
            } else {

                usersApi.sendSessionIdAndUuidToServer(apiConfiguration.getInitialCredentialsUrl(), apiConfiguration.getUserUUID(), apiConfiguration.getSessionId(), new ServerResponseCallback() {
                    @Override
                    public void onSuccess(String responseBody) {
                        Log.i("response body", responseBody);
                        User user = new User();
                        try {
                            user.mapJson(responseBody);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        AndroidUtils.logUserDetails(user);

                        UserManager.getInstance().setUser(user);
                        Intent intent = new Intent(MainActivity.this, ProblemReportActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onFailure(int statusCode, String errorMessage) {
                        Log.i(TAG, "API request failed: " + errorMessage);
                        if (statusCode == 401) {
                            navigateTo(LoginActivity.class);
                        } else if(statusCode != 0){
                            // Optional: handle other status codes
                            Log.w(TAG, "Unexpected status code: " + statusCode);
                            navigateTo(signInActivity.class); // fallback
                        }
                        else {
                            Log.w(TAG, "Error with ip: " + statusCode);
                            navigateTo(signInActivity.class); // fallback
                        }
                    }
                });

            }


        }, 3000);

    }

    private void navigateTo(Class<?> targetActivity) {
        Intent intent = new Intent(MainActivity.this, targetActivity);
        startActivity(intent);
        finish();
    }
}
