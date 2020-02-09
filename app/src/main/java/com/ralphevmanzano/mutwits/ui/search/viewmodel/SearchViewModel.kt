package com.ralphevmanzano.mutwits.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.repo.MutwitsRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class SearchViewModel @Inject constructor(private val mutwitsRepo: MutwitsRepo) : BaseViewModel() {

  val query = MutableLiveData<String>()

  private val _isLoading = MutableLiveData<Boolean>()
  val isLoading: LiveData<Boolean> = _isLoading

  private val _users = MediatorLiveData<List<User>>()
  val users: LiveData<List<User>> = _users
//  val users = mutwitsRepo.getFriends().asLiveData()

  private val _selectedUsers = MutableLiveData<List<User>>(emptyList())
  val selectedUsers: LiveData<List<User>> = _selectedUsers

  private val selectedUsersList = mutableListOf<User>()

  private var queryTextChangedJob: Job? = null

  init {
    _users.addSource(query) { queryStr ->
      Timber.d("query: $queryStr")
      if (queryStr.isNullOrEmpty()) getAllFriends()
      else searchUsers(queryStr)
    }

    getAllFriends()
  }

  private fun getAllFriends() = viewModelScope.launch {
    mutwitsRepo.getFriends().collect {
      if (query.value.isNullOrEmpty()) {
        _users.value = it
      }
    }
  }

  private fun searchUsers(query: String) {
    _isLoading.value = true
    queryTextChangedJob?.cancel()
    queryTextChangedJob = viewModelScope.launch {
      delay(300)
      getFriendsByName(query)
    }
  }

  private suspend fun getFriendsByName(q: String) {
    mutwitsRepo.getFriendsByName(q)
      .collect {
        if (!query.value.isNullOrEmpty()) {
          Timber.d("users: $it")
          _users.value = it
          _isLoading.value = false
        }
      }
  }

  fun selectUser(user: User) {
    user.isSelected = user.isSelected.not()
    updateList(user)

    viewModelScope.launch {
      mutwitsRepo.updateUser(user)
    }

  }

  private fun updateList(user: User) {
    if (user.isSelected) selectedUsersList.add(user)
    else selectedUsersList.removeAll { it.id == user.id }

    _selectedUsers.value = selectedUsersList
  }

  override fun onCleared() {
    super.onCleared()
    queryTextChangedJob?.cancel()
  }

}
