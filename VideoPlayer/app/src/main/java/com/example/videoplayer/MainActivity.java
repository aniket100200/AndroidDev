package com.example.videoplayer;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.net.URI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        VideoView videoView = findViewById(R.id.videoView);

        String originalLink = "https://drive.google.com/file/d/1qWgYFynQWpIQ0m5i-zto_bP6t2E3-3zD/view?usp=sharing";
        Uri videoUri = Uri.parse(getDirectDriveLink(originalLink));

        // 1. Set up the MediaController FIRST
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // 2. Set the video source
        videoView.setVideoURI(videoUri);

        // 3. WAIT for the video to buffer over the internet, THEN start playing
        videoView.setOnPreparedListener(mp -> {
            videoView.start();
        });

        // Optional but recommended: handle errors so it doesn't just freeze
        videoView.setOnErrorListener((mp, what, extra) -> {
            // Log the error or show a Toast here
            return true;
        });
    }


    private String getDirectDriveLink(String originalLink) {
        if (originalLink != null && originalLink.contains("drive.google.com/file/d/")) {
            try {
                // Extract the ID located between "/d/" and "/view"
                int startIndex = originalLink.indexOf("/d/") + 3;
                int endIndex = originalLink.indexOf("/view");
                String fileId = originalLink.substring(startIndex, endIndex);

                // Return the direct download format
                return "https://docs.google.com/uc?export=download&id=" + fileId;
            } catch (Exception e) {
                return originalLink;
            }
        }
        return originalLink;
    }
}