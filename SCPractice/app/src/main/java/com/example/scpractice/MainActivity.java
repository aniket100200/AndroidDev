package com.example.scpractice;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.scpractice.enums.TypeOfWebView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton aboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager = findViewById(R.id.viewPager);

        viewPager.setAdapter(new ViewPagerMessengerAdapter(this));

        TabLayout tabLayout = findViewById(R.id.tab);

        new TabLayoutMediator(
                tabLayout,
                viewPager,
                (tab, position) -> {

                    switch (position) {
                        case 0:
                            tab.setText("Square Cubes");
                            break;

                        case 1:
                            tab.setText("Tables");
                            break;

                        case 2:
                            tab.setText("Quiz");
                            break;
                        default:
                            tab.setText("About Us");
                    }
                }
        ).attach();

        aboutUs = findViewById(R.id.btnAboutUs);

        aboutUs.setOnClickListener((v) -> {
            Intent iNext = new Intent(this, WebViewActivity.class);

            iNext.putExtra("type", TypeOfWebView.ONLINE.toString());
            startActivity(iNext);
        });


    }


}