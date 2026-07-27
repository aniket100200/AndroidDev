package com.example.listexample;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Spinner spinner;

    AutoCompleteTextView acTxtView;
    List<String> students=new LinkedList<>();
    List<String>idProofs=new ArrayList<>();

    List<String> languages=new Vector<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        listView=findViewById(R.id.listStudent);
        listView.setBackgroundColor(getColor(R.color.white));


        for(int i=0;i<1000;i++){
            students.add("Student "+(i+1));
        }

        ArrayAdapter<String>adapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,students);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView,view,position,x)->{
                if(position==9){
                    Toast.makeText(MainActivity.this,"You Win",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"Try Again",Toast.LENGTH_SHORT).show();
                }
        });


        /**
         * for Spinner
         */

        spinner=findViewById(R.id.spinner_id_proof);

        idProofs.add("Aadhar Card");
        idProofs.add("Driving License");
        idProofs.add("PAN Card");
        idProofs.add("Voter Id Card");
        idProofs.add("Ration Card");
        idProofs.add("Xth Score card");
        idProofs.add("XIIth Score card");

        adapter=new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,idProofs);

        spinner.setAdapter(adapter);

        /**
         * For AutoCompleteTextView
         */

        acTxtView=findViewById(R.id.acTxtView);

       languages.add("C");
       languages.add("C++");
       languages.add("Java");
       languages.add("PHP");
       languages.add("Python");
       languages.add("Objective C");
       languages.add("C#");
       languages.add("CScript");
       languages.add("Javascript");

       ArrayAdapter<String> acTvAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,languages);

       acTxtView.setAdapter(acTvAdapter);
       acTxtView.setThreshold(2);






    }
}