package com.example.wykrywaczszerszeni;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    private TextView startRec, stopRec, startPlay, stopPlay, statusTextView;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static String mFilename = null;

    // constant for storing audio permission
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        startRec = findViewById(R.id.recStart);
        stopRec = findViewById(R.id.recStop);

        startRec.setVisibility(View.VISIBLE);
        stopRec.setVisibility(View.GONE);

        //startPlay = findViewById(R.id.btnPlay);
        //stopPlay = findViewById(R.id.btnStopPlay);
        statusTextView = findViewById(R.id.textViewStatus);


        startRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecording();
            }
        });
        stopRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopRecording();
            }
        });
        /*startPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlaying();
            }
        });
        stopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
            }
        });*/
    }

    private void startRecording() {
        if (checkPermissions()) {

            /*startRec.setBackgroundColor(getResources().getColor(R.color.ugly_yellow));
            stopRec.setBackgroundColor(getResources().getColor(R.color.dark_brown));
            startPlay.setBackgroundColor(getResources().getColor(R.color.gray));
            stopPlay.setBackgroundColor(getResources().getColor(R.color.gray));
            */
            startRec.setVisibility(View.GONE);
            stopRec.setVisibility(View.VISIBLE);

            mFilename = getExternalCacheDir().getAbsolutePath();
            mFilename += "/audiorecordtest.3gp";
            Log.d("FILE: ", mFilename);
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(mFilename);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setAudioSamplingRate(16000);

            try {
                mRecorder.prepare();
            }
            catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }

            try{
                mRecorder.start();
                statusTextView.setText("Recording Started");
            }
            catch (Exception e){
                Log.e("TAG", "start() failed");
            }
        }
        else {
            RequestPermissions();
        }
    }

    private void stopRecording() {
        /*stopRec.setBackgroundColor(getResources().getColor(R.color.gray));
        startRec.setBackgroundColor(getResources().getColor(R.color.ugly_yellow));
        startPlay.setBackgroundColor(getResources().getColor(R.color.ugly_yellow));
        stopPlay.setBackgroundColor(getResources().getColor(R.color.ugly_yellow));
        */


        /*mRecorder.stop();
        mRecorder.release();
        mRecorder = null;*/

        //Log.d("AMPL", String.valueOf(mRecorder.getMaxAmplitude()));

        //mRecorder.stop();

        //MediaPlayer warningSound = MediaPlayer.create(HomeActivity.this, R.raw.sound);
        //warningSound.start();
        //statusTextView.setText("Recording Stopped");

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(HomeActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startPlaying() {
        /*stopRec.setBackgroundColor(getResources().getColor(R.color.gray));
        startRec.setBackgroundColor(getResources().getColor(R.color.purple_200));
        startPlay.setBackgroundColor(getResources().getColor(R.color.gray));
        stopPlay.setBackgroundColor(getResources().getColor(R.color.purple_200));*/

        // for playing our recorded audio
        // we are using media player class.
        mPlayer = new MediaPlayer();
        try {
            // below method is used to set the
            // data source which will be our file name
            mPlayer.setDataSource(mFilename);

            // below method will prepare our media player
            mPlayer.prepare();

            // below method will start our media player.
            mPlayer.start();
            statusTextView.setText("Recording Started Playing");
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
        stopPlay.setBackgroundColor(getResources().getColor(R.color.gray));
        startPlay.setBackgroundColor(getResources().getColor(R.color.purple_200));
        startPlay.setBackgroundColor(getResources().getColor(R.color.purple_200));
        stopPlay.setBackgroundColor(getResources().getColor(R.color.gray));
        statusTextView.setText("Recording Play Stopped");
    }

    public boolean checkPermissions() {
        // this method is used to check permission
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }
}