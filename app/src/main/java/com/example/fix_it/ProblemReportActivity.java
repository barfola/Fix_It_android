package com.example.fix_it;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import android.graphics.Bitmap;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fix_it.api.ReportApi;
import com.example.fix_it.api_dto.ProblemReport;
import com.example.fix_it.api_dto.User;
import com.example.fix_it.api_dto.UserManager;
import com.example.fix_it.helper.AndroidUtils;
import com.example.fix_it.helper.BaseActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.example.fix_it.api_dto.ProblemReport.ReportType;
import com.example.fix_it.api_dto.ProblemReport.Location;
import com.example.fix_it.api_dto.ProblemReport.Role;




public class ProblemReportActivity extends BaseActivity {

    Button btnCapture;
    ImageView imageView;
    String [] roles = {"Teacher", "Student", "School crew", "Other"};
    String [] location = {"Bathroom", "Yard", "Classroom", "Teacher lounge", "Hallway"};
    String [] reportType = {"Lost key", "Lamp burned out", "Batteries", "Air conditioner", "Projector", "Broken object"};

    AutoCompleteTextView autoCompleteRole, autoCompleteLocation, autoCompleteReportType;
    ArrayAdapter<String> adapterRole, adapterLocation, adapterReportType;
    TextInputEditText editTextProblemTitle;

    private ActivityResultLauncher<Intent> cameraLauncher;
    private Bitmap captureImage = null;
    private User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_problem_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = UserManager.getInstance().getUser();
        if (user == null) {
            // TODO: Add implementation of the session id management
            finish();
            return;
        }

        AndroidUtils.logUserDetails(user);
        btnCapture = findViewById(R.id.btnCapture);
        imageView = findViewById(R.id.imageView);
        autoCompleteRole = findViewById(R.id.autoCompleteRole);
        autoCompleteLocation = findViewById(R.id.autoCompleteLocation);
        autoCompleteReportType = findViewById(R.id.autoCompleteReportType);
        editTextProblemTitle = findViewById(R.id.editTextProblemTitle);

        adapterRole = new ArrayAdapter<String>(this, R.layout.list_item, roles);
        adapterLocation = new ArrayAdapter<String>(this, R.layout.list_item, location);
        adapterReportType = new ArrayAdapter<String>(this, R.layout.list_item, reportType);

        autoCompleteRole.setAdapter(adapterRole);
        autoCompleteLocation.setAdapter(adapterLocation);
        autoCompleteReportType.setAdapter(adapterReportType);

        autoCompleteRole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String role = parent.getItemAtPosition(position).toString();
                Toast.makeText(ProblemReportActivity.this, "Role"+role, Toast
                        .LENGTH_SHORT).show();
            }
        });

        autoCompleteLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String location = parent.getItemAtPosition(position).toString();
                Toast.makeText(ProblemReportActivity.this, "Location"+location, Toast
                        .LENGTH_SHORT).show();
            }
        });

        autoCompleteReportType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String reportType = parent.getItemAtPosition(position).toString();
                Toast.makeText(ProblemReportActivity.this, "Report type"+reportType, Toast
                        .LENGTH_SHORT).show();
            }
        });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            captureImage = (Bitmap) data.getExtras().get("data");
                            Log.i("CameraCapture", "Bitmap received: " + (captureImage != null));

                            imageView.setImageBitmap(captureImage);
                        }
                    }
                }
        );


        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraLauncher.launch(intent);
            }
        });

        Button btnSubmit = findViewById(R.id.submitButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmit();
            }
        });



    }

    private void validateAndSubmit() {
        String role = autoCompleteRole.getText().toString().trim();
        String location = autoCompleteLocation.getText().toString().trim();
        String reportType = autoCompleteReportType.getText().toString().trim();
        String description = editTextProblemTitle.getText().toString().trim();
        Log.i("role", role);

        if (role.isEmpty() || location.isEmpty() || reportType.isEmpty() || description.isEmpty()) {
            Toast.makeText(ProblemReportActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        ProblemReport problemReport;
        Role userRole = ProblemReport.getRoleFromString(role);
        Location userLocation = ProblemReport.getLocationFromString(location);
        ReportType userReportType = ProblemReport.getReportTypeFromString(reportType);

        if (captureImage == null) {

            problemReport = new ProblemReport(description, userRole, userLocation, userReportType, user);
        }
        else{
            Log.i("validateAndSubmit", "Creating report WITH image");

            String imageBytes = AndroidUtils.convertBitmapToBase64(captureImage);
            problemReport = new ProblemReport(description, userRole, userLocation, userReportType, imageBytes,user);

        }

        ReportApi.sendReportToServer("http://10.100.102.12:5000/problemReport", problemReport, ProblemReportActivity.this);

        Log.i("fill all", "you fill all the fields");
    }



}