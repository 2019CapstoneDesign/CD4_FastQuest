package com.example.tt;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

public class Login extends AppCompatActivity {
    //android:value="AIzaSyB_RuFDEAjgz-R4NvYbA13tcvHSKbm999Q" /> debug key
    //android:value="AIzaSyAYMsaas3FYMMpkeo2YC2Zug6bsiwFHM1E" />release key
    Button login;
    EditText username;
    EditText password;
    private AlertDialog dialog;
    private String userID;
    private String userPassword;
    User user;
    LatLng correct_cur_loc;
    static SharedPreferences save;
    static SharedPreferences.Editor editor;
    boolean mycheck_pre;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user = User.getInstance();
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login= (Button)findViewById(R.id.login);

        Current_Location current_location = new Current_Location(this);
        user.setUser_location(current_location.get_current_location());

        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();
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
                        String success = jsonResponse.get("token").toString();
                        JSONObject jsonuser = new JSONObject(jsonResponse.get("user").toString());
                        user = User.getInstance();
                        user.setUser_id(jsonuser.get("id").toString());
                        user.setUsername(jsonuser.get("username").toString());
                        user.setEmail(jsonuser.get("email").toString());
                        user.setNickname(jsonuser.get("nickname").toString());
                        user.setScore(Integer.parseInt(jsonuser.get("score").toString()));
                        user.setActivity(Float.parseFloat(jsonuser.get("activity").toString()));
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        dialog = builder.setMessage("success Login")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor.putString("id", userID);
                                        editor.putString("password", userPassword);
                                        editor.apply();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }
                                })
                                .create();
                        dialog.show();
                    } catch (Exception e) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        dialog = builder.setMessage("Login fail")
                                .setNegativeButton("OK", null)
                                .create();
                        dialog.show();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };//Response.Listener 완료

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                dialog = builder.setMessage("Login fail")
                        .setNegativeButton("OK", null)
                        .create();
                dialog.show();
            }
        };

        //Volley 라이브러리를 이용해서 실제 서버와 통신을 구현하는 부분
        loginRequest login_Request = new loginRequest(userID, userPassword, responseListener, errorListener);
        RequestQueue queue = Volley.newRequestQueue(Login.this);

        queue.add(login_Request);

    }
    public void move_register(View view) {
        startActivity(new Intent(getApplicationContext(), Register_Activity.class));
    }
}
