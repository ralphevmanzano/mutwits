package com.ralphevmanzano.mutwits.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.kotlin_starter_app.ui.BaseFragment
import com.example.todo_app.util.EventObserver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.AuthFragmentBinding
import com.ralphevmanzano.mutwits.util.NavEventArgs


class AuthFragment : BaseFragment<AuthViewModel, AuthFragmentBinding>() {

  private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

  override val layoutRes: Int
    get() = R.layout.auth_fragment

  override val viewModelClass: Class<AuthViewModel>
    get() = AuthViewModel::class.java


  override fun observeEvents() {
    viewModel.loginEvent.observe(viewLifecycleOwner, EventObserver {
//      navigateTo(NavEventArgs(R.id.act_auth_to_home))
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

  override fun setupToolbar() {
    mainActivity.setupToolbar(show = false)
  }
}
