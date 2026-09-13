package com.example.scpractice;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Handler for the 3-second delay
    private final Handler handler = new Handler(Looper.getMainLooper());
    private SeekBar seekBarMax;
    private TextView tvRangeLabel, tvRandomNumber, tvResult;
    private AppCompatButton btnGenerate, btnSquare, btnCube;
    private int currentRandomNumber = 0;
    private int currentMaxRange = 50; // Default value matching the XML progress

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Map UI components
        seekBarMax = findViewById(R.id.seekBarMax);
        tvRangeLabel = findViewById(R.id.tvRangeLabel);
        tvRandomNumber = findViewById(R.id.tvRandomNumber);
        tvResult = findViewById(R.id.tvResult);
        btnGenerate = findViewById(R.id.btnGenerate);
        btnSquare = findViewById(R.id.btnSquare);
        btnCube = findViewById(R.id.btnCube);

        // Listen for the user scrolling the SeekBar
        seekBarMax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Prevent the slider from going all the way down to 0
                if (progress < 1) {
                    progress = 1;
                    seekBar.setProgress(1);
                }
                currentMaxRange = progress;
                // Update the text so the user knows what range they selected
                tvRangeLabel.setText("Current Range: 1 to " + currentMaxRange);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // Set Click Listeners
        btnGenerate.setOnClickListener(v -> generateRandomNumber());
        btnSquare.setOnClickListener(v -> showAnswer(true));
        btnCube.setOnClickListener(v -> showAnswer(false));
    }

    private void generateRandomNumber() {
        // Generate a random number between 1 and the slider's current value
        Random random = new Random();
        currentRandomNumber = random.nextInt(currentMaxRange) + 1;

        // Update UI
        tvRandomNumber.setText(String.valueOf(currentRandomNumber));
        tvResult.setText("");

        // Enable the game buttons
        btnSquare.setEnabled(true);
        btnCube.setEnabled(true);
    }

    private void showAnswer(boolean isSquare) {
        showAnswer(isSquare, 3000);
    }

    private void showAnswer(boolean isSquare, long timer) {
        // Disable buttons so the user can't click during the countdown
        btnSquare.setEnabled(false);
        btnCube.setEnabled(false);
        btnGenerate.setEnabled(false);

        long answer = isSquare ?
                (long) currentRandomNumber * currentRandomNumber :
                (long) currentRandomNumber * currentRandomNumber * currentRandomNumber;

        String label = isSquare ? "Square" : "Cube";

        // Create a timer for 3000ms (3 seconds) that ticks every 1000ms (1 second)
        new android.os.CountDownTimer(timer, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Convert milliseconds to seconds and round up so it displays 3, 2, 1
                int secondsLeft = (int) Math.ceil(millisUntilFinished / 1000.0);
                tvResult.setText("Revealing the answer in " + secondsLeft + "...");
            }

            @Override
            public void onFinish() {
                // This runs exactly when the timer hits 0
                tvResult.setText("The " + label + " is: " + answer);

                // Re-enable buttons for the next round
                btnSquare.setEnabled(true);
                btnCube.setEnabled(true);
                btnGenerate.setEnabled(true);
            }
        }.start(); // Don't forget to call .start()!
    }
}