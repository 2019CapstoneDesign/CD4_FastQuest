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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class review extends AppCompatActivity {

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

        List<String> listTitle = Arrays.asList("pre_art", "pre_book", "pre_camera", "pre_dance", "pre_food", "pre_game", "pre_language", "pre_meet",
                "pre_travel", "pre_volunteer", "pre_art", "pre_book", "pre_music", "pre_movie", "pre_music", "pre_book");
        List<String> listContent = Arrays.asList(
                "art.",
                "book.",
                "camera.",
                "dance.",
                "food.",
                "game.",
                "language.",
                "meet.",
                "travel.",
                "volunteer",
                "art.",
                "book",
                "music",
                "movie,.",
                "music.",
                "book"
        );
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
        for (int i = 0; i < listTitle.size(); i++) {
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setResId(listResId.get(i));

            adapter.addItem(data);
        }

        adapter.notifyDataSetChanged();
    }
}
