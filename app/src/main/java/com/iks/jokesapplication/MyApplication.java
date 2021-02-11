package com.iks.jokesapplication;

import android.app.Application;

import com.iks.jokesapplication.common.ConnectionReceiver;


public class MyApplication extends Application  {
    private static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }
    public static MyApplication getInstance() {
        return mInstance;
    }
    public void setConnectivityListener(ConnectionReceiver.ConnectionReceiverListener listener) {
        ConnectionReceiver.connectionReceiverListener = listener;
    }
}
