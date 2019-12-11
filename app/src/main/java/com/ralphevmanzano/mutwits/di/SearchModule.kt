package com.ralphevmanzano.mutwits.di

import androidx.lifecycle.ViewModel
import com.ralphevmanzano.mutwits.ui.home.HomeFragment
import com.ralphevmanzano.mutwits.ui.home.HomeViewModel
import com.ralphevmanzano.mutwits.ui.search.SearchFragment
import com.ralphevmanzano.mutwits.ui.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class SearchModule {
  @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
  internal abstract fun searchFragment(): SearchFragment

  @Binds
  @IntoMap
  @ViewModelKey(SearchViewModel::class)
  abstract fun bindsSearchViewModel(viewModel: SearchViewModel): ViewModel

}