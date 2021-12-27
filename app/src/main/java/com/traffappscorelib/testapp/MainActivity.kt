package com.traffappscorelib.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trafficappscorelib.R
import com.traffappscorelib.wsc.StartActivity

class MainActivity : StartActivity() {

    override fun getLoadingViewLayoutRes(): Int {
        return R.layout.loadingg;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}