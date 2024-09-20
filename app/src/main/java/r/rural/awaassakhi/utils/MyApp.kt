package r.rural.awaassakhi.utils

import android.app.Application
import android.content.res.Configuration
import android.text.TextUtils
import dagger.hilt.android.HiltAndroidApp
import rural.housing2.utils.LocaleHelper

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        AppPreferencesManager.with(this)
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (TextUtils.equals(AppPreferencesManager.getString(LocaleHelper.SELECTED_LANGUAGE), "hi"))
            LocaleHelper.setLocale(this, "hi")!!
        else
            LocaleHelper.setLocale(this, "en")!!
    }
}