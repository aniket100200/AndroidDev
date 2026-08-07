package com.example.audioplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.audioplayer.utils.SongData;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MaterialButton btnPrevious, btnPlay, btnNext;
    ProgressBar progressLoader; // Add this
    private final MediaPlayer mp = new MediaPlayer();
    List<SongData.Song> songList;
    int currentSong = 0;

    TextView songTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnPrevious = findViewById(R.id.btnPause);
        btnPlay = findViewById(R.id.btnPlay);
        btnNext = findViewById(R.id.btnStop);
        songTitle = findViewById(R.id.txtTitle);
        progressLoader = findViewById(R.id.progressLoader); // Initialize this

        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        songList = SongData.ALL_SONGS;

        // Load the initial song
        setSong();

        // 3. When audio finishes buffering: hide loader, show play icon
        mp.setOnPreparedListener(player -> {
            progressLoader.setVisibility(View.GONE); // Hide spinner
            btnPlay.setIconResource(R.drawable.play_icon); // Show play icon
            btnPlay.setEnabled(true);

            // Optional: If you want the song to automatically start playing when they hit Next/Prev
            // uncomment the next two lines:
            // mp.start();
            // btnPlay.setIconResource(R.drawable.pause_icon);
        });

        btnPlay.setOnClickListener(v -> {
            if (!mp.isPlaying()) {
                mp.start();
                btnPlay.setIconResource(R.drawable.pause_icon);
            } else {
                mp.pause();
                btnPlay.setIconResource(R.drawable.play_icon);
            }
        });

        btnPrevious.setOnClickListener(v -> {
            if (currentSong > 0) {
                this.currentSong--;
                setSong();
            }
        });

        btnNext.setOnClickListener(v -> {
            this.currentSong++;
            currentSong %= songList.size(); // Loops back to 0 if at the end
            setSong();
        });

        mp.setOnCompletionListener(player -> {
            player.seekTo(0);
            btnPlay.setIconResource(R.drawable.play_icon); // Reset icon when song ends
        });
    }

    public void setSong() {
        // --- SHOW LOADER STATE ---
        btnPlay.setEnabled(false); // Disable button clicks
        btnPlay.setIconResource(0); // Remove the play/pause icon entirely (0 clears it)
        progressLoader.setVisibility(View.VISIBLE); // Show the white spinner

        SongData.Song song = songList.get(currentSong);
        String onlineAudioPath = getDirectDriveLink(song.getLink());
        String songTitleStr = song.getTitle();

        this.songTitle.setText(songTitleStr);
        Uri onlineUri = Uri.parse(onlineAudioPath);

        try {
            mp.reset();
            mp.setDataSource(this, onlineUri);
            mp.prepareAsync();
        } catch (IOException t) {
            t.printStackTrace();
            // If it fails, hide loader and show an error state/icon here if desired
            progressLoader.setVisibility(View.GONE);
            btnPlay.setIconResource(R.drawable.play_icon);
        }
    }

    private String getDirectDriveLink(String originalLink) {
        if (originalLink != null && originalLink.contains("drive.google.com/file/d/")) {
            try {
                int startIndex = originalLink.indexOf("/d/") + 3;
                int endIndex = originalLink.indexOf("/view");
                String fileId = originalLink.substring(startIndex, endIndex);
                return "https://docs.google.com/uc?export=download&id=" + fileId;
            } catch (Exception e) {
                return originalLink;
            }
        }
        return originalLink;
    }
}