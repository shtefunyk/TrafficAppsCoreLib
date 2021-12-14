package com.traffappscorelib.wsc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.onesignal.OneSignal;
import java.util.Map;
import im.delight.android.webview.AdvancedWebView;

public abstract class StartActivity extends AppCompatActivity {

    private AdvancedWebView webView;
    private FrameLayout loadingView;
    private SharedPreferences prefs;
    private boolean showWebView = false;
    private Integer systemUiVisibility;

    protected abstract @LayoutRes int getLoadingViewLayoutRes();

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateStatusBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppThemeWebView);
        setContentView(R.layout.activity_webview);
        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);

        updateStatusBar();
        initWebView();

        if(!showWebView) prepareUrlFromFirebase(new IResultListener() {
            @Override
            public void success(String result) {
                if(!TextUtils.isEmpty(result)) loadUrl(result);
                else showAppUI();
            }

            @Override
            public void failed() {
                showAppUI();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    @Override
    protected void onResume() {
        webView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        webView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        webView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webView.onActivityResult(requestCode, resultCode, data);
    }

    private void prepareUrlFromFirebase(IResultListener listener) {
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setConfigSettingsAsync(new FirebaseRemoteConfigSettings.Builder().build());
        // Получение данных
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {

                String url = firebaseRemoteConfig.getString(Constants.FIELD_URL);
                String appsflyer = firebaseRemoteConfig.getString(Constants.FIELD_APPSFLYER);
                String oneSignal = firebaseRemoteConfig.getString(Constants.FIELD_ONE_SIGNAL);
                boolean useNaming = firebaseRemoteConfig.getBoolean(Constants.FIELD_USE_NAMING);
                String countries = firebaseRemoteConfig.getString(Constants.FIELD_COUNTRIES);

                boolean canLoadUrl = isCountryAllowed(countries) && !TextUtils.isEmpty(url);

                if(canLoadUrl) {
                    if(!useNaming) listener.success(url);
                }
                else listener.failed();

                if(!TextUtils.isEmpty(oneSignal)) initOneSignal(oneSignal);
                if(!TextUtils.isEmpty(appsflyer)) {
                    if(useNaming && canLoadUrl) initAppsflyer(appsflyer, new IResultListener() {
                        @Override public void success(String result) { listener.success(result);}
                        @Override public void failed() { listener.failed();}
                    }, url);
                    else initAppsflyer(appsflyer, null, null);
                }
            }
            else listener.failed();
        })
        .addOnFailureListener(e -> listener.failed());
    }

    protected void showAppUI() {
        if(((App) getApplication()).showIntro() && prefs.getBoolean(Constants.PREFS_FIRST_LAUNCH, true)){
            prefs.edit().putBoolean(Constants.PREFS_FIRST_LAUNCH, false).apply();
            startActivity(new Intent(this, Intro.class));
        }
        else {
            startActivity(new Intent(this, ((App) getApplication()).getAppUiClassName()));
        }
        finish();
    }

    private void updateStatusBar() {
        View decorView = getWindow().getDecorView();
        if(systemUiVisibility == null) systemUiVisibility = decorView.getSystemUiVisibility();

        int orientation = getResources().getConfiguration().orientation;
        boolean landscape = orientation == Configuration.ORIENTATION_LANDSCAPE;

        int uiOptions = landscape
                ? systemUiVisibility
                : View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility(uiOptions);
    }

    private void initWebView() {
        webView = findViewById(R.id.webView);
        webView.setListener(this, new AdvancedWebView.Listener() {
            @Override
            public void onPageStarted(String url, Bitmap favicon) {
                if(url.contains("app://")) showAppUI();
                if(showWebView) {
                    webView.setVisibility(View.VISIBLE);
                    loadingView.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageFinished(String url) {
                CookieManager.getInstance().flush();
            }
            @Override public void onPageError(int errorCode, String description, String failingUrl) { }
            @Override public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }
            @Override public void onExternalPageRequest(String url) { }
        });
        webView.setWebChromeClient(new ChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(false);
        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString().replace("; wv", ""));

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        loadingView = findViewById(R.id.progress);
        View.inflate(getApplicationContext(), getLoadingViewLayoutRes(), loadingView);
    }

    private void loadUrl(String url) {
        webView.post(() -> {
            showWebView = true;
            webView.loadUrl(url);
        });
    }

    public class ChromeClient extends WebChromeClient {

        private View mCustomView;
        private CustomViewCallback mCustomViewCallback;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) return null;
            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    private boolean isCountryAllowed(String countries) {
        String country = "";
        try {
            country = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getSimCountryIso().toUpperCase();
            if (country.isEmpty()) {
                country = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getNetworkCountryIso().toUpperCase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (countries.isEmpty()) {
            // Проверка пройдена. Так как в Firebase Remote Config не прописаны страны
            return true;

        } else {
            if (countries.contains(country) && !country.isEmpty()) {
                // Проверка пройдена. Страна SIM-карты содержится в переменной countries
                return true;

            } else {
                // Проверка НЕ пройдена. Страна SIM-карты не содержится в переменной countries или SIM-карта отсутствует
                return false;
            }
        }
    }

    private void initOneSignal(String id) {
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(id);
    }

    private void initAppsflyer(String id, IResultListener listener, String url) {
        AppsFlyerLib.getInstance().init(id, new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> map) {
                if(listener != null) AppsflyerUtil.Companion.parse(map, listener, url);
            }

            @Override
            public void onConversionDataFail(String s) {
                if(listener != null) listener.failed();
            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }

            @Override
            public void onAttributionFailure(String s) {
                if(listener != null) listener.failed();
            }
        }, this);
        AppsFlyerLib.getInstance().start(this);
    }

}