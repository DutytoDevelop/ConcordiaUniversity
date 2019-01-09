package com.example.nhickam.concordianavigation;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by nhickam on 12/7/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "InstanceIdService";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        //Get updated Token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"New Token: "+ refreshedToken);
    }
}
