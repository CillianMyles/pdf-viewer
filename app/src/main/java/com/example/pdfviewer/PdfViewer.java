package com.example.pdfviewer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class PdfViewer extends AppCompatActivity {

    private static final String TAG = PdfViewer.class.getSimpleName();

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

        final String viewerUrl = "file:///android_asset/pdf-js-1.8/web/viewer.html";
        final String localPdfUrl = "file:///android_asset/SamplePDFFile_5mb.pdf";
        final String onlinePdfUrl = "https://cdn.mozilla.net/pdfjs/helloworld.pdf";
        final String toBeEncoded = onlinePdfUrl;

        String encodedUrl = "";
        try {
            encodedUrl = URLEncoder.encode(toBeEncoded, "UTF-8");
        } catch (UnsupportedEncodingException pE) {
            Log.e(TAG, "Cannot encode URL: " + toBeEncoded, pE);
        }

        final String toBeLoaded = viewerUrl + "?file=" + encodedUrl;
        Log.e(TAG, "Loading URL: " + toBeLoaded);
        mWebView.loadUrl(toBeLoaded);
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
