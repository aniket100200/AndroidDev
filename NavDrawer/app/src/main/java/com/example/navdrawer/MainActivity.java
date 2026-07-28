package com.example.navdrawer;

import android.os.Bundle;
import android.view.Gravity;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawer;
    NavigationView navigationView;

    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawer=findViewById(R.id.drawerLayout);
        navigationView=findViewById(R.id.navView);
        toolbar=findViewById(R.id.toolbar);

        //step 1
        //set toolbar
        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_drawer,R.string.close_drawer);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener((item)->{
            int id=item.getItemId();
            if(id==R.id.optNotes){
                openFragment(new NotesFragment());
            }else if(id==R.id.optHome){
                openFragment(new HomeFragment());
            }else{
                openFragment(new SettingsFragment());
            }

            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        openFragment(new HomeFragment(),false);

        OnBackPressedCallback drawerCallback= new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                drawer.closeDrawer(GravityCompat.START);
            }
        };



    }

    private void openFragment(Fragment fragment){
        openFragment(fragment,true);
    }

    public void openFragment(Fragment fragment,boolean isAdded){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();

        if(isAdded)
            ft.replace(R.id.container,fragment);
        else
            ft.add(R.id.container,fragment);

        ft.commit();

    }




}