package com.example.tt.remote;

public class likeAPIUtils {
    private likeAPIUtils(){
    }

    public static final String API_URL = "http://52.79.125.108/api/";

    public static FileService getFileService(){
        return RetrofitClient.getClient(API_URL).create(FileService.class);
    }
}

