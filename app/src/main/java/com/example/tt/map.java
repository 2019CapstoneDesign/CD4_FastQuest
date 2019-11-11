package com.example.tt;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

public class map extends AppCompatActivity implements OnMapReadyCallback {
    private FragmentManager fragmentManager;
    private MapFragment mapFragment;
    private FusedLocationProviderClient mfusedLocationProviderClient;
    public GoogleMap gmap;
    TextView actTextView;
    //Button playing;
    Marker startMarker;
    Marker goalMarker;
    double goal_lat;
    double goal_lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        actTextView= (TextView)findViewById(R.id.activity_text);
        actTextView.setText(getIntent().getExtras().getString("title"));
        //playing = (Button)findViewById(R.id.playing);
        goal_lat = getIntent().getExtras().getDouble("latitude");
        goal_lng = getIntent().getExtras().getDouble("longitude");

        fragmentManager = getFragmentManager();
        mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        /*playing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),createreview.class));
            }
        });*/


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gmap = googleMap;
        //Location cur_location = mfusedLocationProviderClient.getLastLocation().getResult();
        //LatLng location = new LatLng(cur_location.getLatitude(), cur_location.getLongitude()); //현재위치

        //goal_lat = 37.506840;
        //goal_lng = 126.953;
        LatLng goal = new LatLng(goal_lat, goal_lng); //목적지

        MarkerOptions goalmarkeroption = new MarkerOptions();
        goalmarkeroption.title("목적지");
        goalmarkeroption.snippet("여기까지");
        goalmarkeroption.position(goal);
        goalMarker = googleMap.addMarker((goalmarkeroption));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(goal, (float)15));
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return;
        }
        mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    LatLng mycurloc =  new LatLng(37.4963 ,126.9388);
                    MarkerOptions startMarkeroption = new MarkerOptions();
                    startMarkeroption.position(mycurloc);
                    startMarkeroption.title("현재 위치 : " + location.getLatitude() + ", "  + location.getLongitude());
                    startMarker = googleMap.addMarker(startMarkeroption);
                    LatLng camera = new LatLng((startMarker.getPosition().latitude + goalMarker.getPosition().latitude)/2, (startMarker.getPosition().longitude + goalMarker.getPosition().longitude) / 2);
                    double zoom = 20 - (Math.log(Math.max(Math.abs(startMarker.getPosition().latitude - goalMarker.getPosition().latitude), Math.abs(startMarker.getPosition().longitude - goalMarker.getPosition().longitude)) / 0.00035) / Math.log(2));
                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, (float)zoom));
                }
                else{
                    new AlertDialog.Builder(map.this).setMessage("현재 위치를 받을 수 없습니다.").setPositiveButton("OK",null).show();
                }
            }
        });



    }

/*
    public void currentLocation(View view) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
            return;
        }
        mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location != null) {
                    LatLng mycurloc =  new LatLng(location.getLatitude(), location.getLongitude());
                    startMarker.setPosition(mycurloc);
                    startMarker.setTitle("현재 위치 : " + location.getLatitude() + ", "  + location.getLongitude());
                    LatLng camera = new LatLng((startMarker.getPosition().latitude + goalMarker.getPosition().latitude)/2, (startMarker.getPosition().longitude + goalMarker.getPosition().longitude) / 2);
                    double zoom = 20 - (Math.log(Math.max(Math.abs(startMarker.getPosition().latitude - goalMarker.getPosition().latitude), Math.abs(startMarker.getPosition().longitude - goalMarker.getPosition().longitude)) / 0.00035) / Math.log(2));
                    gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, (float)zoom));
                }
            }
        });

    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "권한 체크 거부 됌", Toast.LENGTH_SHORT).show();
                }
                else {
                    mfusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null) {
                                LatLng mycurloc =  new LatLng(location.getLatitude(), location.getLongitude());
                                startMarker.setPosition(mycurloc);
                                startMarker.setTitle("현재 위치 : " + location.getLatitude() + ", "  + location.getLongitude());
                                LatLng camera = new LatLng((startMarker.getPosition().latitude + goalMarker.getPosition().latitude)/2, (startMarker.getPosition().longitude + goalMarker.getPosition().longitude) / 2);
                                double zoom = 20 - (Math.log(Math.max(Math.abs(startMarker.getPosition().latitude - goalMarker.getPosition().latitude), Math.abs(startMarker.getPosition().longitude - goalMarker.getPosition().longitude)) / 0.00035) / Math.log(2));
                                gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(camera, (float)zoom));
                            }
                        }
                    });
                }
        }
    }
}
