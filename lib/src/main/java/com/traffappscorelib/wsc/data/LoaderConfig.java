package com.traffappscorelib.wsc.data;

import android.app.Activity;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.traffappscorelib.wsc.interfaces.IValueListener;

public class LoaderConfig {

    public static final String CONFIG_FIELD_ALLOW_ORGANIC = "allow_organic";
    public static final String CONFIG_FIELD_MULTIPLE_USE = "multiple_use";
    public static final String CONFIG_FIELD_ONE_SIGNAL_ID = "one_signal_id";

    public static class Config {
        public boolean multipleUse;
        public boolean allowOrganic; //only for single use (multipleUse = false)
        public String oneSignal;
    }

    public static void prepareConfig(Activity context, IValueListener<Config> listener) {
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().build());
        firebaseRemoteConfig
            .fetchAndActivate()
            .addOnCompleteListener(context, task -> {
                if(task.isSuccessful()) {
                    boolean multipleUse = firebaseRemoteConfig.getBoolean(CONFIG_FIELD_MULTIPLE_USE);
                    boolean allowOrganic = firebaseRemoteConfig.getBoolean(CONFIG_FIELD_ALLOW_ORGANIC);
                    String oneSignal = firebaseRemoteConfig.getString(CONFIG_FIELD_ONE_SIGNAL_ID);

                    Config config = new Config();
                    config.multipleUse = multipleUse;
                    config.allowOrganic = allowOrganic;
                    config.oneSignal = oneSignal;
                    listener.value(config);
                }
                else listener.failed();
            })
            .addOnFailureListener(e -> listener.failed());
    }
}
