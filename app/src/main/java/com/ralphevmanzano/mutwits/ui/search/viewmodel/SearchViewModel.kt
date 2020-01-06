package com.ralphevmanzano.mutwits.ui.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.repo.MutwitsRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val mutwitsRepo: MutwitsRepo) : BaseViewModel() {

  val query = MutableLiveData<String>()

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  private val _users = MediatorLiveData<List<User>>()
  val users: LiveData<List<User>> = _users

  private var queryTextChangedJob: Job? = null

  init {
    _users.addSource(query) { queryStr ->
      Log.d("SearchViewModel", "query: $queryStr")
      if (queryStr.isNullOrEmpty()) _users.value = emptyList()
      else searchUsers(queryStr)
    }
  }

  fun searchUsers(query: String) {
    _isLoading.value = true
    queryTextChangedJob?.cancel()
    queryTextChangedJob = viewModelScope.launch {
      delay(300)
      _users.value = mutwitsRepo.searchUsers(query)
      _isLoading.value = false
    }
  }

  fun selectUser(user: User) {
    val temp = users.value
    temp?.find { it.id_str == user.id_str }?.toggleSelected()
    _users.value = temp
  }

  override fun onCleared() {
    super.onCleared()
    queryTextChangedJob?.cancel()
  }

}
