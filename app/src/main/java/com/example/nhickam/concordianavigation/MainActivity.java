package com.example.nhickam.concordianavigation;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.nhickam.concordianavigation.model.Feed;
import com.example.nhickam.concordianavigation.model.item.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity {

    //Generic Log TAG
    private static final String TAG = "MainActivity";

    //Retrofit's variables
    private static final String BASE_URL = "http://www.concordia.edu/calendar/rss/";
    String raw;

    //Initialized menu, drawer, & actionbar here
    int currentMenuItemId;
    private DrawerLayout mDrawerLayout;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle mToggle;
    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open,  R.string.close);
    }

    //When activity is created, set up action bar, navigation drawer, and set homepage fragment as start up frag
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Display Action Bar at top of the screen
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.custom_action_bar_layout, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );
        getSupportActionBar().setTitle(getResources().getString(R.string.home));

        //Set up navigation drawer and toggling option
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawerLayoutMain);
        mToggle = setupDrawerToggle();
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        //Set up the items in nav drawer
        setupDrawerContent(nvDrawer);

        //Log to get id from current screen
        Log.wtf(TAG, "onCreate "+getWindow().getDecorView().findViewById(android.R.id.content).getId()+" clicked");

        //Default starting fragment
        HomePageFrag homePageFrag = new HomePageFrag();
        getSupportFragmentManager().beginTransaction().replace(R.id.flContent,homePageFrag).addToBackStack(null).commit();

        /**************************vvvConcordia Feed Updatervvv*********************/


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        ConcordiaUniversityFeed concordiaUniversityFeed = retrofit.create(ConcordiaUniversityFeed.class);

        Call<Feed> call = concordiaUniversityFeed.getFeed();

        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                raw=response.body().getChannel().toString();
                //Log.e(TAG,"onResponse: " + response.body().getChannel());
                //Log.e(TAG,"onResponse: Server response: " + response.toString());

                List<Item> items = response.body().getChannel().getItems();

                // Write events to the database
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference concordiaEvents = database.getReference("Rss");

                for(int i=0;i<items.size();i++) {
                    String temp = items.get(i).getTitle();
                    String[] arrOfStr = temp.split(" - ");
                    final String date = arrOfStr[0];
                    String title = arrOfStr[1];
                    final String concordiaTitle = title;
                    final String pubDate = items.get(i).getPubDate();


                    final String id = concordiaEvents.push().getKey();

                    final Events events = new Events(id, concordiaTitle, items.get(i).getPubDate(), items.get(i).getLink(), items.get(i).getGuid());
                    //Log.e(TAG, "ADDING ENTRY");

                    concordiaEvents.child(pubDate).setValue(events);

                    /*
                    //Duplicate check...
                    concordiaEvents.child("Rss").child(pubDate).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "DataSnapshot"+dataSnapshot.getValue());

                            // This method is called once with the initial value and again
                            // whenever data at this location is updated.
                            for(DataSnapshot x : dataSnapshot.getChildren())
                                if(!(x.child("eventDate").getValue().equals(pubDate))) {
                                    //Events event = dataSnapshot.getValue(Events.class);
                                    //eventIds.add(event);
                                    concordiaEvents.child(id).setValue(events);
                                    Log.d(TAG, "Entry is not a duplicate");
                                }else{
                                    Log.d(TAG, "Entry IS a duplicate");
                                }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });*/
                }
                //Log.e(TAG,"onResponse: Title: "+title);
                //Log.e(TAG,"onResponse: Date: "+date);
                Log.e(TAG,"onResponse: Description: "+items.get(0).getDescription());
                Log.e(TAG,"onResponse: Link: "+items.get(0).getLink());
                Log.e(TAG,"DATABASE UPDATED");
            }


            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG,"onFailure" + t.getMessage());
            }
        });

        /**************************^^^Concordia Feed Updater^^^*********************/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                            selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    //Replace current fragment with selected fragment if it's not the same fragment!
    public void selectDrawerItem(MenuItem menuItem) {
        if(menuItem.getItemId()!=currentMenuItemId){
            Log.wtf(TAG,"pastId:"+currentMenuItemId+" .. menuItem.getItemId():"+menuItem.getItemId());
            switch(menuItem.getItemId()) {
                case R.id.concordia_home:
                    HomePageFrag homePageFrag = new HomePageFrag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,homePageFrag).addToBackStack(null).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.home));
                    break;
                case R.id.nav_myinfo:
                    MyInfoFrag myinfo = new MyInfoFrag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,myinfo).addToBackStack(null).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.myinfo));
                    break;
                case R.id.nav_events:
                    EventsFrag eventsFrag = new EventsFrag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,eventsFrag).addToBackStack(null).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.events));
                    break;
                case R.id.nav_blackboard:
                    BlackBoardFrag bb = new BlackBoardFrag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,bb).addToBackStack(null).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.blackboard));
                    break;
                case R.id.nav_tutor_list:
                    ILFTutors mILFTutors = new ILFTutors();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,mILFTutors).addToBackStack(null).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.tutor_list));
                    break;
                case R.id.nav_course_catalog:
                    ILFCatalog mILFCatalog = new ILFCatalog();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,mILFCatalog).addToBackStack(null).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.catalog));
                    break;
                case R.id.nav_important_dates:
                    ILFDates mILFDates = new ILFDates();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,mILFDates).addToBackStack(null).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.dates));
                    break;
                case R.id.nav_important_map:
                    ILFMap mILFMap = new ILFMap();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,mILFMap).addToBackStack(null).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.map));
                    break;
                case R.id.nav_dining:
                    ILFDining mILFDining= new ILFDining();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,mILFDining).addToBackStack(null).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.dining));
                    break;
                /*DINING HALL PROTOTYPE case R.id.nav_dining2:
                    ILFDining_2 mILFDining_2 = new ILFDining_2();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,mILFDining_2).addToBackStack(null).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.dining));
                    break;*/
                case R.id.nav_athletics:
                    AthleticsFrag mAthleticsFrag= new AthleticsFrag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,mAthleticsFrag).addToBackStack(null).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.athletics));
                    break;
                case R.id.nav_settings:
                    SettingsFrag settingsFrag = new SettingsFrag();
                    getSupportFragmentManager().beginTransaction().replace(R.id.flContent,settingsFrag).addToBackStack(null).commit();
                    getSupportActionBar().setTitle(getResources().getString(R.string.settings));
                    break;
            }
        }
        currentMenuItemId=menuItem.getItemId();
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Close the navigation drawer
        mDrawerLayout.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setContentView(int layoutResID) {
        this.currentMenuItemId = currentMenuItemId;
        super.setContentView(layoutResID);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
