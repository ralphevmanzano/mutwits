package com.ralphevmanzano.mutwits

import com.ralphevmanzano.mutwits.di.DaggerAppComponent
import com.ralphevmanzano.mutwits.util.Prefs
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class App : DaggerApplication() {

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerAppComponent.factory().create(applicationContext)
  }

  override fun onCreate() {
    super.onCreate()

    Prefs.init(this)

    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}