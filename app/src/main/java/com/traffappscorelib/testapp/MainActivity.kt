package com.traffappscorelib.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.trafficappscorelib.R
import com.traffappscorelib.wsc.StartActivity

class MainActivity : StartActivity() {


    override fun getPlaceholderStartActivity(): Class<*> = ExampleActivity::class.java

    override fun getOneSignalId() = "782601dd-4e22-473f-a2e5-7669a5bec132"

    override fun getAlartReceiver() = AlartReceiver::class.java
}