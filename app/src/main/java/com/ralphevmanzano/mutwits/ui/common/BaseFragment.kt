package com.example.kotlin_starter_app.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todo_app.util.EventObserver
import com.ralphevmanzano.mutwits.BR
import com.ralphevmanzano.mutwits.ui.MainActivity
import com.ralphevmanzano.mutwits.util.NavEventArgs
import javax.inject.Inject

abstract class BaseFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {

  @get:LayoutRes
  protected abstract val layoutRes: Int

  protected abstract val viewModelClass: Class<VM>

  protected lateinit var vm: VM
  protected lateinit var binding: DB

  val mainActivity: MainActivity by lazy (mode = LazyThreadSafetyMode.NONE) {
    activity as MainActivity
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    vm = ViewModelProvider(this).get(viewModelClass)
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
    binding.setVariable(BR.viewModel, vm)
    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    observeNavigationEvents()
    observeLiveData()
  }

  protected open fun observeLiveData() {}

  private fun observeNavigationEvents(){
    vm.navigationEvent.observe(viewLifecycleOwner, EventObserver {
      navigateTo(it)
    })
  }

  protected fun navigateTo(args: NavEventArgs) {
    view?.post {
      when(args) {
        is NavEventArgs.Destination -> findNavController().navigate(args.actionId, args.bundle)
        is NavEventArgs.Pop -> findNavController().popBackStack()
      }
    }
  }

}