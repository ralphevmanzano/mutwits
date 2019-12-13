package com.ralphevmanzano.mutwits.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
  val query = MutableLiveData<String>()
}