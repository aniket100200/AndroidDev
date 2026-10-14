package com.example.activerevision;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.activerevision.databinding.ActivityMainBinding;
import com.example.activerevision.notification.NotificationHelper;
import com.example.activerevision.scheduler.ReminderWorkManager;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Ensure periodic background reminder worker is scheduled
        ReminderWorkManager.schedulePeriodicReminder(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
        }

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null && intent.hasExtra(NotificationHelper.EXTRA_TASK_ID) && navController != null) {
            long taskId = intent.getLongExtra(NotificationHelper.EXTRA_TASK_ID, -1);
            if (taskId > 0) {
                Bundle bundle = new Bundle();
                bundle.putLong("taskId", taskId);
                navController.navigate(R.id.activeRecallFragment, bundle);
            }
        }
    }
}
