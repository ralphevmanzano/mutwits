package com.ralphevmanzano.mutwits.ui.home

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.kotlin_starter_app.ui.BaseFragment
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.models.Resource
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.databinding.HomeFragmentBinding
import com.ralphevmanzano.mutwits.util.Prefs
import com.ralphevmanzano.mutwits.util.extensions.hide
import com.ralphevmanzano.mutwits.util.extensions.observe
import com.ralphevmanzano.mutwits.util.extensions.setVisible
import com.ralphevmanzano.mutwits.util.extensions.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding>() {

    private val adapter = HomeAdapter()

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
        initListeners()
    }

    private fun initUI() {
        val profile = Prefs.userProfile
        profile?.let {
            binding.run {
                Glide.with(imgProfile.context).load(it.imgUrl.replace("_normal", ""))
                    .apply(RequestOptions().circleCrop()).into(imgProfile)
                txtUsername.text = "@${it.userName}"
                txtFriends.text = it.noOfFriends.toString()
                txtMuted.text = it.noOfMutedFriends.toString()
            }
        }
    }

    private fun initListeners() {
        binding.run {
            btnImport.setOnClickListener {
                vm.getMutedUsers()
            }
        }
    }

    private fun setupList() {
        binding.rv.adapter = adapter
    }

    override fun observeLiveData() {
        vm.run {
            observe(mutedUsers) {
                it?.let { result ->
                    when(result) {
                        is Resource.Loading -> {
                            binding.apply {
                                /** Only show the create list layout when no items is in the recyclerview */
                                if (adapter.itemCount <= 0) {
                                    layoutCreateList.setVisible(View.VISIBLE)
                                    rv.setVisible(View.GONE)
                                }
                            }
                        }
                        is Resource.Success -> {
                            binding.apply {
                                val data = result.data
                                if (data is List<*>) {
                                    val list = data as List<User>
                                    list.sortedBy { user -> user.userName }
                                    adapter.submitList(list)
                                    layoutCreateList.setVisible(View.GONE)
                                    rv.setVisible(View.VISIBLE)
                                }
                            }
                        }
                        is Resource.Error -> {}
                    }
                }
            }

            observe(fetchMutedUsers) {
                it?.let { result ->
                    when(result) {
                        is Resource.Loading -> {
                            binding.apply {
                                layoutCreateList.hide()
                                rv.hide()
                                pb.show()
                            }
                        }
                        is Resource.Success -> {
                            binding.apply {
                                pb.hide()
                                rv.show()
                            }
                        }
                        is Resource.Error -> {
                            binding.apply {
                                layoutCreateList.show()
                                rv.hide()
                                pb.setVisible(View.GONE)
                            }
                        }
                    }
                }
            }
        }
    }
}
