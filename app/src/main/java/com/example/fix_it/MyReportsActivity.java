package com.example.fix_it;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fix_it.api.ReportApi;
import com.example.fix_it.api_dto.User;
import com.example.fix_it.api_dto.UserManager;
import com.example.fix_it.helper.BaseActivity;
import com.example.fix_it.helper.ReportAdapter;

import java.util.ArrayList;

public class MyReportsActivity extends BaseActivity {

    private User user;
    private RecyclerView recyclerView;
    private ReportAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_reports);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.reportsRecyclerView);

        // Set up adapter with empty list initially
        adapter = new ReportAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = UserManager.getInstance().getUser();
        if (user == null) {
            // TODO: Add implementation of the session id management
            finish();
            return;
        }

        String serverUrl = "http://10.100.102.8:5000/get_reports";
        String userUuid = user.getUuid();
        String sessionId = user.getSessionID();

        // Fetch reports from server
        ReportApi.getReportsFromServer(serverUrl, userUuid, sessionId, reports -> {
            adapter.updateData(reports); // Update the adapter with fetched reports
        });



    }
}