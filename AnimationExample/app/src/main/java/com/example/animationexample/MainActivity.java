package com.example.animationexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;


public class MainActivity extends AppCompatActivity {

    AppCompatButton btnTranslate,btnAlpha,btnRotate,btnScale;
    LottieAnimationView lottieView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        btnTranslate= findViewById(R.id.btnTranslate);
//        btnAlpha=findViewById(R.id.btnAlpha);
//        btnRotate=findViewById(R.id.btnRotate);
//        btnScale=findViewById(R.id.btnScale);
//
//
//        TextView txtAnim=findViewById(R.id.txtAnim);
//        Animation move= AnimationUtils.loadAnimation(MainActivity.this,R.anim.move);
//
//        btnTranslate.setOnClickListener((view)->{
////            txtAnim.setAnimation(move);
//            txtAnim.startAnimation(move);
//        });
//
//        btnRotate.setOnClickListener((v)->{
//            Animation rotate=AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate);
//            txtAnim.startAnimation(rotate);
//        });
//
//        btnAlpha.setOnClickListener((v)->{
//            Animation alpha=AnimationUtils.loadAnimation(MainActivity.this,R.anim.alpha);
//            txtAnim.startAnimation(alpha);
//        });
//
//        btnScale.setOnClickListener((v)->{
//            Animation scale=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale);
//            txtAnim.startAnimation(scale);
//        });

        lottieView=findViewById(R.id.lottie_anim1);
        lottieView.setAnimation(R.raw.liquid_loader);
        lottieView.playAnimation();
        lottieView.loop(false);
    }
}