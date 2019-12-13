package com.ralphevmanzano.mutwits.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.kotlin_starter_app.ui.BaseFragment

import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.SearchFragmentBinding

class SearchFragment : BaseFragment<SearchViewModel, SearchFragmentBinding>() {
  override val layoutRes: Int
    get() = R.layout.search_fragment

  override val viewModelClass: Class<SearchViewModel>
    get() = SearchViewModel::class.java

  override fun setupToolbar() {
    mainActivity.setupToolbar("", showBackButton = true, show = true, isSearch = true)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    mainViewModel.query.observe(viewLifecycleOwner, Observer { query ->
      Log.d("SearchFragment", query)
      if (query.isNotEmpty()) {
        viewModel.searchUsers(query)
      }
    })
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    setupList()
  }

  private fun setupList() {
    val adapter = SearchAdapter()
    binding.rv.adapter = adapter
  }
}
