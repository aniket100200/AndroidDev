package com.example.scpractice.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.example.scpractice.R;

public class QuizFragment extends Fragment {

    public QuizFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        
        WebView webView = view.findViewById(R.id.quizWebView);

        WebSettings webSettings = webView.getSettings();
        
        // 1. Enable JavaScript (Crucial for your dashboard.js and Tailwind to run)
        webSettings.setJavaScriptEnabled(true);

        // 2. Allow Fetching local JSON files
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        
        // 3. Enable Dom Storage (Useful for React/Vue or complex scripts)
        webSettings.setDomStorageEnabled(true);

        // 4. Ensure navigation stays within the app and doesn't open Chrome
        webView.setWebViewClient(new WebViewClient());

        // 5. Load your Web Dashboard
        webView.loadUrl("file:///android_asset/index.html");

        return view;
    }
}
