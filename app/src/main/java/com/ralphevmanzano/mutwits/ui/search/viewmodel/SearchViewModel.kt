package com.ralphevmanzano.mutwits.ui.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.example.todo_app.util.Event
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.repo.MutwitsRepo
import com.ralphevmanzano.mutwits.util.NavEventArgs
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

  private val _selectedUsers = MutableLiveData<List<User>>(emptyList())
  val selectedUsers: LiveData<List<User>> = _selectedUsers

  private val _addedUsers = MutableLiveData<List<User>>(emptyList())
  val addedUsers: LiveData<List<User>> = _addedUsers

  private val _removedUsers = MutableLiveData<List<User>>(emptyList())
  val removedUsers: LiveData<List<User>> = _removedUsers

  private val addedUsersList = mutableListOf<User>()
  private val removedUsersList = mutableListOf<User>()
  private val allFriendsList = mutableListOf<User>()

  private var queryTextChangedJob: Job? = null

  init {
    _users.addSource(query) { queryStr ->
      if (queryStr.isNullOrEmpty()) getAllFriends()
      else searchUsers(queryStr)
    }

    getAllFriends()
  }

  private fun getAllFriends() = viewModelScope.launch {

    mutwitsRepo.getFriends().collect {
      if (query.value.isNullOrEmpty()) {
        allFriendsList.clear()
        allFriendsList.addAll(it)
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
      _isLoading.value = false
    }
  }

  private suspend fun getFriendsByName(q: String) {
    mutwitsRepo.getFriendsByName(q)
      .collect {
        if (!query.value.isNullOrEmpty()) {
          _users.value = it
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

  fun saveListToFirestore() = viewModelScope.launch {
    _loadingEvent.value = Event(true)

    val list = allFriendsList.filter { it.isSelected }
    mutwitsRepo.saveListToFirestore(list)

    _loadingEvent.value = Event(false)
    _navigationEvent.value = Event(NavEventArgs.Pop)
  }

  private fun updateList(user: User) {
    if (user.isSelected) {
      addedUsersList.add(user)
      _addedUsers.value = addedUsersList
    } else {
      if (addedUsersList.find { it.id == user.id } != null) {
        addedUsersList.removeAll { it.id == user.id }
        _addedUsers.value = addedUsersList
      } else {
        removedUsersList.add(user)
        _removedUsers.value = removedUsersList
      }
    }
  }

  override fun onCleared() {
    super.onCleared()
    queryTextChangedJob?.cancel()
  }

}
