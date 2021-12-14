package com.traffappscorelib.testapp;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.traffappscorelib.wsc.App;
import com.traffappscorelib.wsc.IntroItem;

import java.util.List;

public class TestApp extends App {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());
    }

    @Override
    public boolean showIntro() {
        return false;
    }

    @NonNull
    @Override
    public List<IntroItem> getIntroItems() {
        return null;
    }

    @Override
    public int getIntroBgColor() {
        return 0;
    }

    @NonNull
    @Override
    public Class<?> getAppUiClassName() {
        return ExampleActivity.class;
    }
}
