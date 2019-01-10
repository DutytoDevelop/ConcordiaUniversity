package com.example.nhickam.concordianavigation;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

/**
 * Created by nhickam on 11/10/2017.
 */


public class ILFDining extends Fragment {

    WebView browser;
    ProgressBar progressBar;

    public ILFDining(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.important_links_dining,container,false);
    }



    //On view created, display dining menu webview
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //This creates a html grabber for the dining hall website. Grabs html -> gets url to menu -> set for webview url
        class RetroFitParser extends AsyncTask<Void,Void,String> {

            String url;//Url for menu
            String html;//Raw html of given page

            public RetroFitParser() {
            }

            @Override
            protected String doInBackground(Void... voids) {
                String raw="";
                Document doc;
                try {
                    doc = Jsoup.connect("https://menus.sodexomyway.com/BiteMenu/Menu?menuId=15254&locationId=11268001&whereami=http://ctxdining.sodexomyway.com/dining-near-me/dining-hall").get();
                    raw = doc.body().toString();
                    html = raw;
                    int startpos = 0;
                    int endpos = 0;
                    if (html.indexOf("11268001 CONCORDIA UNIVERSITY TEXAS  01")!=-1){
                        url = "https://menus.sodexomyway.com/BiteMenu/Menu?menuId=15254&locationId=11268001&whereami=http://ctxdining.sodexomyway.com/dining-near-me/dining-hall";
                    }else{
                        url = "https://ctxdining.sodexomyway.com/dining-near-me/dining-hall";
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return raw;
            }

            @Override
            protected void onPostExecute(String result) {
                //if you had a ui element, you could display the title
                browser.getSettings().setAppCacheMaxSize( 5 * 1024 * 1024 ); // 5MB
                browser.getSettings().setAppCachePath( getActivity().getApplicationContext().getCacheDir().getAbsolutePath() );
                browser.getSettings().setAllowFileAccess( true );
                browser.getSettings().setAppCacheEnabled( true );
                browser.getSettings().setLoadWithOverviewMode(true);
                browser.getSettings().setUseWideViewPort(true);
                browser.getSettings().setBuiltInZoomControls(true);
                browser.getSettings().setDisplayZoomControls(false);
                browser.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

                browser.setWebViewClient(new WebViewClient());
                try {browser.loadUrl(url);}
                catch(Exception e){
                    browser.setVisibility(View.GONE);
                }
            }
        }


        RetroFitParser ff = new RetroFitParser();
        ff.execute();
        browser = view.findViewById(R.id.webViewDining);
        progressBar = view.findViewById(R.id.progressBarDining);
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
        //browser.getSettings().setLoadWithOverviewMode(true);



/*
        Object restClient;
        Call<Void> restCall = restClient.getUserService().readUserData(object);
        restCall.enqueue(new Callback<Void>(){
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccess()){
                    Log.d(TAG, "  response from server" + response.body());
                }else{
                    Log.d(TAG, " Some error occurred ");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d(TAG, " Web service failure, exception ");
            }
        });*/
    }

}