package com.ralphevmanzano.mutwits.ui.home

import androidx.hilt.lifecycle.ViewModelInject
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
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@FlowPreview
class HomeViewModel @ViewModelInject constructor(private val mutwitsRepo: MutwitsRepo) : BaseViewModel() {

  private val _mutedUsers = MutableLiveData<List<User>>(emptyList())
  val mutedUsers: LiveData<List<User>> = _mutedUsers

  init {
//    getMutedUsers()
//    getFriendsIds()
//    fetchFriends()
  }

  private fun getMutedUsers() = viewModelScope.launch {
    val temp = mutableListOf<User>()
    try {
      val muteListResponse = mutwitsRepo.fetchMutedUsers()
      temp.addAll(muteListResponse.users)
      var nextCursor = muteListResponse.next_cursor_str
      while (nextCursor != "0") {
        val response = mutwitsRepo.fetchMutedUsers(nextCursor)
        temp.addAll(response.users)
        nextCursor = response.next_cursor_str
      }

      _mutedUsers.value = temp
    } catch (e: Exception) {
      Timber.e(e)
    }
  }

  fun goToSearch() {
    _navigationEvent.value = Event(NavEventArgs.Destination(R.id.act_home_to_search))
  }

}
