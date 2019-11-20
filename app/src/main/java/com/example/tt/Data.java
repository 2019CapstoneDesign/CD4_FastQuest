package com.example.tt;

import android.graphics.Bitmap;

public class Data {

    private String title;
    private String content;
    private int resId;
    private Bitmap image;

    public Bitmap getImage() { return image; }

    public void setImage(Bitmap image) { this.image = image; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}