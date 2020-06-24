package com.ralphevmanzano.mutwits.ui.auth

import android.view.View
import com.example.kotlin_starter_app.ui.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.models.Profile
import com.ralphevmanzano.mutwits.databinding.AuthFragmentBinding
import com.ralphevmanzano.mutwits.util.Prefs
import com.ralphevmanzano.mutwits.util.extensions.observe
import com.ralphevmanzano.mutwits.util.extensions.observeEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import timber.log.Timber
import javax.inject.Inject


@FlowPreview
@AndroidEntryPoint
class AuthFragment : BaseFragment<AuthViewModel, AuthFragmentBinding>() {

  @Inject lateinit var firebaseAuth: FirebaseAuth

  override val layoutRes = R.layout.auth_fragment

  override val viewModelClass: Class<AuthViewModel> = AuthViewModel::class.java

  override fun observeLiveData() {
    vm.run {
      observeEvent(loginEvent) {
        checkPendingResult()
      }
      observe(showLoading) { show ->
        show?.let {
          binding.run {
            pb.visibility = if (it) View.VISIBLE else View.GONE
            btnTwitter.visibility = if (it) View.GONE else View.VISIBLE
          }
        }
      }
    }
  }

  private fun checkPendingResult() {
    val pendingResultTask = firebaseAuth.pendingAuthResult
    if (pendingResultTask != null) {
      pendingResultTask.addOnSuccessListener {
        Timber.d(it.toString())
      }.addOnFailureListener {
        Timber.e(it.localizedMessage!!)
      }
    } else {
      startSignInFlow()
    }
  }

  private fun startSignInFlow() {
    val provider = OAuthProvider.newBuilder("twitter.com")
    activity?.let { it ->
      vm.showLoading(true)
      firebaseAuth.startActivityForSignInWithProvider(it, provider.build())
        .addOnSuccessListener { authResult ->
          val profileMap = authResult.additionalUserInfo!!.profile
          profileMap?.let {
            val imgUrl = it["profile_image_url_https"] as String
            val friendsCount = it["friends_count"] as Int
            val userName = it["screen_name"] as String

            Prefs.userProfile = Profile(userName, imgUrl, friendsCount, 0)
          }
          Timber.d("ImgUrl: ${profileMap?.get("profile_image_url_https")}")
          Timber.d("Friends: ${profileMap?.get("friends_count")}")
          Timber.d("Sign in: ${authResult.additionalUserInfo?.profile}")

          vm.saveTokenAndFetchProfileInfo(authResult)
        }.addOnFailureListener { e ->
          Timber.e("Sign in failed: ${e.localizedMessage} ${e.message!!}")
        }.addOnCompleteListener {
          vm.showLoading(false)
        }
    }
  }

  override fun setupToolbar() {
    mainActivity.setupToolbar(show = false)
  }
}
