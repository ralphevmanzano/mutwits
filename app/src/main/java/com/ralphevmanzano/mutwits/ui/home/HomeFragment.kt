package com.ralphevmanzano.mutwits.ui.home

import android.os.Bundle
import android.view.View
import com.example.kotlin_starter_app.ui.BaseFragment
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.HomeFragmentBinding
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding>() {

  override val layoutRes: Int
    get() = R.layout.home_fragment

  override val viewModelClass: Class<HomeViewModel>
    get() = HomeViewModel::class.java

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    mainActivity.setupToolbar(true)
    setupList()
  }

  private fun setupList() {
    val adapter = HomeAdapter()
    rv.adapter = adapter
  }

}
