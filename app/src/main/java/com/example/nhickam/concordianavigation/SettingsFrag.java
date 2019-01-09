package com.example.nhickam.concordianavigation;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.nhickam.concordianavigation.model.Feed;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import com.example.nhickam.concordianavigation.model.Feed;
import com.example.nhickam.concordianavigation.model.item.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class SettingsFrag extends Fragment {

    private static final String TAG = "Settings" ;
    TextView mTextView;
    Switch subscribeButton;

    public SettingsFrag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //Get view, init. text view and switchbutton
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        mTextView= view.findViewById(R.id.textViewSettings);
        mTextView.setTypeface(Typeface.DEFAULT_BOLD);


        subscribeButton = view.findViewById(R.id.switchPush);
        subscribeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked) {
                    // [START subscribe_topic]
                    FirebaseMessaging.getInstance().subscribeToTopic("rss");
                    // [END subscribe_topic]

                    // Log and toast
                    String msg = getString(R.string.subscribed);
                    Log.d(TAG, msg);
                    //Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }else{
                    // [START unsubscribe_topic]
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("rss");
                    // [END unsubscribe_topic]

                    // Log and toast
                    String msg = getString(R.string.unsubscribed);
                    Log.d(TAG, msg);
                    //Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }
            }
        });



        return view;
    }

    }
