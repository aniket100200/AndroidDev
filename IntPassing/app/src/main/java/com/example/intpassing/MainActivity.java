package com.example.intpassing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    AppCompatButton btnDail,btnMessage,btnEmail,btnShare,btnFragA,btnFragB,btnFragC;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        initialiseButtons();
//
//        btnDail.setOnClickListener((v)->{
//
//            Intent iDial=new Intent(Intent.ACTION_DIAL);
//            iDial.setData(Uri.parse("tel: +919527270839"));
//
//            startActivity(iDial);
//
//        });
//
//        btnMessage.setOnClickListener((v)->{
//
//            Intent iMsg=new Intent(Intent.ACTION_SENDTO);
//            iMsg.setData(Uri.parse("smsto:"+Uri.encode("+919322462271")));
//            iMsg.putExtra("sms_body","Hey Wassup!, Have you had your dinner");
//
//            startActivity(iMsg);
//
//
//        });
//
//        btnEmail.setOnClickListener(v->{
//            Intent iEmail=new Intent(Intent.ACTION_SEND);
//            iEmail.setType("message/rfc822");
//            iEmail.putExtra(Intent.EXTRA_EMAIL,new String[]{"vedanjagnade@gmail.com","ojaskhangar@gmail.com","khangaraniket365@gmail.com"});
//            iEmail.putExtra(Intent.EXTRA_SUBJECT,"Queries");
//            iEmail.putExtra(Intent.EXTRA_TEXT,"Hello User wassup!!");
//            startActivity(Intent.createChooser(iEmail,"Email Via"));
//        });
//
//        btnShare.setOnClickListener(v->{
//
//            Intent iShare=new Intent(Intent.ACTION_SEND);
//            iShare.setType("text/plain");
//            iShare.putExtra(Intent.EXTRA_TEXT,"Download this Amazing App, https://play.google.com/store/apps/details?id=com.pubg.imobile");
//            startActivity(Intent.createChooser(iShare,"Share Via"));
//        });
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnFragA=findViewById(R.id.btnFragA);
        btnFragB=findViewById(R.id.btnFragB);
        btnFragC=findViewById(R.id.btnFragC);

        loadFragment(new BFragment(),false);



        btnFragA.setOnClickListener((v)->{
           loadFragment(new AFragment(),true);
        });

        btnFragB.setOnClickListener(v->{

           loadFragment(new BFragment(),true);

        });


        btnFragC.setOnClickListener(v->{

            loadFragment(new CFragment(),true);

        });

    }

    public void loadFragment(Fragment fragment,boolean flag){
        FragmentManager fm= getSupportFragmentManager();
        FragmentTransaction ft= fm.beginTransaction();
        if(flag)
            ft.replace(R.id.container,fragment);
        else
             ft.add(R.id.container,fragment);

        ft.addToBackStack(null); // <-- Add this line
        ft.commit();
    }

//    private void initialiseButtons(){
//        btnDail=findViewById(R.id.btnDial);
//        btnMessage=findViewById(R.id.btnMsg);
//        btnEmail=findViewById(R.id.btnEmail);
//        btnShare=findViewById(R.id.btnShare);
//    }



}