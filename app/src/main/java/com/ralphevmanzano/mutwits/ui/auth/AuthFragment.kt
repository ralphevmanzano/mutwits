package com.ralphevmanzano.mutwits.ui.auth

import android.util.Log
import com.example.kotlin_starter_app.ui.BaseFragment
import com.example.todo_app.util.EventObserver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.AuthFragmentBinding
import com.ralphevmanzano.mutwits.util.NavEventArgs
import com.ralphevmanzano.mutwits.util.Prefs
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import kotlin.experimental.and


class AuthFragment : BaseFragment<AuthViewModel, AuthFragmentBinding>() {

  private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

  override fun getViewModel(): Class<AuthViewModel> {
    return AuthViewModel::class.java
  }

  override fun getLayoutRes(): Int {
    return R.layout.auth_fragment
  }

  override fun observeEvents() {
    viewModel.loginEvent.observe(viewLifecycleOwner, EventObserver {
      checkPendingResult()
    })
  }

  private fun checkPendingResult() {
    val pendingResultTask = firebaseAuth.pendingAuthResult
    if (pendingResultTask != null) {
      pendingResultTask.addOnSuccessListener {
        navigateTo(NavEventArgs(R.id.act_auth_to_home))
        Log.d("Main", it.toString())
      }.addOnFailureListener {
        Log.e("Main", it.localizedMessage!!)
      }
    } else {
      startSignInFlow()
    }
  }

  private fun startSignInFlow() {
    val provider = OAuthProvider.newBuilder("twitter.com")
    activity?.let { it ->
      firebaseAuth.startActivityForSignInWithProvider(it, provider.build())
        .addOnSuccessListener { authResult ->
          viewModel.saveToken(authResult)
          navigateTo(NavEventArgs(R.id.act_auth_to_home))
        }.addOnFailureListener { e ->
          Log.e("Main", e.message!!)
        }
    }
  }
}
