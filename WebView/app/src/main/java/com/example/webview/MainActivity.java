package com.example.webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private ProgressBar pgBar;

    private FrameLayout fullScreenContainer;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private WebChromeClient webChromeClient; // Store this to call it later

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        pgBar = findViewById(R.id.pgBar);
        fullScreenContainer = findViewById(R.id.fullScreenContainer);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        // 1. Initialize and set the WebChromeClient
        webChromeClient = new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (customView != null) {
                    callback.onCustomViewHidden();
                    return;
                }

                customView = view;
                customViewCallback = callback;

                webView.setVisibility(View.GONE);
                fullScreenContainer.setVisibility(View.VISIBLE);
                fullScreenContainer.addView(customView);

                // ADDED: Hide Status and Navigation bars
                WindowInsetsControllerCompat insetsController =
                        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
                insetsController.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                insetsController.hide(WindowInsetsCompat.Type.systemBars());
            }

            @Override
            public void onHideCustomView() {
                if (customView == null) {
                    return;
                }

                fullScreenContainer.removeView(customView);
                fullScreenContainer.setVisibility(View.GONE);
                customView = null;

                if (customViewCallback != null) {
                    customViewCallback.onCustomViewHidden();
                    customViewCallback = null;
                }

                webView.setVisibility(View.VISIBLE);

                // ADDED: Show Status and Navigation bars again
                WindowInsetsControllerCompat insetsController =
                        WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
                insetsController.show(WindowInsetsCompat.Type.systemBars());
            }
        };

        webView.setWebChromeClient(webChromeClient);

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



        // 2. FIXED: Update Back Button Logic
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Priority 1: Close fullscreen video cleanly
                if (customView != null) {
                    webChromeClient.onHideCustomView(); // Call the method directly so system bars come back!
                }
                // Priority 2: Go back in browser
                else if (webView.canGoBack()) {
                    webView.goBack();
                }
                // Priority 3: Close app
                else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(webView, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Apply the system bar heights as margins to the WebView
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            params.topMargin = insets.top;
            params.bottomMargin = insets.bottom;
            params.leftMargin = insets.left;
            params.rightMargin = insets.right;
            v.setLayoutParams(params);

            // Return CONSUMED if you don't want the insets to pass to child views
            return WindowInsetsCompat.CONSUMED;
        });



        webView.setWebViewClient(new WebViewClient() {
            // ... your other methods ...

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pgBar.setVisibility(View.GONE);

                // Inject JavaScript to override the Page Visibility API
                String js = "javascript:(function() { " +
                        "Object.defineProperty(document, 'hidden', { value: false, writable: false }); " +
                        "Object.defineProperty(document, 'visibilityState', { value: 'visible', writable: false }); " +
                        "window.addEventListener('visibilitychange', function(e) { e.stopImmediatePropagation(); }, true); " +
                        "})()";

                view.evaluateJavascript(js, null);

                String adBlockJs = "javascript:(function() { " +
                        "setInterval(function() { " +
                        "var skipButton = document.querySelector('.ytp-ad-skip-button, .ytp-ad-skip-button-modern'); " +
                        "if (skipButton) { skipButton.click(); } " +

                        "var adShowing = document.querySelector('.ad-showing'); " +
                        "if (adShowing) { " +
                        "var video = document.querySelector('video'); " +
                        "if (video) { video.currentTime = video.duration; } " +
                        "} " +
                        "}, 500); " + // Checks every 500 milliseconds
                        "})()";

                view.evaluateJavascript(adBlockJs, null);
            }
        });


        webView.loadUrl("https://www.youtube.com");


    }

    @Override
    protected void onPause() {
        super.onPause();
        // Force the CookieManager to save the login session to permanent storage
        CookieManager.getInstance().flush();
    }
}