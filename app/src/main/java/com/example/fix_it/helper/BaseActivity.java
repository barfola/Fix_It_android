package com.example.fix_it.helper;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.LinearLayout;
//import android.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.fix_it.AllReportsActivity;
import com.example.fix_it.LoginActivity;
import com.example.fix_it.MyReportsActivity;
import com.example.fix_it.ProblemReportActivity;
import com.example.fix_it.R;
import com.example.fix_it.api_dto.UserManager;
import com.example.fix_it.signInActivity;
import com.google.android.material.navigation.NavigationView;

public class BaseActivity extends AppCompatActivity {

    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;
    protected Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        // Inflate the base layout
        DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        LinearLayout contentFrame = fullLayout.findViewById(R.id.content_frame);

        // Inflate the child layout into the content frame
        getLayoutInflater().inflate(layoutResID, contentFrame, true);
        super.setContentView(fullLayout);

        drawerLayout = fullLayout.findViewById(R.id.drawer_layout);
        navigationView = fullLayout.findViewById(R.id.nav_view);

        setupToolbar();
        setupDrawer();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            toolbar = new Toolbar(this);
            toolbar.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            ((LinearLayout) findViewById(R.id.content_frame)).addView(toolbar, 0);
        }
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
    }

    private void setupDrawer() {
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_all_reports).setVisible(UserManager.getInstance().getUser() != null &&
                UserManager.getInstance().getUser().getAdminLevel() == 1);

        navigationView.setNavigationItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_login) {
                startActivity(new Intent(this, LoginActivity.class));
            }
            else if (id == R.id.nav_signin) {
                startActivity(new Intent(this, signInActivity.class));
            }
            else if (id == R.id.nav_problem_report) {
                startActivity(new Intent(this, ProblemReportActivity.class));
            }
            else if (id == R.id.nav_my_reports) {
                startActivity(new Intent(this, MyReportsActivity.class));
                //startActivity(new Intent(this, ProblemReport.class));
            }
            else if (id == R.id.nav_all_reports) {
                startActivity(new Intent(this, AllReportsActivity.class)); // Your admin-only page
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}
