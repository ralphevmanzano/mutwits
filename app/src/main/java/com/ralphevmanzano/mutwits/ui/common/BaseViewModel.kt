package com.example.kotlin_starter_app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todo_app.util.Event
import com.ralphevmanzano.mutwits.util.NavEventArgs

open class BaseViewModel : ViewModel() {

  protected val _snackbarEvent = MutableLiveData<Event<String>>()
  val snackbarEvent: LiveData<Event<String>> = _snackbarEvent

  protected val _navigationEvent = MutableLiveData<Event<NavEventArgs>>()
  val navigationEvent: LiveData<Event<NavEventArgs>> = _navigationEvent

  protected val _loadingEvent = MutableLiveData<Event<Boolean>>()
  val loadingEvent: LiveData<Event<Boolean>> = _loadingEvent

}