package com.ralphevmanzano.mutwits.ui.home

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlin_starter_app.ui.BaseFragment

import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.HomeFragmentBinding
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding>() {

  override fun getViewModel(): Class<HomeViewModel> = HomeViewModel::class.java

  override fun getLayoutRes(): Int = R.layout.home_fragment

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    setupList()
  }

  private fun setupList() {
    val adapter = HomeAdapter()
    rv.adapter = adapter
  }

}
