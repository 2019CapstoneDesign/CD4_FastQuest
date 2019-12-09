package com.example.tt;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.tt.data.User;
import com.example.tt.model.FileINfo;
import com.example.tt.remote.APIUtils;
import com.example.tt.remote.FileService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class test extends AppCompatActivity {
    FileService fileService;
    Button btnChooseFile;
    Button btnUpload;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        alarm();

    }

    public void onWork() {
        WorkManager workManager = WorkManager.getInstance();
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(simpleWorker.class).build();
        workManager.enqueue(workRequest);
    }

    public  void periodicwor() {
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(simpleWorker.class, 15, TimeUnit.MINUTES).build();
        WorkManager workManager = WorkManager.getInstance();
        workManager.enqueue(periodicWorkRequest);
    }

    public void alarm() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        String cur_time_str = format.format(new Date());
        Date cur_time = new Date();
        try {
            cur_time = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss").parse(cur_time_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cur_time.setMinutes(cur_time.getMinutes() + 1);
        Intent intent = new Intent(this, RestarterBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cur_time.getTime(),pendingIntent);
        Log.e("dd", cur_time.toString());
    }
}
