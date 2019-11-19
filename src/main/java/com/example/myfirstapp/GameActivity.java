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
import android.widget.ImageView;
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
    private View player;
    private Button btnLeft;
    private Button btnRight;
    private GridLayout gl;
    private View enemyView0;
    private View enemyView1;
    private View enemyView2;
    private ImageView life_status1;
    private ImageView life_status2;
    private ImageView life_status3;
    private float posY;
    private int life=3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //create handler thread
        handlerThread.start();
        myHandler=new Handler(handlerThread.getLooper());

        player = (View)findViewById(R.id.player);
        btnLeft = findViewById(R.id.move_left);
        btnRight = findViewById(R.id.move_right);
        gl = (GridLayout) findViewById(R.id.grid_layout);
        enemyView0 = (View) findViewById(R.id.enemy_view0);
        enemyView1 = (View) findViewById(R.id.enemy_view1);
        enemyView2 = (View) findViewById(R.id.enemy_view2);
        life_status1= (ImageView)findViewById(R.id.life_status1);
        life_status2= (ImageView)findViewById(R.id.life_status2);
        life_status3= (ImageView)findViewById(R.id.life_status3);

        player.setX(0);

        //move out of the grid layout
        enemyView0.setY(gl.getHeight());
        enemyView1.setY(gl.getHeight());
        enemyView2.setY(gl.getHeight());

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
                if (player.getX() >= (getResources().getDisplayMetrics().widthPixels* 1 / NUM_OF_COL))
                    player.setX(player.getX() - getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
                Log.d("state", "onClick: " + player.getX());
            }
        });
        //move right
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.getX() < (getResources().getDisplayMetrics().widthPixels* 2 / NUM_OF_COL))
                    player.setX(player.getX() + getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
                Log.d("state", "onClick: " + player.getX());

            }
        });

        //select random initial enemy
        int random_col;
        //main process , this part check our life and play all of threads
        switch(NUM_OF_COL) {
            case 3:
                Log.d("hod", "onCreate: " + player.getY());
//                while (life +1 > 2) {
                for(int i=0;i<7;i++) {

                    random_col = (int) (Math.random() * (NUM_OF_COL));
                    if (random_col == 0) {
                        enemyView0.setBackgroundColor(Color.RED);
                        firstColThread();
                    } else if (random_col == 1) {
                        enemyView1.setBackgroundColor(Color.RED);
                        secondColThread();
                    } else if (random_col == 2) {
                        enemyView2.setBackgroundColor(Color.RED);
                        thirdColThread();
                    }
//                }
                }

                break;
        }


    }
        //here there is a problem , i need to check getx and gety
    private void hitCheck(float x,float y) {
        if(x >=player.getX() && y>=player.getY()){
            life--;
            switch (life+1) {
                case 1:
                    life_status1.setVisibility(View.INVISIBLE);
                    Intent gameActivityIntent = new Intent(GameActivity.this, EndActivity.class);
                    startActivity(gameActivityIntent);
                    break;
                case 2:
                    life_status2.setVisibility(View.INVISIBLE);
                    break;
                case 3:
                    life_status3.setVisibility(View.INVISIBLE);
                    break;
            }
        }
        return;
    }

    private void thirdColThread() {
        thirdThread=new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                    myHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            while (enemyView2.getY() < gl.getHeight()) {
                                try {
                                    changePos(enemyView2);
                                    hitCheck(enemyView2.getX(),enemyView2.getY());
                                    Thread.sleep(500);
                                } catch (Exception c) {

                                }
                            }
                        }
                    });


                } catch (Exception c) {
                }

            }
        });
        thirdThread.start();
    }

    private void secondColThread() {
        secondThread=new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(700);
                    myHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            while (enemyView1.getY() < gl.getHeight()) {
                                try {
                                    changePos(enemyView1);
                                    hitCheck(enemyView1.getX(),enemyView1.getY());
                                    Thread.sleep(500);
                                } catch (Exception c) {

                                }
                            }
                        }
                    });


                } catch (Exception c) {
                }

            }
        });
        secondThread.start();
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
                            while (enemyView0.getY() < gl.getHeight()) {
                                try {
                                    changePos(enemyView0);
                                    hitCheck(enemyView0.getX(),enemyView0.getY());
                                    Thread.sleep(500);
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
    public void changePos(View v) {
        posY += 130;
        v.setY(posY);
        Log.d("askfas", "changePos: " + v.getHeight());
    }

}