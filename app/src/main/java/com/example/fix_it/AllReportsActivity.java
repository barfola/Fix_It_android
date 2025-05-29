package com.example.fix_it;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fix_it.api.ReportApi;
import com.example.fix_it.api_dto.ProblemReport;
import com.example.fix_it.api_dto.User;
import com.example.fix_it.api_dto.UserManager;
import com.example.fix_it.helper.AdminReportAdapter;
import com.example.fix_it.helper.BaseActivity;

import java.util.ArrayList;

public class AllReportsActivity extends BaseActivity {

    private User user;
    private RecyclerView recyclerView;
    private AdminReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all_reports);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        recyclerView = findViewById(R.id.AllReportsRecyclerView);

        // Initialize adapter with empty list and context
        adapter = new AdminReportAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        user = UserManager.getInstance().getUser();
        if (user == null) {
            // TODO: Add implementation of the session id management
            finish();
            return;
        }

        String serverUrl = "http://10.100.102.12:5000/get_all_reports"; // Replace with your admin endpoint
        String userUuid = user.getUuid();
        String sessionId = user.getSessionID();

        // Example API call to get all reports for admin view - you should implement this method in ReportApi
        // Fetch reports from server
        ReportApi.getReportsFromServer(serverUrl, userUuid, sessionId, AllReportsActivity.this,reports -> {
            adapter.updateData(reports); // Update the adapter with fetched reports
        });



    }
}
