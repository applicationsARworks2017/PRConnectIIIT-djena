package com.iiit.amaresh.demotrack.Activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.iiit.amaresh.demotrack.R;

/**
 * Created by mobileapplication on 11/26/17.
 */

public class FileView extends AppCompatActivity {
    String path;
    ProgressBar loader_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_view);
        Bundle extras = getIntent().getExtras();
        final ProgressBar loader_view=(ProgressBar)findViewById(R.id.loader_view);

        if (extras != null) {
            path = extras.getString("PATH");
            // and get whatever type user account id is
        }

        WebView webView = (WebView)findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loader_view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // pDialog.dismiss();
                loader_view.setVisibility(View.GONE);
            }
        });
        // String pdf = "http://www.adobe.com/devnet/acrobat/pdfs/pdf_open_parameters.pdf";
        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + path);
    }
}
