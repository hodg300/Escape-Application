package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Rectangle;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private int NUM_OF_COL = 3;
    private View player;
    private View enemy1;
    private View enemy2;
    private View enemy3;
    private ImageView life_status1;
    private ImageView life_status2;
    private ImageView life_status3;
    private int life = 3;
    private ValueAnimator animation1;
    private ValueAnimator animation2;
    private ValueAnimator animation3;
    private int screenHeight;
    private int score=0;
    private TextView scoreView;
    private final int SPEED=3000;
    private MediaPlayer mpBackground;
    private final static int MAX_VOLUME = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        //MediaPlayer mpBackground
        mpBackground = MediaPlayer.create(getApplicationContext(),R.raw.starwars);
        mpBackground.setLooping(true);
        final float volume = (float) (1 - (Math.log(MAX_VOLUME - 5) / Math.log(MAX_VOLUME)));
        mpBackground.setVolume(volume, volume);
        mpBackground.start();

        //initial views
        player = (View) findViewById(R.id.player);
        enemy1 = (View) findViewById(R.id.enemy1);
        enemy2 = (View) findViewById(R.id.enemy2);
        enemy3 = (View) findViewById(R.id.enemy3);
        life_status1 = (ImageView) findViewById(R.id.life_status1);
        life_status2 = (ImageView) findViewById(R.id.life_status2);
        life_status3 = (ImageView) findViewById(R.id.life_status3);
        scoreView=findViewById(R.id.score_view);


        //initial score
        scoreView.setText("SCORE: " + '0');

        //initial enemies
        enemy1.setTranslationY(-130);
        enemy2.setTranslationY(-130);
        enemy3.setTranslationY(-130);

        //get screenHeight
        WindowManager wm=getWindowManager();
        Display disp= wm.getDefaultDisplay();
        Point size=new Point();
        disp.getSize(size);
        screenHeight=size.y;

        //create animation
        animation1 = ValueAnimator.ofInt(-130,screenHeight+400);
        animation1.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        animation1.setStartDelay(200);
        animation1.start();
        animation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                enemy1.setTranslationY(animatedValue);
              if(isCollision(enemy1,player)) {
                  enemy1.setY(-130);
                  hitCheck();
                  updatedAnimation.start();
              }
                addScore(enemy1,updatedAnimation);
            }
        });
        animation2 = ValueAnimator.ofInt(-130,screenHeight +400);
        animation2.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        animation2.setStartDelay(2200);
        animation2.start();
        animation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();

                enemy2.setTranslationY(animatedValue);
              if(isCollision(enemy2,player)) {
                  enemy2.setY(-130);
                  hitCheck();
                  updatedAnimation.start();
              }
                addScore(enemy2,updatedAnimation);
            }
        });

        animation3 = ValueAnimator.ofInt(-130,screenHeight +400);
        animation3.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        animation3.setStartDelay(1100);
        animation3.start();
        animation3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                enemy3.setTranslationY(animatedValue);
                if(isCollision(enemy3,player)) {
                    enemy3.setY(-130);
                    hitCheck();
                    updatedAnimation.start();
                }
                addScore(enemy3,updatedAnimation);
            }
        });


    }

    private synchronized void addScore(View enemy,ValueAnimator updatedAnimation){
        if(enemy.getY()>player.getY()+player.getHeight()){
            score +=100;
            if(score<9999) {
                scoreView.setText("SCORE: " + score);
            }else{
                scoreView.setText("SCORE: " +score);
                scoreView.setTextSize(20);
            }
            updatedAnimation.start();
        }
    }

    private synchronized  void hitCheck() {

            this.life--;
                if (life == 0) {
                    life_status1.setVisibility(View.INVISIBLE);
                    Intent gameActivityIntent = new Intent(GameActivity.this, EndActivity.class);
                    gameActivityIntent.putExtra("score",score);
                    startActivity(gameActivityIntent);
                } else if (life == 1) {
                    life_status2.setVisibility(View.INVISIBLE);

                } else if (life == 2) {
                    life_status3.setVisibility(View.INVISIBLE);

                }
        return;
    }

    private boolean isCollision(View e,View p) {
        int[] enemy_locate = new int[2];
        int[] player_locate = new int[2];

        e.getLocationOnScreen(enemy_locate);
        p.getLocationOnScreen(player_locate);

        Rect rect1=new Rect(enemy_locate[0],enemy_locate[1],(int)(enemy_locate[0]+ e.getWidth()),(int)(enemy_locate[1]+e.getHeight()));
        Rect rect2=new Rect(player_locate[0],player_locate[1],(int)(player_locate[0]+ p.getWidth()),(int)(player_locate[1]+p.getHeight()));

        return Rect.intersects(rect1,rect2);
    }

    public void clickToPause(View view) {
        animation1.pause();
        animation2.pause();
        animation3.pause();
        findViewById(R.id.move_left).setEnabled(false);
        findViewById(R.id.move_right).setEnabled(false);
        mpBackground.pause();

    }

    public void clickToResume(View view) {
        animation1.resume();
        animation2.resume();
        animation3.resume();
        findViewById(R.id.move_left).setEnabled(true);
        findViewById(R.id.move_right).setEnabled(true);
        mpBackground.start();

    }

    public void clickToStop(View view) {
        onStop();
        Intent gameActivityIntent = new Intent(GameActivity.this, EndActivity.class);
        gameActivityIntent.putExtra("score",score);
        startActivity(gameActivityIntent);
    }

    public void clickToMoveRight(View view) {
        if (player.getX() < (getResources().getDisplayMetrics().widthPixels * 2 / NUM_OF_COL))
            player.setX(player.getX() + getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
    }

    public void clickToMoveLeft(View view) {
        if (player.getX() >= (getResources().getDisplayMetrics().widthPixels * 1 / NUM_OF_COL))
            player.setX(player.getX() - getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mpBackground.pause();
        findViewById(R.id.move_left).setEnabled(false);
        findViewById(R.id.move_right).setEnabled(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!findViewById(R.id.btn_pause).isEnabled()) {
            mpBackground.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        animation1.pause();
        animation2.pause();
        animation3.pause();
        mpBackground.pause();
    }
}

