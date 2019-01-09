package com.example.nhickam.concordianavigation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.w3c.dom.Document;

/**
 * Created by nhickam on 11/10/2017.
 */

public class ILFTutors extends Fragment {

    WebView browser;
    ProgressBar progressBar;
    String url = "http://www.concordia.edu/resources/success-center/peer-tutoring/";
    Document doc = null;
    String raw;

    public ILFTutors(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.important_links_tutors, container, false);
    }

    //When view is created, set up webview's browser to open up to Tutor page for Concordia
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        browser = view.findViewById(R.id.webViewTutor);
        progressBar = view.findViewById(R.id.progressBarTutors);
        browser.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setProgress(progress);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);

                } else {
                    progressBar.setVisibility(View.VISIBLE);

                }
            }
        });
        WebSettings webSettings = browser.getSettings();
        webSettings.setJavaScriptEnabled(true);

        browser.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    WebView webView = (WebView) v;

                    switch(keyCode)
                    {
                        case KeyEvent.KEYCODE_BACK:
                            if(webView.canGoBack())
                            {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });
        browser.getSettings().setAppCacheMaxSize( 5 * 1024 * 1024 ); // 5MB
        browser.getSettings().setAppCachePath( getActivity().getApplicationContext().getCacheDir().getAbsolutePath() );
        browser.getSettings().setAllowFileAccess( true );
        browser.getSettings().setAppCacheEnabled( true );
        browser.getSettings().setLoadWithOverviewMode(true);
        browser.getSettings().setUseWideViewPort(true);
        browser.getSettings().setBuiltInZoomControls(true);
        browser.getSettings().setDisplayZoomControls(false);
        browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        //Enables users to download files from the WebView
        browser.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        browser.setWebViewClient(new WebViewClient());
        browser.loadUrl("http://www.concordia.edu/resources/success-center/peer-tutoring/");
    }
/* DONT DELETE******************************************
    class RetrieveHTMLStream extends AsyncTask<String,Void,InputStream> {

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            logAll("async", doc.toString());
        }

        void logAll(String TAG, String longString) {

            int splitSize = 300;

            if (longString.length() > splitSize) {
                int index = 0;
                while (index < longString.length() - splitSize) {
                    Log.e(TAG, longString.substring(index, index + splitSize));
                    index += splitSize;
                }
                Log.e(TAG, longString.substring(index, longString.length()));
            } else {
                Log.e(TAG, longString.toString());
            }
        }

        @Override
        protected InputStream doInBackground(String... params) {
            try {
                doc = (Document) Jsoup.connect(url).get();
            } catch (IOException e) {
                //...
                e.printStackTrace();
            }
            return null;
        }
    }
*/
}