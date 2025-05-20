package com.example.fix_it;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProblemReport extends AppCompatActivity {

    Button btnCapture;
    ImageView imageView;
    String [] roles = {"Teacher", "Student", "School crew", "Other"};
    String [] location = {"Bathroom", "Yard", "Classroom", "Teacher lounge", "Hallway"};
    String [] reportType = {"Lost key", "Lamp burned out", "Batteries", "Air conditioner", "Projector", "Broken object"};

    AutoCompleteTextView autoCompleteRole, autoCompleteLocation, autoCompleteReportType;
    ArrayAdapter<String> adapterRole, adapterLocation, adapterReportType;
    private ActivityResultLauncher<Intent> cameraLauncher;


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
        btnCapture = findViewById(R.id.btnCapture);
        imageView = findViewById(R.id.imageView);
        autoCompleteRole = findViewById(R.id.autoCompleteRole);
        autoCompleteLocation = findViewById(R.id.autoCompleteLocation);
        autoCompleteReportType = findViewById(R.id.autoCompleteReportType);

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
                Toast.makeText(ProblemReport.this, "Role"+role, Toast
                        .LENGTH_SHORT).show();
            }
        });

        autoCompleteLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String location = parent.getItemAtPosition(position).toString();
                Toast.makeText(ProblemReport.this, "Location"+location, Toast
                        .LENGTH_SHORT).show();
            }
        });

        autoCompleteReportType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String reportType = parent.getItemAtPosition(position).toString();
                Toast.makeText(ProblemReport.this, "Report type"+reportType, Toast
                        .LENGTH_SHORT).show();
            }
        });

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                            imageView.setImageBitmap(imageBitmap);
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


    }


}