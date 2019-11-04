package com.example.tt;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tt.data.User;
import com.example.tt.data.category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Comparator;
import java.util.Vector;

public class CardInfo extends AppCompatActivity {
    Button card1, card2, card3, card4, card5;
    Vector<category> value;
    static int reload_value = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*User user = new User(20150101, "nh7881@naver.com", "South", 777, 20, 15, 0, 23, 19970219, 0);
        Vector<String> vprecat = new Vector<String>();
        user_pre_cat pre_cat1 = new user_pre_cat(20150101, "헬스");
        user_pre_cat pre_cat2 = new user_pre_cat(20150101, "볼링");
        vprecat.add(pre_cat1.getCat_name());
        vprecat.add(pre_cat2.getCat_name());*/
        //추천 알고리즘 옹작할 것!
        //Vector<category> value = new Vector<category>();
        User user = User.getInstance();
        final url_json read = new url_json();
        JSONObject user_info;
        String user_info_url = "http://52.79.125.108/api/user/%EA%B8%B0%EB%8B%88%ED%94%BC%EA%B7%B8";
        //url = "http://52.79.125.108/api/user/" + user.getnickname();
        // 밑 try문은 디버깅용
        try {
            user_info = read.readJsonFromUrl(user_info_url);
            JSONObject temp = new JSONObject(user_info.get("temp").toString());
            user.setNickname(temp.get("nickname").toString());
            user.setActivity(Float.parseFloat(temp.get("activity").toString()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://52.79.125.108/api/" + user.getNickname();
        value = new Vector<>();

        JSONObject cat_json = null;
        JSONArray cat_arr = null;

        try {
            cat_json = read.readJsonFromUrl(url);
            cat_arr = new JSONArray(cat_json.get("temp").toString());
            for (int i = 0; i < cat_arr.length(); i++) {
                JSONObject temp = (JSONObject) cat_arr.get(i);
                category temp_cat = new category();
                temp_cat.cat_name = temp.get("cat_name").toString();
                temp_cat.activity_rate = Float.parseFloat(temp.get("activity_rate").toString());
                value.add(temp_cat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // database완성되면 추가
        //임의 값
        //value.add(new category("골프","스포츠",40,60,28));
        //value.add(new category("pc방","오락",20,10,66));
        //value.add(new category("볼링","스포츠",29,15,12));
        //value.add(new category("오리보트","관광",88,70,01));
        //value.add(new category("한강투어","관광",10,60,00));
        setContentView(R.layout.activity_card_info);
        final Intent intent = new Intent(getApplicationContext(), card_selected.class);

        card1 = (Button) findViewById(R.id.card1);
        card_set(value.get(0).cat_name, (int) Math.abs(value.get(0).activity_rate - user.getActivity()), card1);
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("cat_name", value.get(0).cat_name);
                startActivity(intent);
            }
        });
        card2 = (Button) findViewById(R.id.card2);
        card_set(value.get(1).cat_name, (int) Math.abs(value.get(1).activity_rate - user.getActivity()), card2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("cat_name", value.get(1).cat_name);
                startActivity(intent);
            }
        });
        card3 = (Button) findViewById(R.id.card3);
        card_set(value.get(2).cat_name, (int) Math.abs(value.get(2).activity_rate - user.getActivity()), card3);
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("cat_name", value.get(2).cat_name);
                startActivity(intent);
            }
        });
        card4 = (Button) findViewById(R.id.card4);
        card_set(value.get(3).cat_name, (int) Math.abs(value.get(3).activity_rate - user.getActivity()), card4);
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("cat_name", value.get(3).cat_name);
                startActivity(intent);
            }
        });
        card5 = (Button) findViewById(R.id.card5);
        card_set(value.get(4).cat_name, (int) Math.abs(value.get(4).activity_rate - user.getActivity()), card5);
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("cat_name", value.get(4).cat_name);
                startActivity(intent);
            }
        });

    }

    public void reload(View view) {

        if (reload_value > 0) {
            Intent reload = getIntent();
            finish();
            reload_value = reload_value - 1;
            startActivity(reload);
        } else {
            new AlertDialog.Builder(this).setTitle("Reload 횟수가 끝났습니다.").setPositiveButton("OK", null).show();
            return;
        }
    }


