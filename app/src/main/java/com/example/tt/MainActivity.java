package com.example.tt;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.tt.data.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    ImageButton StartButton;
    ImageButton ProfileButton;
    Button InfoButton;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = User.getInstance();
        new AlertDialog.Builder(this).setTitle(String.valueOf(user.getUser_location().latitude) + ", " + String.valueOf(user.getUser_location().longitude)).setPositiveButton("ok", null).show();

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        Menu menu = nav_view.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:

                        break;

                    case R.id.navigation_moim:
                        startActivity(new Intent(getApplicationContext(), moim.class));
                        break;

                    case R.id.navigation_review:
                        startActivity(new Intent(getApplicationContext(), review.class));
                        break;
                }
                return false;
            }
        });
        StartButton = findViewById(R.id.StartButton);
        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "GO!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), CardInfo.class));
            }
        });


        ProfileButton = findViewById(R.id.profileButton);
        ProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(Gravity.RIGHT)) drawer.openDrawer(Gravity.RIGHT);
                else drawer.closeDrawer(GravityCompat.END);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_pre_cat:
                startActivity(new Intent(getApplicationContext(), pre_cat.class));
                break;
            case R.id.nav_review:
                startActivity(new Intent(getApplicationContext(), review_check.class));
                break;
            case R.id.nav_profile:
                startActivity(new Intent(getApplicationContext(), profile_detail.class));
                break;
            case R.id.nav_tip:
                startActivity(new Intent(getApplicationContext(), info.class));
                break;
        }
        return true;
    }
}
