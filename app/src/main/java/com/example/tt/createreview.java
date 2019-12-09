package com.example.tt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.Activity;
import com.example.tt.data.User;
import com.example.tt.model.FileINfo;
import com.example.tt.remote.APIUtils;
import com.example.tt.remote.FileService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;




public class createreview extends AppCompatActivity {

    User user = User.getInstance();

    Button addImage;
    ImageView image;
    EditText review_title, review_content;
    String string_activity, act_id, upload_id;
    File image_file = null;
    FileService fileService;
    Add_picture add_picture;
    int score;
    static SharedPreferences save;
    static SharedPreferences.Editor editor;

    public void add_review(View view) throws JSONException {
        if (check_empty() == false) {
            return;
        }
        score = save.getInt("score", 0);
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    upload_id = response.get("id").toString();
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }
                upload_image Upload = new upload_image(fileService);
                Upload.send_image(upload_id, image_file);
                editor.remove("page");
                editor.apply();
                Edit_score add_score = new Edit_score(createreview.this);
                add_score.edit_score(user.getUsername(), score);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        };//Response.Listener 완료

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", review_title.getText().toString());
        jsonObject.put("content", review_content.getText().toString());
        jsonObject.put("act", act_id);
        jsonObject.put("author", Integer.parseInt(user.getUser_id().toString()));
        String nickname = user.getNickname();
        jsonObject.put("nickname", nickname);
        String username = user.getUsername();
        jsonObject.put("username", username);
        ReviewRequest reviewRequest = new ReviewRequest(Request.Method.POST, jsonObject, responseListener, null);
        RequestQueue queue = Volley.newRequestQueue(createreview.this);
        queue.add(reviewRequest);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_review);
        fileService = APIUtils.getFileService();
        addImage = (Button) findViewById(R.id.add_image);
        image = (ImageView) findViewById(R.id.input_image);
        review_title = (EditText) findViewById(R.id.input_reviw_title);
        review_content = (EditText) findViewById(R.id.input_reviw_content);

        save = getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();

        string_activity = save.getString("activity", "");
        try {
            JSONObject tem_j = new JSONObject(string_activity);
            Activity play_activity = new Activity(tem_j);
            act_id = play_activity.act_id;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        editor.putInt("page", 4);
        editor.apply();

        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.remove("page");
                editor.apply();
                Toast.makeText(createreview.this, String.valueOf(score) + "점이 적립되었습니다.", Toast.LENGTH_LONG);
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    public void add_image(View view) {
        add_picture = new Add_picture(this, image);
        add_picture.add_photo();
        image_file = add_picture.get_image_file();
    }

    public boolean check_empty() {
        if (review_title.getText().toString().equals("")) {
            new AlertDialog.Builder(this).setTitle("제목을 입력해주세요").setPositiveButton("OK", null).show();
            return false;
        }
        if (review_content.getText().toString().equals("")) {
            new AlertDialog.Builder(this).setTitle("내용을 입력해주세요").setPositiveButton("OK", null).show();
            return false;
        }
        if (image_file == null) {
            new AlertDialog.Builder(this).setTitle("이미지 없습니다.").setPositiveButton("OK", null).show();
            return false;
        }
        return true;
    }

    private final int CAMERA_CODE = 0;
    private final int GALLERY_CODE = 1;

    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case GALLERY_CODE:
                add_picture.sendPicture(data.getData());
                break;
            case CAMERA_CODE:
                add_picture.getPictureForPhoto();
                break;

            default:
                break;

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
