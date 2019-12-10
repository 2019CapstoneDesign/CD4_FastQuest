package com.example.tt;


import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;
import com.example.tt.model.Data;
import com.example.tt.remote.APIUtils;
import com.example.tt.remote.FileService;
import com.like.IconType;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MoimAdapter extends RecyclerView.Adapter<MoimAdapter.ItemViewHolder> {

    private ArrayList<Data> listData = new ArrayList<>();
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    User user = User.getInstance();
    String writen_id;
    String moim_id;
    ArrayList<String> slike_list_save = new ArrayList<String>();
    url_json read = new url_json();
    String url;
    private JSONObject cat_json = null;
    private JSONArray cat_arr = null;
    JSONObject my_moim_json;
    FileService moim_like_fileService;
    JSONObject assemblelike_id = new JSONObject();
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.font.moim_item, parent, false);

        add_moim_id();

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.moim_item, parent, false);
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) v.getLayoutParams();
        lp.height = parent.getMeasuredHeight() / 2;
        v.setLayoutParams(lp);

        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(listData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(Data data) {
        listData.add(data);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private ImageView imageView1;
        private LikeButton likebutton;

        private Data data;
        private int position;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.content);
            likebutton = itemView.findViewById(R.id.heart);
            imageView1 = itemView.findViewById(R.id.imageView2);
        }

        void onBind(final Data data, int position) {
            this.data = data;
            this.position = position;

            textView1.setText(data.getTitle());
            textView2.setText(data.getAuthor());
            textView3.setText(data.getDate());
            writen_id = data.getAuthor();
            moim_id = data.getId();
            //textView3.setText(data.getContent());
            if(data.getUrlImage()!= "null"){
                Picasso.get().load(data.getUrlImage()).into(imageView1);
            }
            likebutton.setIcon(IconType.Heart);
            likebutton.setScaleX(1);
            likebutton.setScaleY(1);
            if(slike_contains(moim_id)) {
                likebutton.setLiked(true);
            }
            likebutton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    //생김+1
                    moim_id = data.getId();
                    if (!slike_contains(moim_id)) {
                        edit_score(writen_id,1);
                        slike_list_save.add(moim_id);

                        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    assemblelike_id.put(moim_id, response.get("id"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };//Response.Listener 완료

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("user", user.getUser_id());
                            jsonObject.put("assemble", Integer.parseInt(moim_id));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        moim_like_Request moim_like_request = new moim_like_Request(Request.Method.POST, jsonObject, responseListener, null);
                        RequestQueue moim_like_queue = Volley.newRequestQueue(context);

                        moim_like_queue.add(moim_like_request);
                    }

                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    //사라짐 -1
                    moim_id = data.getId();
                    edit_score(writen_id,-1);
                    if (slike_contains(moim_id)) {
                        edit_score(writen_id,-1);
                        slike_list_save.remove(moim_id);
                        moim_like_fileService = APIUtils.getFileService();
                        //Call<Void> call = fileService.deleteassemble(user.getUsername(), moim_id);
                        String id = "";
                        try {
                            id = assemblelike_id.getString(moim_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Call<Void> calls = moim_like_fileService.deleteassemble(id);
                        calls.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                assemblelike_id.remove(moim_id);
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });


                    }
                }
            });


            itemView.setOnClickListener(this);
            textView1.setOnClickListener(this);
            textView2.setOnClickListener(this);
            imageView1.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context,moim_detail.class);
            intent.putExtra("image",data.getUrlImage());
            //Toast.makeText(context, data.getUrlImage() + " 이미지 입니다.", Toast.LENGTH_SHORT).show();
            intent.putExtra("author",data.getAuthor());
            //Toast.makeText(context, data.getWriter() + " 작성자 입니다.", Toast.LENGTH_SHORT).show();
            intent.putExtra("title",data.getTitle());
            //Toast.makeText(context, data.getTitle() + " 제목 입니다.", Toast.LENGTH_SHORT).show();
            intent.putExtra("content",data.getContent());
           // Toast.makeText(context, data.getContent() + " 내용 입니다.", Toast.LENGTH_SHORT).show();
            intent.putExtra("id", data.getId());
            context.startActivity(intent);

            switch (v.getId()) {

                case R.id.textView1:

                    break;
                case R.id.textView2:

                    break;
                case R.id.imageView1:

                    break;
            }
        }


    }

    public void edit_score(String user_id, int score) {
        // 수정하면 유저 id 받으면 통신하는게 완성 됨
        int temp_score = 0;
        com.android.volley.Response.Listener<JSONObject> pjresponseListener = new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(user.getUser_id() == writen_id) {
                        user.setScore(Integer.parseInt(response.get("score").toString()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        //String URL = "http://52.79.125.108/api/detail/" + user_id;
        String URL = "http://52.79.125.108/api/user/" +  user_id;
        url_json read = new url_json();
        JSONObject jtemp_score = null;
        try {
            jtemp_score = read.readJsonFromUrl(URL);
            JSONObject temp = new JSONObject(jtemp_score.get("temp").toString());
            temp_score = Integer.parseInt(temp.get("score").toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject pointj = new JSONObject();
        try {
            pointj.put("score", (int)(temp_score +score));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addpointRequest preq = new addpointRequest(Request.Method.POST, pointj, URL, pjresponseListener, null);
        RequestQueue pjqueue = Volley.newRequestQueue(context);
        pjqueue.add(preq);
    }

    boolean slike_contains(String temp) {
        for(int j =0; j<slike_list_save.size(); j++) {
            if(slike_list_save.get(j).equals(temp)) {
                return true;
            }
        }
        return false;
    }
    void add_moim_id() {
        url = "http://52.79.125.108/api/likeassemble/user/"+ user.getUsername();
        try {
            cat_json = read.readJsonFromUrl(url);
            cat_arr = new JSONArray(cat_json.get("temp").toString());
            for(int j =0; j<cat_arr.length(); j++) {
                my_moim_json = (JSONObject) cat_arr.get(j);
                if(!slike_contains(my_moim_json.get("assemble").toString())) {
                    slike_list_save.add(my_moim_json.get("assemble").toString());
                    assemblelike_id.put(my_moim_json.get("assemble").toString(), my_moim_json.get("id").toString());
                }
            }

        }
        catch (Exception e) {

        }
    }



}