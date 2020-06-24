package com.ralphevmanzano.mutwits.ui.search.view

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.kotlin_starter_app.ui.BaseFragment
import com.example.todo_app.util.EventObserver

import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.databinding.SearchFragmentBinding
import com.ralphevmanzano.mutwits.ui.common.LoadingDialog
import com.ralphevmanzano.mutwits.ui.search.adapter.SearchAdapter
import com.ralphevmanzano.mutwits.ui.search.viewmodel.SearchViewModel
import com.ralphevmanzano.mutwits.util.extensions.observe
import com.ralphevmanzano.mutwits.util.extensions.observeEvent
import com.zhuinden.livedatacombinetuplekt.combineTuple
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.FlowPreview
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
@AndroidEntryPoint
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

  override fun observeLiveData() {
    vm.apply {
      observe(users) { users ->
        users?.let {
          adapter.submitList(users)
        }
      }

      combineTuple(addedUsers, removedUsers).observe(viewLifecycleOwner, Observer { (added, removed) ->
        binding.run {
          if (!added.isNullOrEmpty() || !removed.isNullOrEmpty()) {
            banner.visibility = View.VISIBLE
            added?.let {
              if (it.isNotEmpty()) txtAddedUsers.text = resources.getQuantityString(R.plurals.usersToBeAdded, it.size, it.size)
              txtAddedUsers.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            }
            removed?.let {
              if (it.isNotEmpty()) binding.txtRemovedUsers.text = resources.getQuantityString(R.plurals.usersToBeRemoved, it.size, it.size)
              txtRemovedUsers.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            }
          } else {
            banner.visibility = View.GONE
          }
        }
      })

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

      observeEvent(showLoadingList) {
        it?.let { isLoading ->
          binding.run {
            pb.visibility = if (isLoading) View.VISIBLE else View.GONE
            rv.visibility = if (isLoading) View.GONE else View.VISIBLE
          }
        }
      }
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    initList()
    binding.vm = vm
  }

  private fun initList() {
    adapter.setOnAddToListListener { user, pos ->
      vm.selectUser(user)
    }
    
    binding.rv.adapter = adapter
  }
}
