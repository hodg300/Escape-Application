package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class EndActivity extends AppCompatActivity {
    public final String SHARE_PREFS = "sharedPrefs";
    public final String TEXT = "text";
    public final String CURRENT_PLAYER="currentPlayer";
    private TextView scoreView;
    private final String SCORE = "score";
    private final String NAME = "name";
    private ArrayList<Player> players_list;
    private Location userLocation;
    public final String CHECK_BOX = "check_box";
    private FusedLocationProviderClient client;
    private LocationManager locationManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        //create list of players
        players_list=new ArrayList<>();
        Intent intent=getIntent();


        getScoreFromGameActivity();
        listenerOfBtns();
        getLocation();//add lcurrent location to userLocation
        loadPlayersData();
        topTenHighScore(intent.getStringExtra(NAME),userLocation,intent.getIntExtra(SCORE,0));
        savePlayersData();

    }



    private void getLocation(){
        client = LocationServices.getFusedLocationProviderClient(this);
        //fetchLastLocation();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        userLocation = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
    }

    private void getScoreFromGameActivity() {
        //catch intent from GameActivity
        scoreView=findViewById(R.id.your_score);
        Intent intent=getIntent();
        scoreView.setText("YOUR SCORE : " + intent.getIntExtra(SCORE,0));
    }

    private void listenerOfBtns() {
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
                double[] latLng={userLocation.getLatitude(),userLocation.getLongitude()};
                EndActivityIntent.putExtra(CURRENT_PLAYER,latLng);
                startActivity(EndActivityIntent);

            }
        });
    }


    private void topTenHighScore(String name,Location location,int score){

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


            if(score < players_list.get(players_list.size()-1).getScore()){
                players_list.add(players_list.size(),new Player(name,location,score));
            }
            else if(score == players_list.get(players_list.size()-1).getScore()){
                players_list.add(players_list.size()-1,new Player(name,location,score));
            }
        }

    }


    private void savePlayersData(){
        SharedPreferences sharedPref = getSharedPreferences(SHARE_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String json=gson.toJson(players_list);
        editor.putString(TEXT,json);
        editor.apply();
    }

    private void loadPlayersData(){
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent EndActivityIntent=new Intent(EndActivity.this,StartActivity.class);
        startActivity(EndActivityIntent);
    }


}