/*
    public Vector<category> recommend(String cat_url, User user) throws  IOException, JSONException{
        //cat_url 일정 값안에 있는 카테고리 url
        //user는 user정보가 저장되어 있어야한다.
        //최소치와 최대치를 충분히 크게 잡아 카테고리가 최소 5개 이상은 나와야한다.
        //최소치 최대치는 싫어하는거까지 포함하는 값을 넣는다.
        String pre_cat_url = "http://52.79.125.108/api/pre_cat/2";
        JSONObject pre_cat_json = null;
        url_json read = new url_json();
        pre_cat_json = read.readJsonFromUrl(pre_cat_url);
        JSONArray pre_cat_arr = new JSONArray(pre_cat_json.get("temp").toString());
        Vector<user_pre_cat> my_user_pre_category = new Vector<user_pre_cat>();
        for(int i = 0; i < pre_cat_arr.length(); i++) {
            JSONObject myuser_pre_cat =  (JSONObject) pre_cat_arr.get(i);
            user_pre_cat temp_user_pre_cat = new user_pre_cat(myuser_pre_cat);
            my_user_pre_category.add(temp_user_pre_cat);
        }

        JSONObject recom_cat_json = null;
        recom_cat_json = read.readJsonFromUrl(cat_url);
        JSONArray recom_cat_arr = new JSONArray(recom_cat_json.get("temp").toString());
        Vector<category> condi_recom_cats = new Vector<category>();
        for(int j = 0; j<recom_cat_arr.length(); j++) {
            JSONObject recom_cat = (JSONObject) recom_cat_arr.get(j);
            category temp_cat = new category(recom_cat);
            temp_cat.score = Math.abs(user.getSociality() - temp_cat.sociality_rate) + Math.abs(user.getActivity() - temp_cat.activity_rate);
            condi_recom_cats.add(temp_cat);
        }
        Collections.sort(condi_recom_cats, new MyComparator());

        int num =  (int)(5 - user.score * 0.001);
        Vector<category> value = new Vector<>();
        List<category> like;
        List<category> dislike;
        int search = 0;
        int index =0;
        while (index <= num && index != condi_recom_cats.size()) {
            search = search + 10;
            index = token(condi_recom_cats, Math.min(search, condi_recom_cats.size() -1));
        }
        like = condi_recom_cats.subList(0, index);
        if(index + 5 - num < condi_recom_cats.size() -1) {
            dislike = condi_recom_cats.subList(index + 1,
                    Math.min(index + num * 10, condi_recom_cats.size()));
        }
        else{
            dislike = new Vector<category>();
            for(int o = 0; o < 5 - num; o++) {
                dislike.add(like.get(like.size() - 1));
                like.remove(like.size() - 1);
            }
        }

        for(int n = 0; n<num; n++) {
            if(like.size() == 0) {break;}
            Random random = new Random();
            int seed = random.nextInt(like.size());
            value.add(like.get(seed));
            like.remove(seed);
        }
        for(int k = 0; k < 5-num; k++) {
            if(dislike.size() == 0) {break;}
            Random random = new Random();
            int seed = random.nextInt(dislike.size());
            value.add(dislike.get(seed));
            dislike.remove(seed);
        }
        return value;
    }

    public Vector<category> recom(User user, Vector<String> vprecat) throws IOException, JSONException {
        Vector<String> catlist = new Vector<String>();
        Vector<category> precat = new Vector<category>();
        float act = 0;
        float max_act =0;
        float soc = 0;
        float max_soc = 0;
        JSONObject jsonCat = null;
        String handle;
        jsonCat = readJsonFromUrl("http://52.79.125.108/api/category/");
        // jsoncat값을 category안에 ㄱㄱ
        JSONArray carr = new JSONArray(jsonCat.get("temp").toString());
        Vector<category> vcat = new Vector<category>();
        for(int i= 0; i<carr.length(); i++) {
            JSONObject temp = (JSONObject) carr.get(i);
            category tcat = new category();
            tcat.cat_name = temp.get("cat_name").toString();
            tcat.Lcat_name = temp.get("lcat_name").toString();
            handle = temp.get("activity_rate").toString();
            if (handle == "null") {
                tcat.activity_rate = 0;
            } else {
                tcat.activity_rate = Integer.parseInt(handle);
            }
            handle = temp.get("sociality_rate").toString();
            if (handle == "null") {
                tcat.sociality_rate = 0;
            } else {
                tcat.sociality_rate = Integer.parseInt(handle);
            }

            if(vprecat.get(0).equals(tcat.cat_name) || vprecat.get(1).equals(tcat.cat_name)) {

                if(max_act < Math.abs(user.getActivity() - tcat.activity_rate)) {
                    max_act = Math.abs(user.getActivity() - tcat.activity_rate);
                    act = tcat.activity_rate;
                }

                if(max_soc < Math.abs(user.getSociality() - tcat.sociality_rate)) {
                    max_soc = Math.abs(user.getSociality() - tcat.sociality_rate);
                    soc = tcat.sociality_rate;
                }
                precat.add(tcat);
            }
            vcat.add(tcat);
        }

        for(int k = 0; k<vcat.size(); k ++) {
            float sco = 100 - (Math.abs(soc - vcat.get(k).sociality_rate) + Math.abs(act - vcat.get(k).activity_rate));
            vcat.get(k).score = sco;

        }

        JSONObject jsonObject = null;
        String url = "http://52.79.125.108/api/activity/" +
                (int)Math.min(user.getSociality(), soc) +"/"+ (int)Math.max(user.getSociality(), soc) +"/"+
                (int)Math.min(user.getActivity(), act) +"/"+ (int)Math.max(user.getActivity(), act) +"/outside/solo";
        jsonObject = readJsonFromUrl(url);
        JSONArray jarr = new JSONArray(jsonObject.get("temp").toString());
        Vector<Activity> activities = new Vector<Activity>();

        for(int i= 0; i<jarr.length(); i++) {
            JSONObject temp = (JSONObject)jarr.get(i);
            Activity tact = new Activity();
            tact.act_id = Integer.parseInt(temp.get("act_id").toString());
            tact.title = temp.get("title").toString();
            tact.category = temp.get("category").toString();
            tact.content = temp.get("content").toString();
            tact.longterm = temp.get("longterm").toString().charAt(0);
            tact.outside = temp.get("outside").toString().charAt(0);
            tact.address = temp.get("address").toString();
            tact.latitude = Double.parseDouble(temp.get("latitude").toString());
            tact.longitude = Double.parseDouble(temp.get("longitude").toString());
            if(catlist.contains(tact.category) != true) {
                catlist.add(tact.category);
            }
            activities.add(tact);
        }
        Collections.sort(vcat, new MyComparator());
        Vector<category> recommend = new Vector<category>();
        int size = vcat.size();
        for(int j =0; j<size;j++) {
            if(catlist.contains(vcat.get(j).cat_name) == true) {
                recommend.add(vcat.get(j));
                vcat.remove(j);
                j = j-1;
                size = size -1;
            }
        }
        Vector<category> value = new Vector<category>();
        int num =  (int)(5 - user.score * 0.001);
        for(int n = 0; n<num; n++) {
            if(recommend.size() == 0) {break;}
            Random random = new Random();
            int seed = random.nextInt(recommend.size());
            value.add(recommend.get(seed));
            recommend.remove(seed);
        }
        for(int k = 0; k<5-num; k++) {
            if(vcat.size() == 0) {break;}
            value.add(vcat.get(Math.min(k, vcat.size()-1)));
            vcat.remove(Math.min(k, vcat.size()-1));
        }
        if(value.size() < 5) {
            while(value.size() < 5) {
                value.add(vcat.get(0));
                vcat.remove(0);
            }
        }

        return value;



        //유저의 카테고리 받고 <-- 임의 값
        // 유저의 카테고리와 같은 대분류를 가진 카테고리들 갖고 오기
        // query user category = category
        // 유저의 안밖 받고
        // query user in_out = in_out
        // 유저의 안밖과 같은 안밖을 가진 활동을 첫번 째 대분류에서 다 뽑는다.
        // 유저의 사교성과 활동성을 받고
        // 유저의 사교성과 활동성과 카테고리의 사교성 활동성 사이의 범위를 가진 활동들을 뽑는다.
        //  query max(유저 사교성,  카테고리 사교성) > activity && min(유저 사교성, 카테고리 사교성)
        //  랜덤으로 5 - 사용자의 점수 * 0.0001개만 받는다. 이 때 받은 값을 int로 받음으로써 소수 자리 값은 버린다.
        // 받은 결과와 활동성과 사교성을 비교하여 점수를 뽑고 색깔에 반영한다.
        // 나머지 수 만큼 (자신의 사교성과 활동성 바깥) <> (사용자의 점수 * 0.0003 + 자신의 사교성과 활동성 바깥)의 법위를 가진 활동을 뽑는다.
        // 원하는 수만큼 뽑으면 끝내고 색깔에 반영한다.
        // 해당하는 결과가 없다면 범위를 1늘려서 반복한다.
    }*/




    public void card_set(String text, int value, Button card) {
        value = value /20;
        card.setText(text);
        card.setTextColor(Color.BLACK);
        card.setTextSize(20);
        card.setTypeface(card.getTypeface(), Typeface.BOLD);
        switch (value) {
            case 0:
                card.setBackgroundColor(Color.GRAY);
                break;
            case 1:
                card.setBackgroundColor(Color.GREEN);
                break;
            case 2:
                card.setBackgroundColor(Color.BLUE);
                break;
            case 3:
                card.setBackgroundColor(Color.parseColor("#FF00DD"));
                break;
            case 4 :
                card.setBackgroundColor(Color.YELLOW);
                break;
        }
    }

    public int token(Vector<category> condi_recom_cats, float search) {
        for(int l =0; l < condi_recom_cats.size(); l++) {
            if (condi_recom_cats.get(l).score > search) {
                return l;
            }
        }
        return  condi_recom_cats.size();
    }

    class MyComparator implements Comparator<category> {
        @Override
        public int compare(category c1, category c2) {
            if (c1.score > c2.score) {
                return 1;
            }
            return -1;
        }
    }

}
