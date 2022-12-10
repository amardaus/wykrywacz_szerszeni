package com.example.wykrywaczszerszeni;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Vibrator;

public class DangerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger);
    }

    void vibrate(){
        long[] pattern = {0, 1000, 1000};
        Vibrator v = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        v.vibrate(pattern,0);
    }

    @Override
    protected void onStart() {
        super.onStart();

        vibrate();

        MediaPlayer mediaPlayer = MediaPlayer.create(DangerActivity.this, R.raw.danger_sound2);
        mediaPlayer.start();

        TextView resultDangerTextView = findViewById(R.id.resultDangerTextView);
        Button resultDangerBtn = findViewById(R.id.resultDangerBtn);

        Animation scaleAnimation = AnimationUtils.loadAnimation(this,R.anim.scale);
        int animationTime = 3000;
        scaleAnimation.setDuration(animationTime);
        ImageView beeImageView = findViewById(R.id.beeImageView);
        beeImageView.startAnimation(scaleAnimation);
        beeImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                resultDangerTextView.setVisibility(View.VISIBLE);
                resultDangerBtn.setVisibility(View.VISIBLE);
            }
        }, animationTime);

        resultDangerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation scaleAnimation2 = AnimationUtils.loadAnimation(DangerActivity.this,R.anim.scale2);
                int animationTime2 = 1000;
                scaleAnimation2.setDuration(animationTime2);
                beeImageView.startAnimation(scaleAnimation2);
                beeImageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DangerActivity.this.finish();
                    }
                }, animationTime2);
            }
        });
    }
}