package com.example.wykrywaczszerszeni;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.material.button.MaterialButton;

import java.io.IOException;

public class HomeActivity extends Activity {
    private MaterialButton recBtn = null;
    private MaterialButton playBtn = null;
    private Button analyzeBtn = null;
    private MediaRecorder recorder = null;
    private MediaPlayer player = null;
    boolean mStartRecording = true;
    boolean mStartPlaying = true;
    private TextView textViewStatus = null;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    private WaveFormView waveformView = null;
    Handler handler;
    Runnable updateCanvas;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
            textViewStatus.setText(R.string.status_play);

            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    textViewStatus.setText(R.string.status_choose);
                    playBtn.setIcon(getDrawable(R.drawable.play));
                }
            });
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
        textViewStatus.setText(R.string.status_choose);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
        waveformView.clearCanvas();
        handler = new Handler(Looper.getMainLooper());
        updateCanvas = new Runnable(){
            public void run(){
                waveformView.addAmplitude((float) recorder.getMaxAmplitude());
                handler.postDelayed(this,100);
            }
        };
        updateCanvas.run();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;

        handler.removeCallbacks(updateCanvas);
    }

    void identify(){
        textViewStatus.setText(R.string.status_init);
        analyzeBtn.setVisibility(View.GONE);
        playBtn.setVisibility(View.GONE);
        waveformView.clearCanvas();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                boolean hornet_identified = true;

                if (hornet_identified) {
                    Intent intent = new Intent(HomeActivity.this, DangerActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(HomeActivity.this, SafeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d("ggggg", "gggggggggg");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        textViewStatus = findViewById(R.id.textViewStatus);

        // Record to the external cache directory for visibility
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        waveformView = findViewById(R.id.waveformView);
        recBtn = findViewById(R.id.recBtn);

        recBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRecord(mStartRecording);
                if (mStartRecording) {
                    recBtn.setIcon(getDrawable(R.drawable.stop));
                    playBtn.setVisibility(View.GONE);
                    analyzeBtn.setVisibility(View.GONE);
                    textViewStatus.setText(R.string.status_listening);
                } else {
                    recBtn.setIcon(getDrawable(R.drawable.microphone));
                    playBtn.setVisibility(View.VISIBLE);
                    analyzeBtn.setVisibility(View.VISIBLE);
                    textViewStatus.setText(R.string.status_choose);
                }
                mStartRecording = !mStartRecording;
            }
        });

        analyzeBtn = findViewById(R.id.analyzeBtn);
        analyzeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identify();
            }
        });

        playBtn = findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPlay(mStartPlaying);

                if(mStartPlaying){
                    playBtn.setIcon(getDrawable(R.drawable.pause));
                }
                else {
                    playBtn.setIcon(getDrawable(R.drawable.play));
                }
                mStartPlaying = !mStartPlaying;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }
    }
}