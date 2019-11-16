package com.example.tt.data;

import org.json.JSONException;
import org.json.JSONObject;

public class Activity {
    public String act_id;
    public String title;
    public String category;
    public String content;
    public char longterm;
    public char outside;
    public String address;
    public double latitude;
    public double longitude;
    public float score;

    public Activity() {

    }

    public Activity(String Act_id, String Title, String Category, String Content, char Longterm, char Outside, String Address, double Latitude, double Longitude, float Score) {
        this.act_id = Act_id;
        this.title = Title;
        this.category = Category;
        this.content = Content;
        this.longterm = Longterm;
        this.outside = Outside;
        this.address = Address;
        this.latitude = Latitude;
        this.longitude = Longitude;
        this.score = Score;
    }

    public Activity(JSONObject jsonActivity) throws JSONException {
        this.act_id = jsonActivity.get("act_id").toString();
        this.title = jsonActivity.get("title").toString();
        this.category = jsonActivity.get("category").toString();
        this.content = jsonActivity.get("content").toString();
        this.longterm = jsonActivity.get("longterm").toString().charAt(0);
        this.outside = jsonActivity.get("outside").toString().charAt(0);
        this.address = jsonActivity.get("address").toString();
        this.latitude = Double.parseDouble(jsonActivity.get("latitude").toString());
        this.longitude = Double.parseDouble(jsonActivity.get("longitude").toString());
    }
}
