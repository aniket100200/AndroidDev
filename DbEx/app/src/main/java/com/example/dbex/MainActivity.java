package com.example.dbex;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dbex.model.Contact;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        MyDbHelper helper=new MyDbHelper(this);

        helper.addContact(new Contact("Aniket","+91 93595626454"));
        helper.addContact(new Contact("Ojas","+91 9359407416"));
        helper.addContact(new Contact("Aniket","+91 9665178772"));
        helper.addContact(new Contact("Vedan","+91 9322462271"));
        helper.addContact(new Contact("Pranay","+91 9096891333"));

    }
}