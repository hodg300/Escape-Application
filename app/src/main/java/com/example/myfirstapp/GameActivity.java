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
import android.media.Image;
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
    private int NUM_OF_COL = 5;
    private final String SCORE = "score";
    private final String TEXT_SCORE = "SCORE: ";
    private View player;
    private View[] enemies = new View[NUM_OF_COL];
    private View enemy1;
    private View enemy2;
    private View enemy3;
    private View enemy4;
    private View enemy5;
    private View[] bonus_staff = new View[NUM_OF_COL];
    private View coin1;
    private View coin2;
    private View coin3;
    private View coin4;
    private View coin5;
    private ImageView life_status1;
    private ImageView life_status2;
    private ImageView life_status3;
    private ImageView btnLeft;
    private ImageView btnRight;
    private int life = 3;
    private ValueAnimator enemy1_anim;
    private ValueAnimator enemy2_anim;
    private ValueAnimator enemy3_anim;
    private ValueAnimator enemy4_anim;
    private ValueAnimator enemy5_anim;
    private ValueAnimator bonus1_anim;
    private ValueAnimator bonus2_anim;
    private ValueAnimator bonus3_anim;
    private ValueAnimator bonus4_anim;
    private ValueAnimator bonus5_anim;
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
        enemy4 = (View) findViewById(R.id.enemy4);
        enemy5 = (View) findViewById(R.id.enemy5);
        enemies[0]=enemy1;
        enemies[1]=enemy2;
        enemies[2]=enemy3;
        enemies[3]=enemy4;
        enemies[4]=enemy5;
        coin1  = (View) findViewById(R.id.coin1);
        coin2  = (View) findViewById(R.id.coin2);
        coin3  = (View) findViewById(R.id.coin3);
        coin4  = (View) findViewById(R.id.coin4);
        coin5  = (View) findViewById(R.id.coin5);
        bonus_staff[0]=coin1;
        bonus_staff[1]=coin2;
        bonus_staff[2]=coin3;
        bonus_staff[3]=coin4;
        bonus_staff[4]=coin5;
        life_status1 = (ImageView) findViewById(R.id.life_status1);
        life_status2 = (ImageView) findViewById(R.id.life_status2);
        life_status3 = (ImageView) findViewById(R.id.life_status3);
        scoreView=findViewById(R.id.score_view);


        //initial Buttons
        btnLeft=(ImageView) findViewById(R.id.move_left);
        btnRight=(ImageView) findViewById(R.id.move_right);



        //initial score
        scoreView.setText(TEXT_SCORE + '0');

        //initial enemies
        for(int i=0;i<NUM_OF_COL;i++) {
            enemies[i].setTranslationY(-130f);
            bonus_staff[i].setTranslationY(-260f);
        }


        //get screenHeight
        WindowManager wm=getWindowManager();
        Display disp= wm.getDefaultDisplay();
        Point size=new Point();
        disp.getSize(size);
        screenHeight=size.y;



        //move right
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.getX() < (getResources().getDisplayMetrics().widthPixels * (NUM_OF_COL-1) / NUM_OF_COL))
                    player.setX(player.getX() + getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
                scoreView.setTextColor(Color.WHITE);
            }
        });

        //move left
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player.getX() >= (getResources().getDisplayMetrics().widthPixels * 1 / NUM_OF_COL))
                    player.setX(player.getX() - getResources().getDisplayMetrics().widthPixels / NUM_OF_COL);
                scoreView.setTextColor(Color.WHITE);
            }
        });



