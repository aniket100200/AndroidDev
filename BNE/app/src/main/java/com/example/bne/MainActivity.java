package com.example.bne;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView btView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        btView = findViewById(R.id.btView);

        btView.setOnItemSelectedListener((item)->{
            int id=item.getItemId();

            if(id==R.id.navHome){
               openFragment(new HomeFragment(),true);

            } else if (id == R.id.navSearch) {

               openFragment(new SerachFragment(),true);

            }else if(id==R.id.navUtilities){
                openFragment(new UtilitiesFragment(), true);
            }else if(id==R.id.navContactUs){

                openFragment(new ContactUsFragment(), true);
            }else{
                //for profile
                openFragment(new ProfileFragment(), false);

            }
            return true;
        });

        btView.setSelectedItemId(R.id.navProfile);

    }

    private void openFragment(Fragment fragment,boolean isFragmentLoaded){
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(!isFragmentLoaded)
            ft.add(R.id.container, fragment);
        else ft.replace(R.id.container,fragment);
        ft.commit();
    }
}