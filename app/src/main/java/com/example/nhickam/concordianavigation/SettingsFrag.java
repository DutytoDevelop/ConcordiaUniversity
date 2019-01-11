package com.example.nhickam.concordianavigation;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

public class SettingsFrag extends Fragment {

    private static final String TAG = "Settings" ;
    TextView mTextView;
    Switch subscribeButton;

    public SettingsFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_settings_button, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.actionbar_info){
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("App Details:");
            String setting_details = "";
            String year = "2019";
            setting_details = "Copyright © "+year+" Concordia University. All Rights Reserved.";
            alertDialog.setMessage(setting_details);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
            positiveButtonLL.gravity = Gravity.CENTER;
            positiveButton.setLayoutParams(positiveButtonLL);
        }

        return super.onOptionsItemSelected(item);
    }
}
