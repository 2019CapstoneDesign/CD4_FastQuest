package com.example.tt;

import android.app.Activity;
import android.content.Intent;

import com.example.tt.model.FileINfo;
import com.example.tt.remote.FileService;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class upload_image {
    FileService fileService;

    public upload_image(FileService fileService) {
        this.fileService = fileService;
    }

    void send_image(String upload_content_id, File image_file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), image_file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", image_file.getName(), requestBody);

        Call<FileINfo> call = fileService.upload(upload_content_id, body);

        call.enqueue(new Callback<FileINfo>() {
            @Override
            public void onResponse(Call<FileINfo> call, retrofit2.Response<FileINfo> response) {
                if (response.isSuccessful()) {

                }
            }

            @Override
            public void onFailure(Call<FileINfo> call, Throwable t) {
            }
        });
    }
}
