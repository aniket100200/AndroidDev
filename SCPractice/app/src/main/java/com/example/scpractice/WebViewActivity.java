package com.example.scpractice;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.scpractice.enums.TypeOfWebView;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;
    private FrameLayout fullscreenContainer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();
        String pageId = intent.getStringExtra("pageId");

        webView = findViewById(R.id.myWebView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Add these two lines to allow JavaScript fetch() to read local JSON files
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        String type = intent.getStringExtra("type");
        String url = "file:///android_asset/index.html";
        // 2. Enable Local Storage (Crucial for React/Vue/Netlify apps)
        webSettings.setDomStorageEnabled(true);

        if (TypeOfWebView.ONLINE.toString().equals(type)) {
            url = "https://mpscquiz.netlify.app/";
        }

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (pageId != null) {
                    String jsCommand = "javascript:loadQuizData('" + pageId + "')";
                    webView.evaluateJavascript(jsCommand, null);
                }
            }
        });

        // Add WebChromeClient to handle HTML5 video fullscreen playback
        WebChromeClient customWebChromeClient = new WebChromeClient() {
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                if (customView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                customView = view;
                customViewCallback = callback;

                // Hide the webview
                webView.setVisibility(View.GONE);

                // Add the custom view (fullscreen video) to the screen
                FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
                fullscreenContainer = new FrameLayout(WebViewActivity.this);
                fullscreenContainer.setBackgroundColor(getResources().getColor(android.R.color.black, null));
                fullscreenContainer.addView(customView, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                decorView.addView(fullscreenContainer);

                // Hide system bars for true full screen
                WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), decorView);
                controller.hide(WindowInsetsCompat.Type.systemBars());
                controller.setSystemBarsBehavior(WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }

            @Override
            public void onHideCustomView() {
                if (customView == null) return;

                FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
                decorView.removeView(fullscreenContainer);
                fullscreenContainer = null;
                customView = null;
                customViewCallback.onCustomViewHidden();

                webView.setVisibility(View.VISIBLE);

                // Restore system bars
                WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(getWindow(), decorView);
                controller.show(WindowInsetsCompat.Type.systemBars());
            }
        };
        webView.setWebChromeClient(customWebChromeClient);

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setMimeType(mimeType);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file...");
                String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
                request.setTitle(fileName);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);

                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
            }
        });

        // Intercept back button presses
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // If a video is in fullscreen, exit fullscreen first
                if (customView != null) {
                    customWebChromeClient.onHideCustomView();
                } 
                // If the webview can go back to a previous page, go back
                else if (webView.canGoBack()) {
                    webView.goBack();
                } 
                // Otherwise, exit the activity as normal
                else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }
}
