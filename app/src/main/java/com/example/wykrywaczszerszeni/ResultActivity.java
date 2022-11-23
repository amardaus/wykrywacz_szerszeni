package com.example.wykrywaczszerszeni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView identifiedTextView = findViewById(R.id.identifiedTextView);
        Button identifiedBtn = findViewById(R.id.identifiedBtn);

        identifiedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResultActivity.this.finish();
            }
        });

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
    }
}