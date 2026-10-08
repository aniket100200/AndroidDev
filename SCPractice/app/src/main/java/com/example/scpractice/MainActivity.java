package com.example.scpractice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.scpractice.Utils.TextToSpeechListener;
import com.example.scpractice.enums.TypeOfWebView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton aboutUs;

    TextToSpeechListener textToSpeechListener;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        Toast.makeText(this, "Microphone access granted. You can answer using voice!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Microphone access denied. You can type your answers instead.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        checkAndRequestPermissions();

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

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // Check if we should show a rationale dialog
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {
                new AlertDialog.Builder(this)
                        .setTitle("Microphone Permission")
                        .setMessage("This app can listen to your voice answers for tables and quizzes. Would you like to grant microphone permission?")
                        .setPositiveButton("Grant", (dialog, which) -> {
                            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
                        })
                        .setNegativeButton("Not Now", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
            }
        }
    }

    // Public speak function that accepts any text string
    public void speakMessage(String text) {
        speakMessage(text, null);
    }

    public void speakMessage(String text, Runnable onDone) {
        if (textToSpeechListener != null) {
            textToSpeechListener.speak(text, onDone);
        } else if (onDone != null) {
            onDone.run();
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
