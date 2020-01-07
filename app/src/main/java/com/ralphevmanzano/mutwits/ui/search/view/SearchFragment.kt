package com.ralphevmanzano.mutwits.ui.search.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.kotlin_starter_app.ui.BaseFragment

import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.SearchFragmentBinding
import com.ralphevmanzano.mutwits.ui.search.adapter.SearchAdapter
import com.ralphevmanzano.mutwits.ui.search.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class SearchFragment : BaseFragment<SearchViewModel, SearchFragmentBinding>() {

  @Inject
  lateinit var adapter: SearchAdapter

  override val layoutRes: Int
    get() = R.layout.search_fragment

  override val viewModelClass: Class<SearchViewModel>
    get() = SearchViewModel::class.java

  override fun setupToolbar() {
    mainActivity.setupToolbar(show = false)
    toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    initObservers()
  }

  private fun initObservers() {
    viewModel.users.observe(viewLifecycleOwner, Observer { users ->
      users?.let {
        Log.d("Fragment", "--------------")
        adapter.submitList(users.toList())
      }
    })
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    initList()
  }

  private fun initList() {
    adapter.setOnAddToListListener{ user ->
      viewModel.selectUser(user)
    }
    binding.rv.adapter = adapter
  }
}
