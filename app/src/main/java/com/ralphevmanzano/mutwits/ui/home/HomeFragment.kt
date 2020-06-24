package com.ralphevmanzano.mutwits.ui.home

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kotlin_starter_app.ui.BaseFragment
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.HomeFragmentBinding
import com.ralphevmanzano.mutwits.util.Prefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject

@FlowPreview
@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding>() {

  override val layoutRes: Int get() = R.layout.home_fragment
  override val viewModelClass: Class<HomeViewModel> get() = HomeViewModel::class.java

  override fun setupToolbar() {
    mainActivity.setupToolbar(
      title = getString(R.string.app_name),
      showBackBtn = false
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    setupList()
    initUI()
  }

  private fun initUI() {
    val profile = Prefs.userProfile
    profile?.let {
      binding.run {
        Glide.with(imgProfile.context).load(it.imgUrl.replace("_normal", "")).apply(RequestOptions().circleCrop()).into(imgProfile)
        txtUsername.text = "@${it.userName}"
        txtFriends.text = it.noOfFriends.toString()
        txtMuted.text = it.noOfMutedFriends.toString()
      }
    }
  }

  private fun setupList() {
    val adapter = HomeAdapter()
    binding.rv.adapter = adapter
  }
}
