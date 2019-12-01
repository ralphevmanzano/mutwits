package com.ralphevmanzano.mutwits.ui.auth

import androidx.lifecycle.ViewModel
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import javax.inject.Inject

class AuthViewModel @Inject constructor() : BaseViewModel() {

  fun login() {
    val provider = OAuthProvider.newBuilder("twitter.com")
    FirebaseAuth.getInstance().
  }

}
