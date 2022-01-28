package com.traffappscorelib.testapp

import com.traffappscorelib.wsc.StartActivity

class MainActivity : StartActivity() {

    override fun getPlaceholderStartActivity(): Class<*> = ExampleActivity::class.java

    override fun getAlartReceiver() = AlartReceiver::class.java
}