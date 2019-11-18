package com.example.tt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.widget.ImageButton;

import com.example.tt.model.Data;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class moim extends AppCompatActivity {
    private PopupWindow mPopupWindow;
    ImageButton profileButton;
    ImageButton backButton;
    ImageButton moimcreateButton;
    Spinner spinner;
    private int num;
    private JSONObject cat_json = null;
    private JSONArray cat_arr = null;
    final url_json read = new url_json();
    final String url = "http://52.79.125.108/api/category/";
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim);

        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        Menu menu = nav_view.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;

                    case R.id.navigation_moim:

                        break;

                    case R.id.navigation_review:
                        startActivity(new Intent(getApplicationContext(), review.class));
                        break;
                }
                return false;
            }
        });
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.pager);

        final TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Toast.makeText(getApplicationContext(), tab.getText(), Toast.LENGTH_SHORT).show();
                        setTab(0);
                        break;
                    case 1:
                        Toast.makeText(getApplicationContext(), tab.getText(), Toast.LENGTH_SHORT).show();
                        setTab(1);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        moimcreateButton = findViewById(R.id.moimcreateButton);
        moimcreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp = getTab();
                if (tmp == 0) {


                    View popupView = getLayoutInflater().inflate(R.layout.dialog_moim, null);
                    mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mPopupWindow.setFocusable(true);
                    mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    Button cancel = popupView.findViewById(R.id.Cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPopupWindow.dismiss();
                        }
                    });
                    Button ok = popupView.findViewById(R.id.Ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                        }
                    });


                    List<String> spinnerArray = new ArrayList<>();
                    try {
                        cat_json = read.readJsonFromUrl(url);
                        cat_arr = new JSONArray(cat_json.get("temp").toString());
                        for (int i = 0; i < cat_arr.length(); i++) {
                            JSONObject temp = (JSONObject) cat_arr.get(i);
                            spinnerArray.add(temp.get("cat_name").toString());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    spinner = popupView.findViewById(R.id.spinner);
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(moim.this,
                                    android.R.layout.simple_spinner_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);


                } else if (tmp == 1) {

                    View popupView = getLayoutInflater().inflate(R.layout.dialog_activity, null);
                    mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mPopupWindow.setFocusable(true);
                    mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

                    Button cancel = popupView.findViewById(R.id.Cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPopupWindow.dismiss();
                        }
                    });
                    Button ok = popupView.findViewById(R.id.Ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setTab(int num) {

        this.num = num;
    }

    private int getTab() {

        return this.num;
    }

}

