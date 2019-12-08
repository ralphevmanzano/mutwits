package com.ralphevmanzano.mutwits.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.repo.MutwitsRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val mutwitsRepo: MutwitsRepo) : BaseViewModel() {

  private val _mutedUsers = MutableLiveData<List<User>>(emptyList())
  val mutedUsers: LiveData<List<User>> = _mutedUsers

  init {
    getMutedUsers()
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
      Log.e("Error", "Http error: ${e.localizedMessage}")
    }


  }

}
