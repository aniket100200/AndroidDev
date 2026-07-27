package com.example.switchingbetweenactivites;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        AppCompatButton nextBtn=findViewById(R.id.btnNext);

        nextBtn.setOnClickListener((v)->{
            Intent iNext;
            iNext=new Intent(MainActivity.this, SecondActivity.class);
            /**
             * key Value Pair
             */
            iNext.putExtra("title","Home");
            iNext.putExtra("rollNo",10);
            iNext.putExtra("name","Aniket");
            startActivity(iNext);
        });
    }
}