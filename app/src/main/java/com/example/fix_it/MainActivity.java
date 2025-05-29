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

import com.example.fix_it.api.ServerResponseCallback;
import com.example.fix_it.api.usersApi;
import com.example.fix_it.api_dto.User;
import com.example.fix_it.api_dto.UserManager;
import com.example.fix_it.db.db_utils;
import com.example.fix_it.helper.AndroidUtils;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Log.d("Handler", "This runs after a 3-second delay");
            String uuid = db_utils.readDataFromFile(MainActivity.this, "user.uuid");
            String sessionId = db_utils.readDataFromFile(MainActivity.this, "user.sessionId");

            if (TextUtils.isEmpty(uuid) || TextUtils.isEmpty(sessionId)) {
                Log.i("first if", "first if");
                navigateTo(signInActivity.class);
            } else {
                usersApi.sendSessionIdAndUuidToServer("http://10.100.102.12:5000/initialCredentials", uuid, sessionId, new ServerResponseCallback() {
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
                        Log.i("in else on failure","failure");

                        if(statusCode == 401){
                            navigateTo(LoginActivity.class);
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
