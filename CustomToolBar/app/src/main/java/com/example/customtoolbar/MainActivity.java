package com.example.customtoolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);

        // step1:
        setSupportActionBar(toolbar);

        //step2:Customization

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Aniket");
        }
        toolbar.setSubtitle("Sub Title");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId=item.getItemId();

        if(itemId==R.id.opt_new){
            Toast.makeText(this,"Create a new File",Toast.LENGTH_SHORT).show();
        }else if(itemId==R.id.opt_open){
            Toast.makeText(this,"Open File",Toast.LENGTH_SHORT).show();
        }else if(itemId==R.id.opt_save){
            Toast.makeText(this,"Saved File",Toast.LENGTH_SHORT).show();
        }else if (itemId==R.id.opt_contact_us){
            Toast.makeText(this,"clicked Contact Us",Toast.LENGTH_SHORT).show();
        }else if(itemId==android.R.id.home){
            Toast.makeText(this,"clicked Back Press",Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}