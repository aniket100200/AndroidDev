package com.example.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar pgBar;

    // New variables for fullscreen handling
    private FrameLayout fullScreenContainer;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        pgBar = findViewById(R.id.pgBar);
        fullScreenContainer = findViewById(R.id.fullScreenContainer); // Initialize it

        webView.getSettings().setJavaScriptEnabled(true);

        // This is required for some HTML5 videos to play properly
        webView.getSettings().setDomStorageEnabled(true);

        // 1. Set the WebChromeClient for Fullscreen Video
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                // If a view already exists, ignore this request
                if (customView != null) {
                    callback.onCustomViewHidden();
                    return;
                }

                // Save the view and callback
                customView = view;
                customViewCallback = callback;

                // Hide the WebView and show the full-screen container
                webView.setVisibility(View.GONE);
                fullScreenContainer.setVisibility(View.VISIBLE);
                fullScreenContainer.addView(customView);
            }

            @Override
            public void onHideCustomView() {
                if (customView == null) {
                    return;
                }

                // Remove the custom view and hide the container
                fullScreenContainer.removeView(customView);
                fullScreenContainer.setVisibility(View.GONE);
                customView = null;

                if (customViewCallback != null) {
                    customViewCallback.onCustomViewHidden();
                }

                // Show the WebView again
                webView.setVisibility(View.VISIBLE);
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pgBar.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pgBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl("https://www.google.com");

        // 2. Update Back Button Logic
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Priority 1: If fullscreen video is playing, close it using the callback directly
                if (customView != null && customViewCallback != null) {
                    customViewCallback.onCustomViewHidden();
                }
                // Priority 2: If we can go back in browser history, do that
                else if (webView.canGoBack()) {
                    webView.goBack();
                }
                // Priority 3: Close the app
                else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }
}