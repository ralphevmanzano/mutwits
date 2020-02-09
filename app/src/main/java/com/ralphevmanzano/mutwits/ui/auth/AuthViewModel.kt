package com.ralphevmanzano.mutwits.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.example.todo_app.util.Event
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthCredential
import com.ralphevmanzano.mutwits.util.Prefs
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val prefs: Prefs) :
  BaseViewModel() {

  private val _loginEvent = MutableLiveData<Event<Unit>>()
  val loginEvent: LiveData<Event<Unit>> = _loginEvent

  fun login() {
    _loginEvent.value = Event(Unit)
  }

  fun saveToken(authResult: AuthResult) {
    val oAuthToken = (authResult.credential as OAuthCredential).accessToken
    val oAuthSecret = (authResult.credential as OAuthCredential).secret

    oAuthSecret?.let {
      prefs.save(Prefs.ACCESS_TOKEN, oAuthToken)
      prefs.save(Prefs.SECRET, oAuthSecret)
    }
  }
}
