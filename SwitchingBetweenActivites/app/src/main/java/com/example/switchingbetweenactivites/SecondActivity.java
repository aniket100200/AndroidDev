package com.example.switchingbetweenactivites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        Intent fromActivity=getIntent();

        var name = fromActivity.getStringExtra("name");
        var title=fromActivity.getStringExtra("title");
        var rollNo=fromActivity.getIntExtra("rollNo",0);

        TextView txtName=findViewById(R.id.name);
        TextView txtRollNo=findViewById(R.id.rollNo);

        txtName.setText(name);
        txtRollNo.setText(String.valueOf(rollNo));

//        getSupportActionBar().setTitle(title);



    }
}