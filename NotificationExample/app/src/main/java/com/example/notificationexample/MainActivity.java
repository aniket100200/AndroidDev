package com.example.notificationexample;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationCompat; // EXCLUSIVELY USING COMPAT
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "MESSAGE";
    private static final int NOTIFICATION_ID = 1;
    private static final int PI_REQUEST_CODE = 100;

    private AppCompatButton clickMe;
    private NotificationManager manager;

    // 1. Declare the permission launcher
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission granted, safe to show notifications
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 2. Ask for runtime permission (Android 13+)
        askNotificationPermission();

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();

        // 3. Setup your icons (Load the Bitmap once to save memory)
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.img, null);
        Bitmap imageBitmap = ((BitmapDrawable) drawable).getBitmap();

        // 4. Create the Pending Intent for clicks
        Intent iNotify = new Intent(this, MainActivity.class);
        iNotify.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(
                this,
                PI_REQUEST_CODE,
                iNotify,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // 5. Setup Big Picture Style using NotificationCompat
        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle()
                .bigPicture(imageBitmap)
                .bigLargeIcon((Bitmap) null) // Passing null hides the small thumbnail when expanded (Standard Android behavior)
                .setBigContentTitle("Image Sent by Ramandeep");

        //5.2 Inbox style
        int count=1;
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle()
                .addLine("A")
                .addLine("B")
                .addLine("C")
                .addLine("D")
                .addLine(String.valueOf(++count))
                .addLine(String.valueOf(++count))
                .addLine(String.valueOf(++count))
                .addLine(String.valueOf(++count))
                .addLine(String.valueOf(++count))
                .addLine(String.valueOf(++count))
                .addLine(String.valueOf(++count))
                .addLine(String.valueOf(++count))
                .addLine(String.valueOf(++count))
                .addLine(String.valueOf(++count))
                .addLine(String.valueOf(++count))
                .addLine(String.valueOf(++count))
                .setBigContentTitle("Full Message")
                .setSummaryText("Message From Aniket");

        // 6. Build the Notification using NotificationCompat
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setLargeIcon(imageBitmap)
                .setSmallIcon(R.drawable.icon_database)
                .setContentTitle("New Message")
                .setContentText("Hello Dear User, How are you doing today!!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false)
                .setStyle(inboxStyle)
                .setContentIntent(pi);



        // 7. Fire the Notification on Button Click
        clickMe = findViewById(R.id.btnClickMe);
        clickMe.setOnClickListener(v -> {
            // Always check permission right before firing on Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    manager.notify(NOTIFICATION_ID, builder.build());
                } else {
                    askNotificationPermission(); // Prompt again if they denied it previously
                }
            } else {
                manager.notify(NOTIFICATION_ID, builder.build()); // Fire normally on Android 12 and below
            }
        });
    }

    // --- HELPER METHODS ---

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "New Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            manager.createNotificationChannel(channel);
        }
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
}