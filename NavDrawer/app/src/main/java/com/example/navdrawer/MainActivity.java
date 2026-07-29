package com.example.navdrawer;

import android.os.Bundle;
import android.util.Log;
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

    String ROOT_FRAGMENT_TAG;



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

        ROOT_FRAGMENT_TAG="MY_Fragments";


        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_drawer,R.string.close_drawer);

        drawer.addDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener((item)->{
            int id=item.getItemId();
            if(id==R.id.optNotes){
                openFragment(new NotesFragment(),0);
            }else if(id==R.id.optHome){
                openFragment(HomeFragment.getInstance("Home Screen","Hello Guys Welcome!!\n \t How are you guys Doing."),1);
            }else{
                openFragment(new SettingsFragment(),1);
            }

            drawer.closeDrawer(GravityCompat.START);
            return true;
        });

        openFragment(new HomeFragment(),false,1);

        OnBackPressedCallback drawerCallback= new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                drawer.closeDrawer(GravityCompat.START);
            }
        };



    }

    private void openFragment(Fragment fragment,int id){
        openFragment(fragment,true,id);
    }

    public void openFragment(Fragment fragment,boolean isAdded,int id){
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();

        Bundle bundle=new Bundle();

        switch (id){

            case 0:{
                bundle.putString("name","Today's Schedule");
                bundle.putString("value","1.Wake up 6:30AM\n 2.drink 400ml water.\n 3.Breakfast: oats honey 2 bananas.");
                fragment.setArguments(bundle);
                break;
            }

            case 2:{
                bundle.putString("name","Settings");
                bundle.putString("aboutUs","About Us");
                bundle.putString("wifi","Wifi Settings");
                bundle.putString("display","Display settings.");
                fragment.setArguments(bundle);
                break;
            }

            default:{
                break;
            }

        }



        if(isAdded) {
            ft.replace(R.id.container, fragment);
            ft.addToBackStack(null);
        }
        else {
            ft.add(R.id.container, fragment);
            fm.popBackStack(ROOT_FRAGMENT_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            ft.addToBackStack(ROOT_FRAGMENT_TAG);
        }

        ft.commit();

    }

    public void callFromFragment(){
        Log.d("inAct","from Fragment");
    }




}