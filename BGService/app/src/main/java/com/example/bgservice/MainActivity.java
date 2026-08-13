package com.example.bgservice;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    AppCompatButton btnStart,btnStop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnStart=findViewById(R.id.btnStart);
        btnStop=findViewById(R.id.btnStop);

        btnStart.setOnClickListener((v)->{
            startService(new Intent(MainActivity.this, MusicService.class));
        });

        btnStop.setOnClickListener(v->{
            stopService(new Intent(MainActivity.this, MusicService.class));
        });

    }
}