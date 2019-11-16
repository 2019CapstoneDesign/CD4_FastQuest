package com.example.tt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class review extends AppCompatActivity {

    private JSONObject cat_json = null;
    private JSONArray cat_arr = null;
    final url_json read = new url_json();
    private String url;

    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        BottomNavigationView nav_view = findViewById(R.id.nav_view);
        Menu menu = nav_view.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        break;

                    case R.id.navigation_moim:
                        startActivity(new Intent(getApplicationContext(), moim.class));
                        break;

                    case R.id.navigation_review:

                        break;
                }
                return false;
            }
        });

        init();
        getData();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {


        //그냥 이미지 샘플 입력용

        List<Integer> listResId = Arrays.asList(
                R.drawable.pre_art,
                R.drawable.pre_book,
                R.drawable.pre_camera,
                R.drawable.pre_dance,
                R.drawable.pre_food,
                R.drawable.pre_game,
                R.drawable.pre_language,
                R.drawable.pre_meet,
                R.drawable.pre_travel,
                R.drawable.pre_volunteer,
                R.drawable.pre_art,
                R.drawable.ic_person_black_24dp,
                R.drawable.pre_music,
                R.drawable.pre_movie,
                R.drawable.pre_music,
                R.drawable.pre_book
        );

        url = "http://52.79.125.108/api/feed";
        try {
            cat_json = read.readJsonFromUrl(url);
            cat_arr = new JSONArray(cat_json.get("temp").toString());
            for (int i = 0; i < cat_arr.length(); i++) {
                JSONObject temp = (JSONObject) cat_arr.get(i);
                Data data = new Data();
                data.setTitle(temp.get("title").toString());
                data.setContent(temp.get("content").toString());
                data.setAuthor(temp.get("author").toString());
               // data.setResId(temp.get("image");
                data.setResId(listResId.get(i));
                data.setUrlImage(temp.get("image").toString());
                adapter.addItem(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getApplicationContext(), cat_json.toString(), Toast.LENGTH_SHORT).show();

        adapter.notifyDataSetChanged();
    }
}
