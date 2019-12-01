package com.ralphevmanzano.mutwits.di

import android.content.Context
import com.ralphevmanzano.mutwits.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
  modules = [
    AndroidInjectionModule::class,
    AuthModule::class
  ]
)
interface AppComponent : AndroidInjector<App> {

  @Component.Factory
  interface Factory {
    fun create(@BindsInstance appContext: Context): AppComponent
  }
}