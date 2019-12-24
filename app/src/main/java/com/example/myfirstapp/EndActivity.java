package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.*;
import android.widget.Toast;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class EndActivity extends AppCompatActivity {
    public static final String SHARE_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    private TextView scoreView;
    private final String SCORE = "score";
    private final String NAME = "name";
    private ArrayList<Player> players_list;
    private Location userLocation;
    public final String CHECK_BOX = "check_box";
    private FusedLocationProviderClient client;
    private LocationManager locationManager;
    private String lattitude;
    private String lonitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        //get current location of player
        client = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(EndActivity.this,
                    "Enter your name", Toast.LENGTH_LONG).show();
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        if(location!=null){
            double lat=location.getLatitude();
            double lon=location.getLongitude();
            lattitude=String.valueOf(lat);
            lonitude=String.valueOf(lon);
        }



        players_list=new ArrayList<>();
        scoreView=findViewById(R.id.your_score);
        Intent intent=getIntent();
        scoreView.setText("YOUR SCORE : " + intent.getIntExtra(SCORE,0));

        findViewById(R.id.btn_start_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivityIntent=new Intent(EndActivity.this,StartActivity.class);
                startActivity(gameActivityIntent);
                finish();
            }
        });


        findViewById(R.id.btn_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EndActivityIntent=new Intent(EndActivity.this,GameActivity.class);
                Intent intent= getIntent();
                boolean check_box= intent.getBooleanExtra(CHECK_BOX,false);
                EndActivityIntent.putExtra(CHECK_BOX,check_box);
                EndActivityIntent.putExtra(NAME,getIntent().getStringExtra(NAME));
                startActivity(EndActivityIntent);
                finish();
            }
        });

        findViewById(R.id.btn_quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
                finish();
            }
        });

        findViewById(R.id.btn_high_scores).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent EndActivityIntent=new Intent(EndActivity.this,ScoreActivity.class);
                startActivity(EndActivityIntent);

            }
        });

        loadData();
        topTen(intent.getStringExtra(NAME),location,intent.getIntExtra(SCORE,0));
        saveData();

    }



    private void fetchLastLocation() {
        Task<Location> task=client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location !=null){
                    userLocation=location;
                }
            }
        });
    }


    private void topTen(String name,Location location,int score){

            if(players_list.size()<10 ) {
                findPlace(name,location,score);


            }else{
                if(score >= players_list.get(players_list.size()-1).getScore()) {
                    players_list.remove(players_list.size() - 1);
                    findPlace(name,location,score);

                }
            }


    }

    private void findPlace(String name,Location location,int score) {
        if(players_list.size()==0){
            players_list.add(new Player(name,location,score));
        }else {
            for (Player p : players_list) {
                if (score > p.getScore()) {
                    players_list.add(players_list.indexOf(p), new Player(name,location,score));
                    break;
                }
            }

            //maybe have problems!!!!!
            if(score < players_list.get(players_list.size()-1).getScore()){
                players_list.add(players_list.size(),new Player(name,location,score));
            }
            else if(score == players_list.get(players_list.size()-1).getScore()){
                players_list.add(players_list.size()-1,new Player(name,location,score));
            }
        }

    }


    private void saveData(){
        SharedPreferences sharedPref = getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json=gson.toJson(players_list);
        editor.putString(TEXT,json);
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPref = getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        Gson gson = new Gson();
        String json=sharedPref.getString(TEXT,null);
        Type type= new TypeToken<ArrayList<Player>>(){}.getType();
        players_list=gson.fromJson(json,type);
        if(players_list ==null){
            players_list=new ArrayList<>();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }


}
