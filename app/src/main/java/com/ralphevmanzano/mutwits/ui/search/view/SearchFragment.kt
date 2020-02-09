package com.ralphevmanzano.mutwits.ui.search.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.kotlin_starter_app.ui.BaseFragment

import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.SearchFragmentBinding
import com.ralphevmanzano.mutwits.ui.search.adapter.SearchAdapter
import com.ralphevmanzano.mutwits.ui.search.viewmodel.SearchViewModel
import com.ralphevmanzano.mutwits.util.extensions.observe
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
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
    binding.vm = vm
    
    vm.apply {
      observe(users) { users ->
        users?.let {
          adapter.submitList(users)
        }
      }
      observe(selectedUsers) { users ->
        users?.let {
          Timber.d(" size = ${it.size}")
          binding.txtSelectedUsers.text = resources.getQuantityString(R.plurals.selectedUsers, it.size, it.size)
        }
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    initList()
  }

  private fun initList() {
    adapter.setOnAddToListListener{ user, pos ->
      adapter.notifyItemChanged(pos)
      vm.selectUser(user)
    }
    binding.rv.adapter = adapter
  }
}
