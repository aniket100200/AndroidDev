package com.example.ludo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ludo.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    private ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Using ViewBinding to hook into our new layout safely
        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupClickListeners();
    }

    private void setupClickListeners() {
        // Top Bar
        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Settings Clicked", Toast.LENGTH_SHORT).show();
            // TODO: Open Settings Bottom Sheet Dialog
        });

        // Gameplay actions
        binding.btnRollDice.setOnClickListener(v -> handleDiceRoll());
        binding.ivDice.setOnClickListener(v -> handleDiceRoll());
    }

    private void handleDiceRoll() {
        // Placeholder for future animation and random logic implementation
        Toast.makeText(this, "Rolling Dice...", Toast.LENGTH_SHORT).show();

        // Logic to disable button to prevent double-clicking during animation
        binding.btnRollDice.setEnabled(false);

        // Simulating the end of an animation
        binding.btnRollDice.postDelayed(() -> {
            binding.btnRollDice.setEnabled(true);
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Prevent memory leaks
    }
}