//      create bonus animations----------------------------------------------------------
        bonus1_anim = ValueAnimator.ofInt(-260, screenHeight + 400);
        bonus1_anim.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        bonus1_anim.setStartDelay(10000);
        bonus1_anim.start();
        bonus1_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                    int animatedValue = (int) updatedAnimation.getAnimatedValue();
                    coin1.setTranslationY(animatedValue);
                    if (isCollision(coin1, player)) {
                        score += 500;
                        coin1.setY(-130);
                        scoreView.setTextColor(Color.GREEN);
                        updatedAnimation.start();
                    }
                }
            });

        bonus2_anim = ValueAnimator.ofInt(-260, screenHeight + 400);
        bonus2_anim.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        bonus2_anim.setStartDelay(22000);
        bonus2_anim.start();
        bonus2_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                int animatedValue = (int) updatedAnimation.getAnimatedValue();
                coin2.setTranslationY(animatedValue);
                if (isCollision(coin2, player)) {
                    score += 500;
                    coin2.setY(-130);
                    scoreView.setTextColor(Color.GREEN);
                    updatedAnimation.start();
                }
            }
        });

        bonus3_anim = ValueAnimator.ofInt(-260, screenHeight + 400);
        bonus3_anim.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        bonus3_anim.setStartDelay(10000);
        bonus3_anim.start();
        bonus3_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                int animatedValue = (int) updatedAnimation.getAnimatedValue();
                coin3.setTranslationY(animatedValue);
                if (isCollision(coin3, player)) {
                    score += 500;
                    coin3.setY(-130);
                    scoreView.setTextColor(Color.GREEN);
                    updatedAnimation.start();
                }
            }
        });

        bonus4_anim = ValueAnimator.ofInt(-260, screenHeight + 400);
        bonus4_anim.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        bonus4_anim.setStartDelay(17000);
        bonus4_anim.start();
        bonus4_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                int animatedValue = (int) updatedAnimation.getAnimatedValue();
                coin4.setTranslationY(animatedValue);
                if (isCollision(coin4, player)) {
                    score += 500;
                    coin4.setY(-130);
                    scoreView.setTextColor(Color.GREEN);
                    updatedAnimation.start();
                }
            }
        });

        bonus5_anim = ValueAnimator.ofInt(-260, screenHeight + 400);
        bonus5_anim.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        bonus5_anim.setStartDelay(12000);
        bonus5_anim.start();
        bonus5_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                int animatedValue = (int) updatedAnimation.getAnimatedValue();
                coin5.setTranslationY(animatedValue);
                if (isCollision(coin5, player)) {
                    score += 500;
                    coin5.setY(-130);
                    scoreView.setTextColor(Color.GREEN);
                    updatedAnimation.start();
                }
            }
        });


        
        
//        create animation------------------------------------------------------------------
        enemy1_anim = ValueAnimator.ofInt(-130,screenHeight+400);
        enemy1_anim.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        enemy1_anim.setStartDelay(200);
        enemy1_anim.start();
        enemy1_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
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
        enemy2_anim = ValueAnimator.ofInt(-130,screenHeight +400);
        enemy2_anim.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        enemy2_anim.setStartDelay(2200);
        enemy2_anim.start();
        enemy2_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
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

        enemy3_anim = ValueAnimator.ofInt(-130,screenHeight +400);
        enemy3_anim.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        enemy3_anim.setStartDelay(1100);
        enemy3_anim.start();
        enemy3_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
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


        enemy4_anim = ValueAnimator.ofInt(-130,screenHeight +400);
        enemy4_anim.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        enemy4_anim.setStartDelay(1500);
        enemy4_anim.start();
        enemy4_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                enemy4.setTranslationY(animatedValue);
                if(isCollision(enemy4,player)) {
                    enemy4.setY(-130);
                    hitCheck();
                    updatedAnimation.start();
                }
                addScore(enemy4,updatedAnimation);
            }
        });



        enemy5_anim = ValueAnimator.ofInt(-130,screenHeight +400);
        enemy5_anim.setDuration(SPEED).setRepeatCount(Animation.INFINITE);
        enemy5_anim.setStartDelay(1300);
        enemy5_anim.start();
        enemy5_anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                int animatedValue = (int)updatedAnimation.getAnimatedValue();
                enemy5.setTranslationY(animatedValue);
                if(isCollision(enemy5,player)) {
                    enemy5.setY(-130);
                    hitCheck();
                    updatedAnimation.start();
                }
                addScore(enemy5,updatedAnimation);
            }
        });


    }

    ///-----------------------Method--------------------------------------

    private synchronized void addScore(View enemy,ValueAnimator updatedAnimation){
        if(enemy.getY()>player.getY()+player.getHeight()){
            score +=100;
            if(score<9999) {
                scoreView.setText(TEXT_SCORE + score);
            }else{
                scoreView.setText(TEXT_SCORE +score);
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
                    gameActivityIntent.putExtra(SCORE,score);
                    startActivity(gameActivityIntent);
                    finish();
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
        enemy1_anim.pause();
        enemy2_anim.pause();
        enemy3_anim.pause();
        enemy4_anim.pause();
        enemy5_anim.pause();
        findViewById(R.id.move_left).setEnabled(false);
        findViewById(R.id.move_right).setEnabled(false);
        mpBackground.pause();

    }

    public void clickToResume(View view) {
        enemy1_anim.resume();
        enemy2_anim.resume();
        enemy3_anim.resume();
        enemy4_anim.resume();
        enemy5_anim.resume();
        findViewById(R.id.move_left).setEnabled(true);
        findViewById(R.id.move_right).setEnabled(true);
        mpBackground.start();

    }

    public void clickToStop(View view) {
        onStop();
        Intent gameActivityIntent = new Intent(GameActivity.this, EndActivity.class);
        gameActivityIntent.putExtra(SCORE,score);
        startActivity(gameActivityIntent);
        finish();
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
        enemy1_anim.pause();
        enemy2_anim.pause();
        enemy3_anim.pause();
        enemy4_anim.pause();
        enemy5_anim.pause();
        mpBackground.pause();
    }


    //disable back press
    @Override
    public void onBackPressed() {

    }
}

