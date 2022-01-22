package com.traffappscorelib.wsc

import android.app.Application
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.traffappscorelib.wsc.data.Analytics
import com.traffappscorelib.wsc.data.EntityAppsflyerData
import com.traffappscorelib.wsc.interfaces.IValueListener

abstract class App : Application() {

    companion object {
        lateinit var instance: App
    }

    private var listener: IValueListener<EntityAppsflyerData>? = null
    private var entity: EntityAppsflyerData? = null
    private var failed = false

    abstract fun getAppsflyerAppId() : String

    fun getAppsflyerData(listener: IValueListener<EntityAppsflyerData>) {
        when {
            entity != null -> listener.value(entity)
            failed -> listener.failed()
            else -> this.listener = listener
        }
    }

    override fun onCreate() {
        super.onCreate()
        setTheme(R.style.AppThemeLib)
        instance = this

        AppsFlyerLib.getInstance().init(getAppsflyerAppId(), object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(map: Map<String, Any>) {
//                Analytics.sendAppsflyerData(map.toString())
//
//                val status = map["af_status"]
//                val namingLong = map["campaign"]
//                val namingShort = map["c"]
//
//                val isOrganic = status.toString() != "Non-organic"
//                val naming = namingLong ?: namingShort
//                val namingString = naming?.toString()
//
//                val value = EntityAppsflyerData(isOrganic, namingString)
//
//                if(listener == null) entity = value
//                else listener?.value(value)

            }
            override fun onConversionDataFail(s: String) {
//                if(listener == null) failed = true
//                else listener?.failed()
            }
            override fun onAppOpenAttribution(map: Map<String, String>) {}
            override fun onAttributionFailure(s: String) {}
        }, this)
        AppsFlyerLib.getInstance().start(this)
    }
}