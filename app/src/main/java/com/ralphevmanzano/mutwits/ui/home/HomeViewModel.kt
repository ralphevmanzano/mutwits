package com.ralphevmanzano.mutwits.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.example.todo_app.util.Event
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.models.Resource
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.remote.Result
import com.ralphevmanzano.mutwits.data.remote.Result.*
import com.ralphevmanzano.mutwits.data.repo.MutwitsRepo
import com.ralphevmanzano.mutwits.util.NavEventArgs
import com.ralphevmanzano.mutwits.util.extensions.toLiveData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class HomeViewModel @ViewModelInject constructor(private val mutwitsRepo: MutwitsRepo) :
    BaseViewModel() {

    private val _fetchMutedUsers = MutableLiveData<Resource<Any>>()
    val fetchMutedUsers = _fetchMutedUsers.toLiveData()

    @InternalCoroutinesApi
    val mutedUsers = liveData(Dispatchers.IO) {
        emit(Resource.Loading)
        try {
            mutwitsRepo.fetchList().collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage!!))
            Timber.e(e)
        }
    }

    init {
//    getMutedUsers()
//    getFriendsIds()
//    fetchFriends()
    }


    fun getMutedUsers() = viewModelScope.launch {
        _fetchMutedUsers.value = Resource.Loading
        _fetchMutedUsers.value = when(mutwitsRepo.fetchMutedUsers()) {
            is Success -> Resource.Success(Unit)
            is NetworkError, is GenericError -> Resource.Error("An unexpected error occurred")
        }
    }

    fun goToSearch() {
        _navigationEvent.value = Event(NavEventArgs.Destination(R.id.act_home_to_search))
    }

}
