package com.example.scpractice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.scpractice.Utils.TextToSpeechListener;
import com.example.scpractice.enums.TypeOfWebView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton aboutUs;

    TextToSpeechListener textToSpeechListener;

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

        textToSpeechListener = new TextToSpeechListener(this, TextToSpeechListener.MARATHI, new TextToSpeechListener.OnInitCallback() {
            @Override
            public void onSuccess() {
                aboutUs.setEnabled(true);
            }

            @Override
            public void onError(String message) {
                Log.e("MainActivity", "TTS Error: " + message);
            }
        });

        aboutUs.setOnClickListener((v) -> {
            Intent iNext = new Intent(this, WebViewActivity.class);

            iNext.putExtra("type", TypeOfWebView.ONLINE.toString());
            startActivity(iNext);
        });


    }

    // Public speak function that accepts any text string
    public void speakMessage(String text) {
        speakMessage(text, null);
    }

    public void speakMessage(String text, Runnable onDone) {
        if (textToSpeechListener != null) {
            textToSpeechListener.speak(text, onDone);
        } else if (onDone != null) {
//            onDone.run();
        }
    }

    // Important: Release resources when the app closes
    @Override
    protected void onDestroy() {
        if (textToSpeechListener != null) {
            textToSpeechListener.shutdown();
        }
        super.onDestroy();
    }


}
