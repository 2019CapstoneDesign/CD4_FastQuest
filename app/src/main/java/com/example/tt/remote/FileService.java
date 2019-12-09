package com.example.tt.remote;


import com.example.tt.model.FileINfo;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface FileService {
    @Multipart
    @PUT("{Id}")
    Call<FileINfo> upload(@Path("Id") String id, @Part MultipartBody.Part image);

    @DELETE("likefeed/{username}/{feed}")
    Call<Void> deletePost(@Path("username") String username, @Path("feed") String feed);

    @DELETE("likeassemble/{username}/{feed}")
    Call<Void> deleteassemble(@Path("username") String username, @Path("feed") String feed);

    //@POST("likefeed")
    //Call<likePost> posts(@Body likePost post);
}
