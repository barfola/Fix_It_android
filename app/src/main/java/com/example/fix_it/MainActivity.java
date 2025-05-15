package com.example.fix_it;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fix_it.api.ReportApi;
import com.example.fix_it.api_dto.ProblemReport;
import com.example.fix_it.api_dto.User;


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

        //db_utils db = db_utils.getInstance();
        //usersApi api=new usersApi();
        //Users users=api.getUser("asdad");

//        Users users=new Users();
        //User user = new User("yoni", "1213", 1, "1");
//        String Userid = user.getUuid();
//        String sessionID = user.getSessionID();
        //ProblemReport report = new ProblemReport("takala bath", ProblemReport.Role.STUDENT, ProblemReport.Location.BATHROOM, ProblemReport.ReportType.BROKEN_OBJECT, user);
        //db.saveDataToFile(this,"user.data",users.getUserId());
        //String userId= db.readDataFromFile(this,"user.data");
        //Log.i("userData",userId);
        //usersApi userapi = new usersApi();
        //userapi.sendUserToServer("http://10.100.102.7:5000", users);
        //ReportApi reportApi = new ReportApi();
        //reportApi.sendReportToServer("http://10.100.102.8:5000", report);
        //Log.i("yehonatan bar", "print");
        //userapi.sendUserToServer("http://10.100.102.4:5000", user);
        //reportApi.getReportFromServer("http://10.100.102.4:5000");

    }
}
