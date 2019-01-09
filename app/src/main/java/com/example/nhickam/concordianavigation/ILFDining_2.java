package com.example.nhickam.concordianavigation;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;
import com.takusemba.multisnaprecyclerview.OnSnapListener;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

/**
 * Created by nhickam on 11/10/2017.
 */


public class ILFDining_2 extends Fragment {

    LinearLayoutManager mLayoutManager;
    ProgressBar progressBar;
    String url;//Url for menu
    String html;//Raw html of given page

    String[] daysOfWeek = {
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday",
            "Sunday"
    };

    String[] calDay = {
            "", //0 - Mon.
            "", //1
            "", //2
            "", //3
            "", //4
            "", //5 - Sat.
            ""  //6
    };

    String[] lunchContent = {
            "Test Meal Monday\nTest Meal 2 Monday",
            "Test Meal Tues.\nTest Meal 2 Tues.",
            "Test Meal Wed.\nTest Meal 2 Day 1",
            "..",
            "..",
            "..\nTest Meal 2 Day 1",
            "Test Meal Day 1\nTest Meal 2 Day 1",
    };

    Map<String, String>[][] lunchContent2; //Map<"NutritionCode", "Food Item">[DayofWeek], [food item]

    String[][] lunchItemText = { // {Nutri. Code for food, food name}
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""},
            {"",""}
    };

    String[] dinnerContent = {
            "Test Meal Monday\nTest Meal 2 Monday",
            "Test Meal Tues.\nTest Meal 2 Tues.",
            "Test Meal Wed.\nTest Meal 2 Day 1",
            "..",
            "..",
            "..\nTest Meal 2 Day 1",
            "Test Meal Day 1\nTest Meal 2 Day 1",
    };

    public ILFDining_2(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.important_links_dining_2,container,false);
    }



    //On view created, display dining menu webview
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");

        Date dt = new Date();
        DateTime dtOrg = new DateTime(dt);
        DateTime dtPlusOne = dtOrg.plusDays(1);

        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;
        String monthStr = theMonth(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        for(int dayFlux=0;dayFlux<7;dayFlux++){
            //WHY IS THIS SO  HARD?! CAUSE IM TIRED
        }
*/

        Date date = new Date();
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        for(int i=0;i<7;i++){
            Log.wtf("cal",getFormattedDate(cal.getTime())+" <- Day of week [string::int] ->"+ dayOfWeek);
            cal.set(Calendar.DATE,(cal.getTime().getDate()+1));
        }

        //This creates a html grabber for the dining hall website. Grabs html -> gets menu items -> displays neatly
        class RetroFitParser extends AsyncTask<Void,Void,String> {

            public RetroFitParser() {
            }

            @Override
            protected String doInBackground(Void... voids) {
                String raw="";
                Document doc;

                try {
                    //doc = Jsoup.connect("https://ctxdining.sodexomyway.net/dining-choices/index.html").get();
                    BufferedReader r = new BufferedReader(new InputStreamReader(getActivity().getApplicationContext().getAssets().open("menu.html")));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line).append('\n');
                    }
                    String test = total.toString();
                    //raw = doc.body().toString();
                    raw = test;
                    html = raw;
                    String[] content;
                    String[] aData_Nutrition;
                    String nutrition;
                    String nutriCode; //
                    //Log.wtf(TAG, "PARSED! "+html);
                    content = raw.split("<!-- MONDAY -->|<!-- TUESDAY -->|<!-- WEDNESDAY -->|<!-- THURSDAY -->|<!-- FRIDAY -->|<!-- SATURDAY -->|<!-- SUNDAY -->"); //Content[x] (x=1) = Mond. (x=7) = Nutri. Facts
                    if(content[0]!=raw){//If split doesn't work (MENU DOESN'T EXIST) then display No available information.
                        nutrition = content[7].substring(content[7].indexOf("<!-- JAVASCRIPT DATA -->"),content[7].indexOf("<!-- Nutrition Facts Popup -->"));
                        Log.wtf("NUTR",nutrition);
                        for(int i = 1;i<content.length;i++){
                            String dailyContent = content[i];
                            String[] lunch = dailyContent.split("<tr class=\"lun\"><td class=\"station\">");
                            String[] dinner = lunch[lunch.length-1].split("<tr class=\"din\"><td class=\"station\">");
                            for(int x = 1;x<lunch.length;x++){
                                //    meals[i] = meals[i].substring(meals[i].indexOf(">")+1,meals[i].indexOf("<"));
                                lunch[x]=lunch[x].substring(0,lunch[x].indexOf("</span>"));
                                lunch[x]=lunch[x].substring(lunch[x].lastIndexOf(">")+1,lunch[x].length());
                                Log.wtf(TAG,lunch[x]);
                                lunchContent[i-1]+=" \u2022"+lunch[x];//
                                //ERROR lunchContent2[i-1][i]=lunchContent2[0][0].put(nutriCode,lunch[x]);
                                if(x!=lunch.length-1)
                                    lunchContent[i-1]+="\n";//Array of 6 lunch texts (Ex. lunchContent[1] = "Bread\nCarrots\nPeas...")
                            }
                            for(int y = 1;y<dinner.length;y++){
                                dinner[y]=dinner[y].substring(0,dinner[y].indexOf("</span"));
                                dinner[y]=dinner[y].substring(dinner[y].lastIndexOf(">")+1,dinner[y].length());
                                Log.wtf(TAG,dinner[y]);
                                dinnerContent[i-1]+=" \u2022"+dinner[y];
                                if(y!=lunch.length-1)
                                    dinnerContent[i-1]+="\n";
                            }
                        }
                    }
                    //Log.wtf(TAG, "PARSED! "+content[1]);

                    /*int startpos = 0;
                    int endpos = 0;
                    if (html.indexOf("/images/", html.indexOf(">On the Menu<"))!=-1){
                        startpos = html.indexOf(">On the Menu<");
                        startpos = html.indexOf("/images/", startpos);
                        endpos = html.indexOf(".htm", startpos) + 4;
                        url = "https://ctxdining.sodexomyway.net"+html.substring(startpos,endpos);
                        doc = Jsoup.connect(url).get(); //Weekly menu available
                        raw = doc.body().toString();
                        html= raw; //Rip menu to put on multi-snap recyclerview :)!
                        startpos = 0;
                        endpos = 0;
                    }else{
                        url = "https://m-ctxdining.sodexomyway.net/dining-choices/index.html";
                        doc = Jsoup.connect(url).get(); //No weekly menu available
                    }
                */

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return raw;
            }

            @Override
            protected void onPostExecute(String result) {
                //if you had a ui element, you could display the title
                /*
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
                */
            }
        }


        RetroFitParser ff = new RetroFitParser();
        ff.execute();
        //browser = view.findViewById(R.id.webViewDining);
        progressBar = view.findViewById(R.id.progressBarDining);

        class HorizontalAdapter extends RecyclerView.Adapter<com.example.nhickam.concordianavigation.HorizontalAdapter.ViewHolder> {
            private String[] daysOfWeek;
            private String[] cal_Day;
            private String[][] lunchContent;
            private Map<String, String>[][] lunchContent2;
            private String[] lunch;
            private String[] dinnerContent;


            public HorizontalAdapter(String[] daysOfWeek,String[] calDay,Map<String, String>[][] lunchContent2, String[] dinnerContent) {
                this.daysOfWeek = daysOfWeek;
                this.cal_Day = calDay;
                this.lunchContent2 = lunchContent2;
                this.dinnerContent = dinnerContent;
            }

            @Override
            public com.example.nhickam.concordianavigation.HorizontalAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_horizontal, viewGroup, false);
                return new com.example.nhickam.concordianavigation.HorizontalAdapter.ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(com.example.nhickam.concordianavigation.HorizontalAdapter.ViewHolder holder, int position) {
                String day = daysOfWeek[position];
                String calDay = cal_Day[position];
                holder.day.setText(day);
                //ERROR Collection<String> weekdayList = new Collection<String>[lunchContent2[position].length];
                for(int i = 0, x = 0; i < lunchContent2[position].length; i++){
                   //weekdayList[x] = lunchContent2[position][i].values());

                }

                //ERROR holder.theAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,);
                holder.lunchArr.setAdapter(holder.theAdapter);

                //String lunch = lunchContent[position];
                String dinner = dinnerContent[position];
                holder.calDay.setText(calDay);
                holder.dinner.setText(dinner);
            }

            @Override
            public int getItemCount() {
                return daysOfWeek.length;
            }

            class ViewHolder extends RecyclerView.ViewHolder {
                private TextView day;
                private TextView calDay;
                private TextView lunch;
                private ListAdapter theAdapter;
                private ListView lunchArr;
                private TextView dinner;

                ViewHolder(final View itemView) {
                    super(itemView);
                    this.day = (TextView) itemView.findViewById(R.id.content);
                    this.calDay = (TextView) itemView.findViewById(R.id.calendarDay);
                    this.lunchArr = (ListView) itemView.findViewById(R.id.lunch_array_text);
                    this.lunchArr.setAdapter(theAdapter);
                    this.dinner = (TextView) itemView.findViewById(R.id.dinner_text);
                }
            }
        }
        HorizontalAdapter firstAdapter = new HorizontalAdapter(daysOfWeek,calDay,lunchContent2,dinnerContent);
        final MultiSnapRecyclerView diningRecyclerView = (MultiSnapRecyclerView) view.findViewById(R.id.Dining);
        final LinearLayoutManager firstManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        diningRecyclerView.setLayoutManager(firstManager);
        diningRecyclerView.setAdapter(firstAdapter);

        Calendar c = Calendar.getInstance();
        diningRecyclerView.scrollToPosition((c.get(Calendar.DAY_OF_WEEK))-2); //Get current day and set the recyclerview accordingly

        /*
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        // Gets linearlayout
        LinearLayout layout = view.findViewById(R.id.custom_layout);
        // Gets the layout params that will allow you to resize the layout
        ViewGroup.LayoutParams params = layout.getLayoutParams();

        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpWidth*((float).8), r.getDisplayMetrics());
        // Changes the height and width to the specified *pixels*
        params.height = (int)dpHeight;
        params.width = (int)px;
        layout.setLayoutParams(params);
*/
        /*
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
        */



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

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    private void setDimensions(View view, int width, int height){
        android.view.ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    public static String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }

    //Thanks to Sarz on StackOverflow for allowing anyone to implement a nice date displayer! Ex: "20th of March 2018"
    public static String getFormattedDate(Date date){
        Calendar cal=Calendar.getInstance();
        cal.setTime(date);
        //2nd of march 2015
        int day=cal.get(Calendar.DATE);

        if(!((day>10) && (day<19)))
            switch (day % 10) {
                case 1:
                    return new SimpleDateFormat("d'st' 'of' MMMM yyyy").format(date);
                case 2:
                    return new SimpleDateFormat("d'nd' 'of' MMMM yyyy").format(date);
                case 3:
                    return new SimpleDateFormat("d'rd' 'of' MMMM yyyy").format(date);
                default:
                    return new SimpleDateFormat("d'th' 'of' MMMM yyyy").format(date);
            }
        return new SimpleDateFormat("d'th' 'of' MMMM yyyy").format(date);
    }
}