package com.ralphevmanzano.mutwits.ui.search.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.example.todo_app.util.Event
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.remote.Result
import com.ralphevmanzano.mutwits.data.repo.MutwitsRepo
import com.ralphevmanzano.mutwits.util.NavEventArgs
import com.ralphevmanzano.mutwits.util.extensions.toLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@ExperimentalCoroutinesApi
@FlowPreview
class SearchViewModel @ViewModelInject constructor(private val mutwitsRepo: MutwitsRepo) :
	BaseViewModel() {

	val query = MutableLiveData<String>()

	private val _users = MediatorLiveData<List<User>>()
	val users = _users.toLiveData()

	private val _selectedUsers = MutableLiveData<List<User>>(emptyList())
	val selectedUsers: LiveData<List<User>> = _selectedUsers

	private val _addedUsers = MutableLiveData<List<User>>(emptyList())
	val addedUsers = _addedUsers.toLiveData()

	private val _removedUsers = MutableLiveData<List<User>>(emptyList())
	val removedUsers = _removedUsers.toLiveData()

	private val _showLoadingList = MutableLiveData<Event<Boolean>>()
	val showLoadingList = _showLoadingList.toLiveData()

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
		fetchFriends()
	}

	private fun getAllFriends() = viewModelScope.launch {
		mutwitsRepo.getFriends().collect {
			showLoading(it.isEmpty())

			if (query.value.isNullOrEmpty()) {
				allFriendsList.clear()
				allFriendsList.addAll(it)
				_users.value = it
			}
		}
	}

	private fun fetchFriends() = viewModelScope.launch {
		when (mutwitsRepo.fetchFriends()) {
			is Result.Success -> showLoading(false)
			else -> showLoading(false)
		}
	}

	private fun searchUsers(query: String) {
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
					_users.value = it
				}
			}
	}

	fun selectUser(user: User) {
		updateList(user)
	}

	fun saveListToFirestore() = viewModelScope.launch {
		_loadingEvent.value = Event(true)

		val list = allFriendsList.filter { it.isSelected }

		when (mutwitsRepo.saveList(list)) {
			is Result.Success -> Timber.d("Success!!")
			is Result.GenericError -> {
			}
			is Result.NetworkError -> {
			}
		}

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

	private fun showLoading(isLoading: Boolean) {
		_showLoadingList.value = Event(isLoading)
	}

    fun getAddedList() = _addedUsers.value ?: emptyList()

    fun getRemovedList() = _removedUsers.value ?: emptyList()

	override fun onCleared() {
		super.onCleared()
		queryTextChangedJob?.cancel()
	}

}
