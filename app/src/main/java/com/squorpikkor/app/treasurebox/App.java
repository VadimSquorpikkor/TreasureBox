package com.squorpikkor.app.treasurebox;


import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;

public class App extends Application {
    private static Application mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        FirebaseApp.initializeApp(this);
    }

    public static Context getContext(){
        return mApplication.getApplicationContext();
    }
}
