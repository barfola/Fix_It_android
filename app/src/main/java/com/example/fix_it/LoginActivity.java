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

import com.example.fix_it.api.ServerResponseCallback;
import com.example.fix_it.api.usersApi;
import com.example.fix_it.api_dto.User;
import com.example.fix_it.db.db_utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    TextView btn;
    Button btnLogIn;
    private EditText inputUserName, inputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        inputPassword = findViewById(R.id.inputPassword);
        inputUserName = findViewById(R.id.inputUserName);
        btnLogIn = findViewById(R.id.buttonLogin);
        btn = findViewById(R.id.textViewSignIn);

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
        else {
            String inputUserNameStr = inputUserName.getText().toString();
            String inputPasswordStr = inputPassword.getText().toString();
            User user = new User(inputUserNameStr, inputPasswordStr);
            String fileData = db_utils.readDataFromFile(LoginActivity.this, "user.data");
            Log.i("filedatabefore", fileData);


            usersApi.sendLoginToServer("http://10.100.102.8:5000/login", inputUserNameStr, inputPasswordStr, new ServerResponseCallback() {
                @Override
                public void onSuccess(String responseBody) {
                    Log.i("response body", responseBody);
                    User user = new User();
                    try {
                        user.mapJson(responseBody);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    Log.i("session", user.getSessionID());
                    Log.i("uuid", user.getUuid());
                    Log.i("password", user.getPassword());
                    Log.i("hashPassword", user.getHashPassword());
                    Log.i("adminLevel", ""+user.getAdminLevel());
                    Log.i("username", user.getUserName());
                    Log.i("message", user.getMessage());
                    db_utils.saveDataToFile(LoginActivity.this, "user.uuid", user.getUuid());
                    db_utils.saveDataToFile(LoginActivity.this, "user.sessionId", user.getSessionID());
                    String fileData = db_utils.readDataFromFile(LoginActivity.this, "user.uuid");
                    File file = new File(getFilesDir(), "user.data");


                    assert fileData != null;
                    Log.i("file data", fileData);

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
            //Toast.makeText(this, "Call registration method", Toast.LENGTH_SHORT).show();
        }

    }

    public void showError(EditText input, String errorMessage){
        input.setError(errorMessage);
        input.requestFocus();
    }
}