package com.ralphevmanzano.mutwits.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.repo.MutwitsRepo
import kotlinx.coroutines.*
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val mutwitsRepo: MutwitsRepo) : BaseViewModel() {

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  private val _users = MutableLiveData<List<User>>(emptyList())
  val users: LiveData<List<User>> = _users

  var queryTextChangedJob: Job? = null

  fun searchUsers(query: String) {
    queryTextChangedJob?.cancel()
    queryTextChangedJob = viewModelScope.launch {
      delay(300)
      _isLoading.value = true
      _users.value = mutwitsRepo.searchUsers(query)
      _isLoading.value = false
    }
  }

  override fun onCleared() {
    super.onCleared()
    queryTextChangedJob?.cancel()
  }

}
