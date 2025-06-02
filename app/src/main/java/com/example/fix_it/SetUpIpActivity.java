package com.example.fix_it;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fix_it.api.ApiConfiguration;
import com.example.fix_it.helper.AndroidUtils;

import java.util.regex.Pattern;

public class SetUpIpActivity extends AppCompatActivity {

    private EditText editTextIP;
    private Button buttonSet, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_up_ip);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonBack = findViewById(R.id.buttonBack);
        buttonSet = findViewById(R.id.buttonSet);
        EditText editTextIP = findViewById(R.id.editTextIP);


        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close current activity and return to the previous one
            }
        });

        buttonSet.setOnClickListener(v -> {
            String ip = editTextIP.getText().toString().trim();
            if (isValidIP(ip)) {
                ApiConfiguration.getInstance().setServerIP(this, ip);

                Toast.makeText(this, "Valid IP: " + ip, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Invalid IP address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidIP(String ip) {
        if (ip == null || ip.isEmpty()){
            return false;
        };

        String[] parts = ip.split("\\.");
        if (parts.length != 4){
            return false;
        };

        for (String part : parts) {
            try {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255){
                    return false;
                };
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }
}