package com.ralphevmanzano.mutwits.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.example.todo_app.util.Event
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.ralphevmanzano.mutwits.data.remote.TwitterService
import com.ralphevmanzano.mutwits.data.repo.AuthRepo
import com.ralphevmanzano.mutwits.util.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val authRepo: AuthRepo) :
  BaseViewModel() {

  private val _loginEvent = MutableLiveData<Event<Unit>>()
  val loginEvent: LiveData<Event<Unit>> = _loginEvent

  fun login() {
    _loginEvent.value = Event(Unit)
  }

  fun saveToken(authResult: AuthResult) {
    val oAuthToken = (authResult.credential as OAuthCredential).accessToken
    val oAuthSecret = (authResult.credential as OAuthCredential).secret

    oAuthSecret?.let { authRepo.saveTokens(oAuthToken, it) }
  }
}
