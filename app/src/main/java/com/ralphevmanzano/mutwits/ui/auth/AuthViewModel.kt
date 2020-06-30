package com.ralphevmanzano.mutwits.ui.auth

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_starter_app.ui.BaseViewModel
import com.example.todo_app.util.Event
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.OAuthCredential
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.remote.Result
import com.ralphevmanzano.mutwits.data.repo.MutwitsRepo
import com.ralphevmanzano.mutwits.util.NavEventArgs
import com.ralphevmanzano.mutwits.util.Prefs
import com.ralphevmanzano.mutwits.util.extensions.toLiveData
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

@FlowPreview
class AuthViewModel @ViewModelInject constructor(private val mutwitsRepo: MutwitsRepo) :
    BaseViewModel() {

    private val _loginEvent = MutableLiveData<Event<Unit>>()
    val loginEvent = _loginEvent.toLiveData()

    private val _showLoadingEvent = MutableLiveData<Boolean>()
    val showLoading = _showLoadingEvent.toLiveData()

    fun login() {
        _loginEvent.value = Event(Unit)
    }

    fun saveTokenAndFetchProfileInfo(authResult: AuthResult) {
        val oAuthToken = (authResult.credential as OAuthCredential).accessToken
        val oAuthSecret = (authResult.credential as OAuthCredential).secret

        oAuthSecret?.let {
            Prefs.accesToken = oAuthToken
            Prefs.secretKey = oAuthSecret
        }

        getMutedCount()
    }

    fun showLoading(shouldShow: Boolean) {
        if (_showLoadingEvent.value != shouldShow) {
            _showLoadingEvent.value = shouldShow
        }
    }

    private fun getMutedCount() = viewModelScope.launch {
        when (val mutedIdsResponse = mutwitsRepo.fetchMutedIds()) {
            is Result.Success -> {
                val temp = Prefs.userProfile
                temp?.noOfMutedFriends = mutedIdsResponse.data.ids.size
                Prefs.userProfile = temp

                showLoading(false)
                _navigationEvent.value = Event(NavEventArgs.Destination(R.id.act_auth_to_home))
            }
            else -> {//TODO: show error and sign out}
            }
        }
    }
}
