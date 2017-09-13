package com.example.pdfviewer;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class PdfViewer extends AppCompatActivity {

    private static final String TAG = PdfViewer.class.getSimpleName();

    private static final String JS_ERROR_MISSING_PDF = "Missing PDF file";
    private static final String USER_ERROR_INVALID_URL = "Invalid URL";

    private WebView mWebView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setBackgroundColor(ContextCompat.getColor(this, R.color.webview_bg));

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);

        // Default value is true for API ICE_CREAM_SANDWICH_MR1 and below,
        // and false for API level JELLY_BEAN and above.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        }

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                String printable = String.format(Locale.ENGLISH,
                        "\"%s\", source: %s (%d)",
                        consoleMessage.message(),
                        consoleMessage.sourceId(),
                        consoleMessage.lineNumber()
                );
                switch (consoleMessage.messageLevel()) {
                    case TIP:
                        Log.v(TAG, printable);
                        break;
                    case DEBUG:
                        Log.d(TAG, printable);
                        break;
                    case LOG:
                        Log.i(TAG, printable);
                        break;
                    case WARNING:
                        Log.w(TAG, printable);
                        break;
                    case ERROR:
                        Log.e(TAG, printable);
                        if (consoleMessage.message().contains(JS_ERROR_MISSING_PDF)) {
                            Toast.makeText(PdfViewer.this, USER_ERROR_INVALID_URL, Toast.LENGTH_SHORT).show();
                            mWebView.loadUrl("about:blank");
                        }
                        break;
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });

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
