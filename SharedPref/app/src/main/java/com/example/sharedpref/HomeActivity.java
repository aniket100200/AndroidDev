package com.example.sharedpref;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    AppCompatButton logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        logout=findViewById(R.id.btnLogout);

        logout.setOnClickListener((v)->{
            SharedPreferences pref= getSharedPreferences(CacheConstants.Login.toString(),MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putBoolean(CacheConstants.flag.toString(),false);

            editor.apply();

            /**
             * let's move to Login activity
             */

            Intent intent=new Intent(HomeActivity.this, LoginActivity2.class);
            startActivity(intent);

            finishAffinity(); //will remove all the activites form the stack
        });

    }
}