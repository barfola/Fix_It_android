package com.example.fix_it;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fix_it.api.ReportApi;
import com.example.fix_it.api_dto.UserManager;
import com.example.fix_it.helper.AndroidUtils;
import com.example.fix_it.helper.BaseActivity;
import com.example.fix_it.api_dto.User;
import com.example.fix_it.api_dto.ProblemReport;

import com.google.android.material.textfield.TextInputEditText;


public class ReportDetailActivity extends BaseActivity {


    private ImageView imageView;
    private Button deleteButton;
    private ProblemReport problemReport;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

         imageView = findViewById(R.id.imageView);
         deleteButton = findViewById(R.id.deleteButton);



        TextView textDescription = findViewById(R.id.textDescription);
        TextView textRole = findViewById(R.id.textRole);
        TextView textLocation = findViewById(R.id.textLocation);
        TextView textReportType = findViewById(R.id.textReportType);

        user = UserManager.getInstance().getUser();
        if (user == null) {
            finish();
            return;
        }

        problemReport = getIntent().getParcelableExtra("problemReport");
        if (problemReport == null) {
            Toast.makeText(this, "Missing report data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        textDescription.setText(AndroidUtils.replaceUnderscoreWithSpace("Description: " + problemReport.getDescription()));
        textRole.setText(AndroidUtils.replaceUnderscoreWithSpace("Role: " + problemReport.getRole().name()));
        textLocation.setText(AndroidUtils.replaceUnderscoreWithSpace("Location: " + problemReport.getLocation().name()));
        textReportType.setText(AndroidUtils.replaceUnderscoreWithSpace("Report Type: " + problemReport.getReportType().name()));


        if (problemReport.getImage() != null) {
            imageView.setImageBitmap(AndroidUtils.convertBase64ToBitmap(problemReport.getImage()));
        }



        deleteButton.setOnClickListener(v -> {
            if (problemReport == null || user == null) {
                Toast.makeText(this, "Missing data for deletion", Toast.LENGTH_SHORT).show();
                return;
            }

            ReportApi.deleteReportFromServer(
                    "http://10.100.102.12:5000/delete_report", // your server base URL
                    problemReport.getUuid(),
                    user.getSessionID(),
                    user.getUuid(),
                    () -> {
                        Toast.makeText(this, "Report deleted successfully", Toast.LENGTH_SHORT).show();
                        finish(); // Close this activity and return to the previous one
                    }
            );
        });





































    }
}