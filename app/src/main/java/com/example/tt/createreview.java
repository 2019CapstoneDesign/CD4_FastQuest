package com.example.tt;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class createreview extends AppCompatActivity {
    DialogInterface.OnClickListener camerListener;
    DialogInterface.OnClickListener albumListener;
    DialogInterface.OnClickListener cancleListener;

    Uri mImageCaptureUri;
    Button addImage;
    ImageView image;
    Bitmap photo;
    EditText review_title;
    EditText review_content;
    AlertDialog dialog;

    public void add_review(View view) {
        if (review_title.getText().toString().equals("")) {
            new AlertDialog.Builder(this).setTitle("제목을 입력해주세요").setPositiveButton("OK", null).show();
            return;
        }
        if (review_content.getText().toString().equals("")) {
            new AlertDialog.Builder(this).setTitle("내용을 입력해주세요").setPositiveButton("OK", null).show();
            return;
        }
        /*if (photo.isPremultiplied()) {
            new AlertDialog.Builder(this).setTitle("사진을 입력해주세요").setPositiveButton("OK", null).show();
            return;
        }*/
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try{
            photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        }
        catch (Exception e) {
            new AlertDialog.Builder(this).setTitle("사진을 입력해주세요").setPositiveButton("OK", null).show();
            return;
        }
        byte[] byteArray = stream.toByteArray();

        Response.Listener<String> responseListener = new Response.Listener<String>(){

            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success){//사용할 수 있는 아이디라면
                        AlertDialog.Builder builder = new AlertDialog.Builder(createreview.this);
                        dialog = builder.setMessage("리뷰가 등록되었습니다.")
                                .setPositiveButton("OK", null)
                                .create();
                        dialog.show();
                        finish();//액티비티를 종료시킴(회원등록 창을 닫음)
                    }else{//사용할 수 없는 아이디라면
                        AlertDialog.Builder builder = new AlertDialog.Builder(createreview.this);
                        dialog = builder.setMessage("등록이 실패하였습니다.")
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

        ReviewRequest registerRequest = new ReviewRequest(review_title.getText().toString(),
                review_content.getText().toString(), byteArray, responseListener);
        RequestQueue queue = Volley.newRequestQueue(createreview.this);

        queue.add(registerRequest);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_review);

       addImage = (Button)findViewById(R.id.add_image);
       image = (ImageView)findViewById(R.id.input_image);
       review_title = (EditText)findViewById(R.id.input_reviw_title);
       review_content = (EditText)findViewById(R.id.input_reviw_content);


    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = "temp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent,0);
    }
    public void takeAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    public void onActivityResult (int requestCode, int resultCode, Intent data) {

        if(resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case 1:
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    photo = BitmapFactory.decodeStream(in);
                    in.close();
                    image.setImageBitmap(photo);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
                //Log.d("TT",mImageCaptureUri.getPath().toString());
            /*case 0:
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");
                intent.putExtra("outputx", 200);
                intent.putExtra("outputy", 200);
                intent.putExtra("aspectx", 1);
                intent.putExtra("aspecty", 1);
                intent.putExtra("scale", true);
                intent.putExtra("return_data", true);
                startActivityForResult(intent, 2);
                break;
            case 2:
                if(resultCode != RESULT_OK) {
                    return;
                }
                final Bundle extras = data.getExtras();

                if(extras != null) {
                    photo = extras.getParcelable("data");
                    image.setImageBitmap(photo);
                    break;
                }

                File f = new File(mImageCaptureUri.getPath());
                if(f.exists()) {
                    f.delete();
                }*/
        }
    }

    public void add_image(View view) {
        /*camerListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takePhoto();;
            }
        };*/
        albumListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                takeAlbum();
            }
        };
        cancleListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        new AlertDialog.Builder(this).setTitle("업로드할 이미지 선택").setPositiveButton("앨범 선택", albumListener).setNegativeButton("취소", cancleListener).show();
    }


}
