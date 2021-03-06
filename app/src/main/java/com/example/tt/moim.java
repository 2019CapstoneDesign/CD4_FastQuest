package com.example.tt;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;

import com.example.tt.model.FileINfo;
import com.example.tt.remote.APIUtils;
import com.example.tt.remote.FileService;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import java.io.ByteArrayOutputStream;
import java.io.File;

import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class moim extends AppCompatActivity {

    private PopupWindow mPopupWindow;
    ImageButton profileButton;
    ImageButton backButton;
    ImageButton moimcreateButton;
    Spinner spinner;
    Spinner spinner2;
    private int num;
    private JSONObject cat_json = null;
    private JSONArray cat_arr = null;
    final url_json read = new url_json();
    final String url = "http://52.79.125.108/api/category/";
    String text;


    TextView create_moim_tilte;
    TextView create_moim_content;
    TextView create_moim_date;
    ImageView create_moim_photo;

    TextView create_challenge_activity;
    TextView create_challenge_content;

    User user;

    String upload_moim_id;

    private final int CAMERA_CODE = 0;
    private final int GALLERY_CODE = 1;

    DialogInterface.OnClickListener camerListener;
    DialogInterface.OnClickListener albumListener;
    DialogInterface.OnClickListener cancleListener;

    Bitmap photo;

    private Uri photoUri;
    private String currentPhotoPath;//실제 사진 파일 경로
    String mImageCaptureName;//이미지 이름

    String imagePath;
    File image_file;

    FileService fileService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moim);

        fileService = APIUtils.getFileService();

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

                    final View popupView = getLayoutInflater().inflate(R.layout.dialog_moim, null);
                    mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mPopupWindow.setFocusable(true);
                    mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                    create_moim_tilte = popupView.findViewById(R.id.creat_moim_title);
                    create_moim_date = popupView.findViewById(R.id.date);
                    create_moim_content = popupView.findViewById(R.id.creat_moim_content);
                    create_moim_photo = popupView.findViewById(R.id.creat_moim_photo);
                    final Button add_photo = popupView.findViewById(R.id.add_photo);
                    final Button cancel = popupView.findViewById(R.id.Cancel);

                    List<String> spinnerArray = new ArrayList<>();
                    try {
                        cat_json = read.readJsonFromUrl(url);
                        cat_arr = new JSONArray(cat_json.get("temp").toString());
                        for (int i = 0; i < cat_arr.length(); i++) {
                            JSONObject temp = (JSONObject) cat_arr.get(i);
                            spinnerArray.add(temp.get("cat_name").toString());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    spinner = popupView.findViewById(R.id.spinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(moim.this, android.R.layout
                            .simple_spinner_dropdown_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPopupWindow.dismiss();
                        }
                    });
                    add_photo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            camerListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (ActivityCompat.checkSelfPermission(moim.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(moim.this, new String[]{Manifest.permission.CAMERA}, 1003);
                                    } else {
                                        if (ActivityCompat.checkSelfPermission(moim.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                                && ActivityCompat.checkSelfPermission(moim.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(moim.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
                                        }
                                    }
                                    takePhoto();
                                }
                            };
                            albumListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (ActivityCompat.checkSelfPermission(moim.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                                            && ActivityCompat.checkSelfPermission(moim.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(moim.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
                                    }
                                    selectGallery();
                                }
                            };
                            cancleListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            };

                            new AlertDialog.Builder(moim.this).setTitle("업로드할 이미지 선택").setPositiveButton("앨범 선택", albumListener).setNeutralButton("사진 촬영", camerListener).setNegativeButton("취소", cancleListener).show();
                        }
                    });
                    Button ok = popupView.findViewById(R.id.Ok);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (create_moim_tilte.getText().toString().equals("")) {
                                new AlertDialog.Builder(moim.this).setTitle("제목을 입력해주세요").setPositiveButton("OK", null).show();
                                return;
                            }
                            if (create_moim_content.getText().toString().equals("")) {
                                new AlertDialog.Builder(moim.this).setTitle("내용을 입력해주세요").setPositiveButton("OK", null).show();
                                return;
                            }
                            if (image_file == null) {
                                new AlertDialog.Builder(moim.this).setTitle("사진을 입력해주세요").setPositiveButton("OK", null).show();
                                //return;
                            }
                            String create_moim_url = "http://52.79.125.108/api/assemble/";
                            JSONObject jsonObject = new JSONObject();
                            user = User.getInstance();
                            Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        upload_moim_id = response.get("id").toString();
                                        Toast.makeText(moim.this, "id 받기 성공", Toast.LENGTH_SHORT);
                                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image_file);
                                        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", image_file.getName(), requestBody);

                                        Call<FileINfo> call = fileService.upload2(upload_moim_id, body);

                                        call.enqueue(new Callback<FileINfo>() {
                                            @Override
                                            public void onResponse(Call<FileINfo> call, retrofit2.Response<FileINfo> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(moim.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                                                    Toast.makeText(moim.this, image_file.getName(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<FileINfo> call, Throwable t) {
                                                Toast.makeText(moim.this, "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(moim.this, "연결 성공", Toast.LENGTH_SHORT);
                                }
                            };

                            try {
                                jsonObject.put("title", create_moim_tilte.getText());
                                jsonObject.put("content", create_moim_content.getText());
                                jsonObject.put("time", create_moim_date.getText());
                                jsonObject.put("author", user.getUser_id());
                                //jsonObject.put("author", user.getUsername());
                                jsonObject.put("category", spinner.getSelectedItem().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            moimRequest mRequest = new moimRequest(Request.Method.POST, create_moim_url, jsonObject, listener, null);
                            RequestQueue moim_request = Volley.newRequestQueue(moim.this);
                            moim_request.add(mRequest);
                            mPopupWindow.dismiss();
                            Toast.makeText(getApplicationContext(), "등록 완료", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else if (tmp == 1) {

                    View popupView = getLayoutInflater().inflate(R.layout.dialog_activity, null);
                    mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    mPopupWindow.setFocusable(true);
                    mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
                    create_challenge_activity = popupView.findViewById(R.id.title);
                    create_challenge_content = popupView.findViewById(R.id.content);

                    final Button cancel = popupView.findViewById(R.id.Cancel);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPopupWindow.dismiss();
                        }
                    });

                    List<String> spinnerArray = new ArrayList<>();
                    spinnerArray.add("공예/만들기");
                    spinnerArray.add("인문학/책/글");
                    spinnerArray.add("사진/영상");
                    spinnerArray.add("댄스/무용");
                    spinnerArray.add("요리/제조");
                    spinnerArray.add("게임/오락");
                    spinnerArray.add("외국/언어");
                    spinnerArray.add("사교/인맥");
                    spinnerArray.add("문화/공연/축제");
                    spinnerArray.add("음악/악기");
                    spinnerArray.add("운동/스포츠");
                    spinnerArray.add("여행/아웃도어");
                    spinnerArray.add("봉사활동");

                    spinner2 = popupView.findViewById(R.id.spinner);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(moim.this, android.R.layout
                            .simple_spinner_dropdown_item, spinnerArray);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter);

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

                            if (create_challenge_activity.getText().toString().equals("")) {
                                new AlertDialog.Builder(moim.this).setTitle("카테고리를 입력해주세요").setPositiveButton("OK", null).show();
                                return;
                            }
                            if (create_challenge_content.getText().toString().equals("")) {
                                new AlertDialog.Builder(moim.this).setTitle("내용을 입력해주세요").setPositiveButton("OK", null).show();
                                return;
                            }

                            String create_challenge_url = "http://52.79.125.108/api/challenge/";
                            JSONObject jsonObject = new JSONObject();
                            user = User.getInstance();
                            try {
                                jsonObject.put("act_name", create_challenge_activity.getText());
                                jsonObject.put("content", create_challenge_content.getText());
                                //jsonObject.put("author", user.getUsername());
                                jsonObject.put("cat_name", spinner2.getSelectedItem().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ChallengeRequest mRequest2 = new ChallengeRequest(Request.Method.POST, create_challenge_url, jsonObject, null, null);
                            RequestQueue challenge_request = Volley.newRequestQueue(moim.this);
                            challenge_request.add(mRequest2);
                            mPopupWindow.dismiss();

                            Toast.makeText(getApplicationContext(), "등록 완료", Toast.LENGTH_SHORT).show();

                        }
                    });
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

    public void add_review(View view) throws JSONException {


    }


    public void takePhoto() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    image_file = photoFile;
                } catch (IOException ex) {

                }
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName(), photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CAMERA_CODE);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/path/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
                + "/path/" + mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();
        return storageDir;
    }

    private void getPictureForPhoto() {
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation;
        int exifDegree;
        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }
        create_moim_photo.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기 }
        photo = bitmap;
    }


    private void selectGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case GALLERY_CODE:
                sendPicture(data.getData());
                break;
            case CAMERA_CODE:
                getPictureForPhoto();
                break;

            default:
                break;

        }
    }

    private void sendPicture(Uri imgUri) {
        imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        File tempfile = new File(imagePath);
        image_file = tempfile;

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        create_moim_photo.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기
        photo = bitmap;
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) { // Matrix 객체 생성
        Matrix matrix = new Matrix(); // 회전 각도 셋팅
        matrix.postRotate(degree); // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }


    public void add_image(View view) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1002:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "저장소 권한 체크 거부", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case 1003:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "카메라 권한 체크 거부", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "저장소 권한 체크 거부", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                break;
        }
    }

}

