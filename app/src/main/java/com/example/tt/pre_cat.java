package com.example.tt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tt.data.UserData;

import java.util.ArrayList;


public class pre_cat extends AppCompatActivity implements View.OnClickListener {

    ToggleButton pre_art;
    ToggleButton pre_book;
    ToggleButton pre_camera;
    ToggleButton pre_dance;
    ToggleButton pre_food;
    ToggleButton pre_game;
    ToggleButton pre_language;
    ToggleButton pre_meet;
    ToggleButton pre_movie;
    ToggleButton pre_music;
    ToggleButton pre_sports;
    ToggleButton pre_travel;
    ToggleButton pre_volunteer;

    Button saveButton;
    ImageButton backButton;
    static ArrayList<Boolean> BoolList =  new ArrayList<Boolean>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pre_cat);

        pre_art = findViewById(R.id.pre_Art);
        pre_art.setOnClickListener(this);
        pre_book = findViewById(R.id.pre_Book);
        pre_book.setOnClickListener(this);
        pre_camera = findViewById(R.id.pre_Camera);
        pre_camera.setOnClickListener(this);
        pre_dance = findViewById(R.id.pre_Dance);
        pre_dance.setOnClickListener(this);
        pre_food = findViewById(R.id.pre_Food);
        pre_food.setOnClickListener(this);
        pre_game = findViewById(R.id.pre_Game);
        pre_game.setOnClickListener(this);
        pre_language =findViewById(R.id.pre_Language);
        pre_language.setOnClickListener(this);
        pre_book.setOnClickListener(this);
        pre_book.setOnClickListener(this);
        pre_meet = findViewById(R.id.pre_Meet);
        pre_meet.setOnClickListener(this);
        pre_movie = findViewById(R.id.pre_Movie);
        pre_movie.setOnClickListener(this);
        pre_music = findViewById(R.id.pre_Music);
        pre_music.setOnClickListener(this);
        pre_sports = findViewById(R.id.pre_Sports);
        pre_sports.setOnClickListener(this);
        pre_travel = findViewById(R.id.pre_Travel);
        pre_travel.setOnClickListener(this);
        pre_volunteer = findViewById(R.id.pre_Volunteer);
        pre_volunteer.setOnClickListener(this);

        saveButton = findViewById(R.id.saveButton);

        UserData userdata = new UserData();
        BoolList = userdata.getBooList();

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


    }
    public void OnClickHandler(View view)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("수정사항").setMessage("저장하시겠습니까?");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {

                UserData userdata = new UserData();
                userdata.setBoolList(BoolList);

                Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
               // startActivity(new Intent(getApplicationContext(),MainActivity.class));


            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pre_Art:
                Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pre_Book:
                Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pre_Camera:
                Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pre_Dance:
                Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pre_Food:
                Toast.makeText(getApplicationContext(), "5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pre_Game:
                Toast.makeText(getApplicationContext(), "6", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pre_Language:
                Toast.makeText(getApplicationContext(), "7", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pre_Meet:
                Toast.makeText(getApplicationContext(), "8", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pre_Movie:
                Toast.makeText(getApplicationContext(), "9", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pre_Music:
                Toast.makeText(getApplicationContext(), "10", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pre_Sports:
                Toast.makeText(getApplicationContext(), "11", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pre_Travel:
                Toast.makeText(getApplicationContext(), "12", Toast.LENGTH_SHORT).show();
                break;
            case R.id.pre_Volunteer:
                Toast.makeText(getApplicationContext(), "13", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}