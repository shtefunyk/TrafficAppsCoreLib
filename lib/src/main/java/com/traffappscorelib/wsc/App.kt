package com.traffappscorelib.wsc

import android.app.Application
import com.google.firebase.FirebaseApp

abstract class App : Application() {

    companion object {
        lateinit var instance: App
    }

    abstract fun showIntro(): Boolean
    abstract fun getIntroItems(): List<IntroItem>
    abstract fun getIntroBgColor() : Int
    abstract fun getAppUiClassName() : Class<*>

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppThemeLib)
        instance = this

    }
}