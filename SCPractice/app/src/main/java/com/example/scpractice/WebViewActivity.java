package com.example.scpractice;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scpractice.enums.TypeOfWebView;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;

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
            url = "https://aniketappstore.netlify.app/";
        }

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                // Assuming pageId is a String. If it's an int, you can leave off the single quotes.

                String jsCommand = "javascript:loadQuizData('" + pageId + "')";

                webView.evaluateJavascript(jsCommand, null);

            }
        });


        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimeType,
                                        long contentLength) {

                // 1. Create a Download Request
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                // 2. Pass necessary headers and cookies (crucial for secure downloads)
                request.setMimeType(mimeType);
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                request.addRequestHeader("User-Agent", userAgent);

                // 3. Configure the notification display
                request.setDescription("Downloading file...");
                String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
                request.setTitle(fileName);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                // 4. Set the destination to the public Downloads folder
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                // 5. Hand the request to the system DownloadManager
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);

                Toast.makeText(getApplicationContext(), "Downloading File", Toast.LENGTH_LONG).show();
            }
        });


    }
}