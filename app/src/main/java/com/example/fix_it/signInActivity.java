package com.example.fix_it;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fix_it.api.ApiConfiguration;
import com.example.fix_it.api.ServerResponseCallback;
import com.example.fix_it.api.usersApi;
import com.example.fix_it.api_dto.User;
import com.example.fix_it.db.db_utils;
import com.example.fix_it.helper.AndroidUtils;
import com.example.fix_it.helper.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class signInActivity extends AppCompatActivity {
    TextView btn;
    Button btnSignIn, btnSetUpIp;
    ApiConfiguration apiConfiguration;
    private EditText inputUserName, inputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        apiConfiguration = ApiConfiguration.getInstance();
        inputPassword = findViewById(R.id.inputPassword);
        inputUserName = findViewById(R.id.inputUserName);
        btn = findViewById(R.id.textViewLogIn);
        btnSetUpIp = findViewById(R.id.buttonSetupIP);
        btnSignIn = findViewById(R.id.buttonSignIn);

        btnSetUpIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace SetUpIPActivity.class with the activity you want to open
                Intent intent = new Intent(signInActivity.this, SetUpIpActivity.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signInActivity.this,LoginActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCredentials();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }



    private void checkCredentials() {
        Log.i("the ip", "f"+apiConfiguration.getSessionId());
        String userName = inputUserName.getText().toString();
        String password = inputPassword.getText().toString();

        if (userName.isEmpty() || userName.length()<7){
            showError(inputUserName, "Your username is not valid");
        }
        else if (password.isEmpty() ||  password.length() < 7){
            showError(inputPassword, "Password length must be 7 characters");
        }
        else if (apiConfiguration.getSignInUrl() == null){
            Toast.makeText(signInActivity.this, "You have to set up ip address", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String inputUserNameStr = inputUserName.getText().toString();
            String inputPasswordStr = inputPassword.getText().toString();
            User user = new User(inputUserNameStr, inputPasswordStr);
            AndroidUtils.logUserDetails(user);
            usersApi.sendUserToServer(apiConfiguration.getSignInUrl(), user, new ServerResponseCallback() {
                @Override
                public void onSuccess(String responseBody) {
                    Log.i("response body", responseBody);
                    String sessionId = "";
                    try {
                        JSONObject json = new JSONObject(responseBody);
                         sessionId = json.getString("sessionId");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    user.setSessionID(sessionId);

                    AndroidUtils.logUserDetails(user);

                    apiConfiguration.setSessionId(signInActivity.this, user.getSessionID());
                    apiConfiguration.setUserUUID(signInActivity.this, user.getUuid());


                    Intent intent = new Intent(signInActivity.this, ProblemReportActivity.class);
                    startActivity(intent);

                }

                @Override
                public void onFailure(int statusCode,String errorMessage) {
                    Log.i("fail", "Status: " + statusCode + ", Error: " + errorMessage);
                    runOnUiThread(() -> {
                        String message;

                        if (statusCode == 401) {
                            message = "Invalid username";
                        } else if (statusCode == -1) {
                            message = "Unable to connect to the server, ip is not correct";
                        } else {
                            message = "Error: " + errorMessage;
                        }
                        Toast.makeText(signInActivity.this, message, Toast.LENGTH_SHORT).show();


                    });
                }
            });
        }

    }

    public void showError(EditText input, String errorMessage){
        input.setError(errorMessage);
        input.requestFocus();
    }
}




































