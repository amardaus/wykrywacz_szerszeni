package com.example.wykrywaczszerszeni;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
    }

    @Override
    protected void onStart() {
        super.onStart();

        MediaPlayer mediaPlayer = MediaPlayer.create(ResultActivity.this, R.raw.danger2);
        mediaPlayer.start();

        TextView identifiedTextView = findViewById(R.id.identifiedTextView);
        Button identifiedBtn = findViewById(R.id.identifiedBtn);

        Animation scaleAnimation = AnimationUtils.loadAnimation(this,R.anim.scale);
        int animationTime = 3000;
        scaleAnimation.setDuration(animationTime);
        ImageView beeImageView = findViewById(R.id.beeImageView);
        beeImageView.startAnimation(scaleAnimation);
        beeImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                identifiedTextView.setVisibility(View.VISIBLE);
                identifiedBtn.setVisibility(View.VISIBLE);
            }
        }, animationTime);

        identifiedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation scaleAnimation2 = AnimationUtils.loadAnimation(ResultActivity.this,R.anim.scale2);
                int animationTime2 = 1000;
                scaleAnimation2.setDuration(animationTime2);
                beeImageView.startAnimation(scaleAnimation2);
                beeImageView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ResultActivity.this.finish();
                    }
                }, animationTime2);
            }
        });
    }
}