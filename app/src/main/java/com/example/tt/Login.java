package com.example.tt;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tt.data.User;

public class Login extends AppCompatActivity {

    Button login;
    EditText username;
    EditText password;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login= (Button)findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = User.getInstance();
                user.setNickname(username.toString());
                startActivity(new Intent(getApplicationContext(),Register_Activity.class));
            }
        });

        //버튼이 눌리면 RegisterActivity로 가게함


    }

}
