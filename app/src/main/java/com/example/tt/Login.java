package com.example.tt;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;

import org.json.JSONObject;

public class Login extends AppCompatActivity {

    Button login;
    EditText username;
    EditText password;
    private AlertDialog dialog;
    private String userID;
    private String userPassword;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login= (Button)findViewById(R.id.login);


        //버튼이 눌리면 RegisterActivity로 가게함
    }

    public void login_button(View view) {
        userID = username.getText().toString();
        userPassword = password.getText().toString();
        Response.Listener<String> responseListener = new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    try {
                        String success = jsonResponse.get("key").toString();
                    } catch (Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        dialog = builder.setMessage("Login fail")
                                .setNegativeButton("OK", null)
                                .create();
                        dialog.show();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                    dialog = builder.setMessage("success Login")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    User user = User.getInstance();
                                    user.setNickname(username.toString());
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                            })
                            .create();
                    dialog.show();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };//Response.Listener 완료

        //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
        loginRequest login_Request = new loginRequest(userID, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Login.this);

        queue.add(login_Request);

    }
    public void move_register(View view) {
        startActivity(new Intent(getApplicationContext(), Register_Activity.class));
    }

}
