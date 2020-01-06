package com.example.kotlin_starter_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todo_app.util.EventObserver
import com.ralphevmanzano.mutwits.BR
import com.ralphevmanzano.mutwits.ui.MainActivity
import com.ralphevmanzano.mutwits.util.NavEventArgs
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> :
  DaggerFragment() {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  @get:LayoutRes
  protected abstract val layoutRes: Int

  protected abstract val viewModelClass: Class<VM>

  protected lateinit var viewModel: VM
  protected lateinit var binding: DB

  val mainActivity: MainActivity by lazy (mode = LazyThreadSafetyMode.NONE) {
    activity as MainActivity
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(this, viewModelFactory).get(viewModelClass)
  }

  override fun onResume() {
    super.onResume()
    setupToolbar()
  }

  protected abstract fun setupToolbar()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
    binding.lifecycleOwner = viewLifecycleOwner
    binding.setVariable(BR.viewModel, viewModel)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    observeNavigationEvents()
    observeEvents()
  }

  protected open fun observeEvents() {}

  private fun observeNavigationEvents(){
    viewModel.navigationEvent.observe(viewLifecycleOwner, EventObserver {
      navigateTo(it)
    })
  }

  protected fun navigateTo(navEventArgs: NavEventArgs) {
    findNavController().navigate(navEventArgs.actionId, navEventArgs.bundle)
  }

}