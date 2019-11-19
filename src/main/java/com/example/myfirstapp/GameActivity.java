package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private Handler myHandler ;
    private HandlerThread handlerThread=new HandlerThread("HandlerThread");
    private Thread firstThread;
    private Thread secondThread;
    private Thread thirdThread;
    private int NUM_OF_COL = 3;
    private Button player;
    private Button btnLeft;
    private Button btnRight;
    private GridLayout gl;
    private View enemyView;
    private float posY;
    private int life=3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //create handler thread
        handlerThread.start();
        myHandler=new Handler(handlerThread.getLooper());

        player = findViewById(R.id.btn_player);
        btnLeft = findViewById(R.id.move_left);
        btnRight = findViewById(R.id.move_right);
        gl = (GridLayout) findViewById(R.id.grid_layout);
        enemyView = (View) findViewById(R.id.enemy_view);

        //move out of the grid layout

        enemyView.setY(gl.getHeight());

        //stop game and going to end screen
        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameActivityIntent = new Intent(GameActivity.this, EndActivity.class);
                startActivity(gameActivityIntent);
            }
        });


        //move left
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.getX() >= 360)
                    player.setX(player.getX() - getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);

            }
        });
        //move right
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.getX() < 720)
                    player.setX(player.getX() + getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
                Log.d("state", "onClick: " + getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);

            }
        });


        //select random initial enemy
        int random_col;

        enemyView.setBackgroundColor(Color.RED);


        switch(NUM_OF_COL) {
            case 3:
                while (life > 0) {
                    checkLife();
                    random_col = (int) (Math.random() * (NUM_OF_COL - 1));
                    if (random_col == 0) {
                        firstColThread();
                    } else if (random_col == 1) {
                        secondColThread();
                    } else if (random_col == 2) {
                        thirdColThread();
                    }
                }
        }


    }

    private void checkLife() {
    }

    private void thirdColThread() {
    }

    private void secondColThread() {
    }

    private void firstColThread(){
        //move down the enemies
        firstThread=new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(500);
                    myHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            while (enemyView.getY() < gl.getHeight()) {

                                changePos();
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception c) {

                                }
                            }
                        }
                    });


                } catch (Exception c) {
                }

            }
        });
        firstThread.start();

    }

    //move down
    public void changePos() {


        posY += 130;
        enemyView.setY(posY);
    }

}
