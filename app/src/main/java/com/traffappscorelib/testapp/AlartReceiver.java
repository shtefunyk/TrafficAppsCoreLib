package com.traffappscorelib.testapp;

import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.example.trafficappscorelib.BuildConfig;
import com.example.trafficappscorelib.R;
import com.traffappscorelib.wsc.notifications.BaseAlarmReceiver;

public class AlartReceiver extends BaseAlarmReceiver {

    @Override
    public int getSmallIconRes() {
        return R.drawable.ic_notification_eur;
    }

    @Override
    public int getSmallIconColor() {
        return R.color.orange;
    }

    @NonNull
    @Override
    public Class<?> getActivityOpen() {
        return MainActivity.class;
    }

    @NonNull
    @Override
    public RemoteViews getNotificationLayout(String text) {
        RemoteViews notificationLayout = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.notification_collapsed);
        notificationLayout.setCharSequence(R.id.collapsed_notification_title, "setText", text);
        return notificationLayout;
    }
}
