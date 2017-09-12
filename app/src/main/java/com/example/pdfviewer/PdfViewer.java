package com.example.pdfviewer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class PdfViewer extends AppCompatActivity {

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);

        // Default value is true for API ICE_CREAM_SANDWICH_MR1 and below,
        // and false for API level JELLY_BEAN and above.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl("file:///android_asset/pdfviewer/index.html");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.loadUrl("javascript:window.location.reload(true)");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.clearCache(true);
    }
}
