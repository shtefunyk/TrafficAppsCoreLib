package com.traffappscorelib.testapp;

import androidx.annotation.NonNull;

import com.example.trafficappscorelib.R;
import com.traffappscorelib.wsc.notifications.BaseAlarmReceiver;

public class AlartReceiver extends BaseAlarmReceiver {

    @Override
    public int getIconRes() {
        return R.mipmap.ic_launcher;
    }

    @NonNull
    @Override
    public Class<?> getActivityOpen() {
        return MainActivity.class;
    }
}
