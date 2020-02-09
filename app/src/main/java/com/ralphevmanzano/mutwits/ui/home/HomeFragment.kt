package com.ralphevmanzano.mutwits.ui.home

import android.os.Bundle
import android.view.View
import com.example.kotlin_starter_app.ui.BaseFragment
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.HomeFragmentBinding

class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding>() {

  override val layoutRes: Int
    get() = R.layout.home_fragment

  override val viewModelClass: Class<HomeViewModel>
    get() = HomeViewModel::class.java

  override fun setupToolbar() {
    mainActivity.setupToolbar(
      title = getString(R.string.app_name),
      showBackBtn = false
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    setupList()
  }

  private fun setupList() {
    val adapter = HomeAdapter()
    binding.rv.adapter = adapter
  }
}
