package com.ralphevmanzano.mutwits.di

import androidx.lifecycle.ViewModel
import com.ralphevmanzano.mutwits.ui.home.HomeFragment
import com.ralphevmanzano.mutwits.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class HomeModule {
  @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
  internal abstract fun homeFragment(): HomeFragment

  @Binds
  @IntoMap
  @ViewModelKey(HomeViewModel::class)
  abstract fun bindsHomeViewModel(viewModel: HomeViewModel): ViewModel

}