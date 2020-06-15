package com.ralphevmanzano.mutwits

import android.app.Application
import com.ralphevmanzano.mutwits.util.Prefs
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

  override fun onCreate() {
    super.onCreate()

    Prefs.init(this)

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}