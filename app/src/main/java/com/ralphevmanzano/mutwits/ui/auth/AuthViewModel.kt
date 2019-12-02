package com.ralphevmanzano.mutwits.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.example.todo_app.util.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import javax.inject.Inject

class AuthViewModel @Inject constructor() : BaseViewModel() {

  private val _loginEvent = MutableLiveData<Event<Unit>>()
  val loginEvent: LiveData<Event<Unit>> = _loginEvent

  fun login() {
    _loginEvent.value = Event(Unit)
  }

}
