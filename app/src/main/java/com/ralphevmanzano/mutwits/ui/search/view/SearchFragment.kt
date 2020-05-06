package com.ralphevmanzano.mutwits.ui.search.view

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.kotlin_starter_app.ui.BaseFragment
import com.example.todo_app.util.EventObserver

import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.SearchFragmentBinding
import com.ralphevmanzano.mutwits.ui.common.LoadingDialog
import com.ralphevmanzano.mutwits.ui.search.adapter.SearchAdapter
import com.ralphevmanzano.mutwits.ui.search.viewmodel.SearchViewModel
import com.ralphevmanzano.mutwits.util.extensions.observe
import com.ralphevmanzano.mutwits.util.extensions.observeEvent
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

  private val loadingDialog = LoadingDialog()

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

      observe(addedUsers) {
        it?.let { users ->
          binding.txtAddedUsers.text =
            resources.getQuantityString(R.plurals.usersToBeAdded, users.size, users.size)
        }
      }

      observe(removedUsers) {
        it?.let { users ->
          binding.txtRemovedUsers.text =
            resources.getQuantityString(R.plurals.usersToBeRemoved, users.size, users.size)
        }
      }

      observeEvent(loadingEvent) {
        it?.let { show ->
          Timber.d("Show: $show")
          if (show && !loadingDialog.isVisible) loadingDialog.show(
            activity?.supportFragmentManager!!,
            "loading_dialog"
          )
          else if (loadingDialog.isVisible) {
            loadingDialog.dismiss()
          }
        }
      }

    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    initList()
  }

  private fun initList() {
    adapter.setOnAddToListListener { user, pos ->
      vm.selectUser(user)
    }
    
    binding.rv.adapter = adapter
  }
}
