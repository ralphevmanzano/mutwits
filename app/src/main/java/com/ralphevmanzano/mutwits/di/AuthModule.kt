package com.ralphevmanzano.mutwits.di

import androidx.lifecycle.ViewModel
import com.ralphevmanzano.mutwits.ui.auth.AuthFragment
import com.ralphevmanzano.mutwits.ui.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class AuthModule {

  @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
  internal abstract fun authFragment(): AuthFragment

  @Binds
  @IntoMap
  @ViewModelKey(AuthViewModel::class)
  abstract fun bindsAuthViewModel(viewModel: AuthViewModel): ViewModel

}