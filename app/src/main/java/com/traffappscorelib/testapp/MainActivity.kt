package com.traffappscorelib.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trafficappscorelib.R
import com.traffappscorelib.wsc.StartActivity

class MainActivity : StartActivity() {


    override fun getPlaceholderStartActivity(): Class<*> = ExampleActivity::class.java

    override fun getAlartReceiver() = AlartReceiver::class.java
}