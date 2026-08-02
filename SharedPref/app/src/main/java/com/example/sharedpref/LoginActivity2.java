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

public class LoginActivity2 extends AppCompatActivity {

    AppCompatButton loginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login2);

        loginBtn=findViewById(R.id.btnLogin);

        loginBtn.setOnClickListener((v)->{
            //code for verification
            SharedPreferences pref= getSharedPreferences(CacheConstants.Login.toString(),MODE_PRIVATE);
            SharedPreferences.Editor editor=pref.edit();

            editor.putBoolean(CacheConstants.flag.toString(),true);

            editor.apply();

            /**
             * let's go to the Home Activity
             */

            Intent intent = new Intent(LoginActivity2.this,HomeActivity.class);

            startActivity(intent);

        });

    }
}