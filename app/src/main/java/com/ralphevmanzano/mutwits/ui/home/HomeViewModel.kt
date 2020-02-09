package com.ralphevmanzano.mutwits.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.example.todo_app.util.Event
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.remote.Result.*
import com.ralphevmanzano.mutwits.data.repo.MutwitsRepo
import com.ralphevmanzano.mutwits.util.NavEventArgs
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
class HomeViewModel @Inject constructor(private val mutwitsRepo: MutwitsRepo) : BaseViewModel() {

  private val _mutedUsers = MutableLiveData<List<User>>(emptyList())
  val mutedUsers: LiveData<List<User>> = _mutedUsers

  init {
//    getMutedUsers()
    getFriendsIds()
  }

  private fun getMutedUsers() = viewModelScope.launch {
    val temp = mutableListOf<User>()
    try {
      val muteListResponse = mutwitsRepo.getMutedUsers()
      temp.addAll(muteListResponse.users)
      var nextCursor = muteListResponse.next_cursor_str
      while (nextCursor != "0") {
        val response = mutwitsRepo.getMutedUsers(nextCursor)
        temp.addAll(response.users)
        nextCursor = response.next_cursor_str
      }

      _mutedUsers.value = temp
    } catch (e: Exception) {
      Timber.e(e)
    }
  }

  private fun getFriendsIds() = viewModelScope.launch {
    when (val response = mutwitsRepo.getFriendIds()) {
      is NetworkError -> print("Error")
      is GenericError -> print("Generic error")
      is Success -> handleIds(response.data.ids)
    }
  }

  private fun handleIds(ids: List<Long>) = viewModelScope.launch {
    val chunkedIds = ids.chunked(100)

    chunkedIds.asFlow()
      .map { cids -> cids.joinToString(",") }
      .flatMapMerge {
        flow { emit(mutwitsRepo.lookupFriends(it)) }
      }
      .collect {
        when (it) {
          is NetworkError -> print("Error")
          is GenericError -> print("Generic error")
          is Success -> mutwitsRepo.saveFriends(it.data)
        }
      }
  }

  fun goToSearch() {
    _navigationEvent.value = Event(NavEventArgs(R.id.act_home_to_search))
  }

}
