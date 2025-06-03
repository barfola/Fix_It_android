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
import com.example.fix_it.api_dto.UserManager;
import com.example.fix_it.db.db_utils;
import com.example.fix_it.helper.AndroidUtils;
import com.example.fix_it.helper.BaseActivity;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {
    ApiConfiguration apiConfiguration;
    TextView btn;
    Button btnLogIn, btnSetUpIp;
    private EditText inputUserName, inputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        apiConfiguration = ApiConfiguration.getInstance();
        inputPassword = findViewById(R.id.inputPassword);
        inputUserName = findViewById(R.id.inputUserName);
        btnLogIn = findViewById(R.id.buttonLogin);
        btnSetUpIp = findViewById(R.id.buttonSetupIP);
        btn = findViewById(R.id.textViewSignIn);

        btnSetUpIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SetUpIpActivity.class);
                startActivity(intent);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,signInActivity.class));
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
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
        String userName = inputUserName.getText().toString();
        String password = inputPassword.getText().toString();

        if (userName.isEmpty() || userName.length()<7){
            showError(inputUserName, "Your username is not valid");
        }
        else if (password.isEmpty() ||  password.length() < 7){
            showError(inputPassword, "Password length must be 7 characters");
        }
        else if(apiConfiguration.getLoginUrl() == null){
            Toast.makeText(LoginActivity.this, "You have to set up ip address", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String inputUserNameStr = inputUserName.getText().toString();
            String inputPasswordStr = inputPassword.getText().toString();
            User user = new User(inputUserNameStr, inputPasswordStr);
            String fileData = db_utils.readDataFromFile(LoginActivity.this, "user.data");
            Log.i("filedatabefore", fileData);


            usersApi.sendLoginToServer(apiConfiguration.getLoginUrl(), inputUserNameStr, inputPasswordStr, new ServerResponseCallback() {
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


                    apiConfiguration.setSessionId(LoginActivity.this, user.getSessionID());
                    apiConfiguration.setUserUUID(LoginActivity.this, user.getUuid());

                    String fileData = db_utils.readDataFromFile(LoginActivity.this, "user.uuid");
                    assert fileData != null;

                    Log.i("file data", fileData);

                    UserManager.getInstance().setUser(user); // Save the user globally
                    Intent intent = new Intent(LoginActivity.this, ProblemReportActivity.class);
                    startActivity(intent);

                }

                @Override
                public void onFailure(int statusCode,String errorMessage) {
                    Log.i("fail", "Status: " + statusCode + ", Error: " + errorMessage);
                    runOnUiThread(() -> {
                        if (statusCode == 401) {
                            Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
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