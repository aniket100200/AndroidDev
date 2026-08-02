package com.example.sharedpref;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(()->{
            SharedPreferences pref = getSharedPreferences(CacheConstants.Login.toString(),MODE_PRIVATE);
           boolean check= pref.getBoolean(CacheConstants.flag.toString(),false);

            Intent iNext;

           if(check){
               iNext= new Intent(MainActivity.this,HomeActivity.class);
           }else{
               iNext=new Intent(MainActivity.this, LoginActivity2.class);
           }

           startActivity(iNext);

           finish();

        },500);
    }
}