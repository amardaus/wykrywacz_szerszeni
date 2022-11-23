package com.example.wykrywaczszerszeni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class SafeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe);

        Button resultSafeBtn = findViewById(R.id.resultSafeBtn);

        resultSafeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SafeActivity.this.finish();
            }
        });
    }
}