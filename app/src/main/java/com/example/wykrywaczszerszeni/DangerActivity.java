package com.example.wykrywaczszerszeni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Vibrator;

public class DangerActivity extends AppCompatActivity {

    MediaPlayer mediaPlayer;
    CameraManager mCameraManager;
    String mCameraId = null;
    Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danger);
    }

    void vibrate(){
        long[] pattern = {0, 1000, 1000};
        vib = (Vibrator) getSystemService(this.VIBRATOR_SERVICE);
        vib.vibrate(pattern,0);
    }

    void flash(){
        boolean isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if(isFlashAvailable){
            mCameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
            try {
                mCameraId = mCameraManager.getCameraIdList()[0];
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mCameraManager.setTorchMode(mCameraId, true);
                    }
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        turnOffFlash();
        mediaPlayer.stop();
        vib.cancel();
    }

    void turnOffFlash(){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, false);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        vibrate();
        flash();

        mediaPlayer = MediaPlayer.create(DangerActivity.this, R.raw.danger_sound2);
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