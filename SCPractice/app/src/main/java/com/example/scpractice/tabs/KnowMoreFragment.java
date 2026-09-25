package com.example.scpractice.tabs;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.scpractice.R;

public class KnowMoreFragment extends Fragment {


    public KnowMoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_know_more, container, false);

        WebView webView = view.findViewById(R.id.myWebView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Add these two lines to allow JavaScript fetch() to read local JSON files
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        String url = "https://aniketappstore.netlify.app/";
        // 2. Enable Local Storage (Crucial for React/Vue/Netlify apps)
        webSettings.setDomStorageEnabled(true);


        webView.loadUrl(url);


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
                DownloadManager dm =
                        (DownloadManager) requireContext().getSystemService(Context.DOWNLOAD_SERVICE);
                dm.enqueue(request);

                Toast.makeText(getContext(), "Downloading File", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}