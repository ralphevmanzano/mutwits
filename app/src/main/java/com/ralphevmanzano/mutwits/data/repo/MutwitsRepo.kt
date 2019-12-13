package com.ralphevmanzano.mutwits.data.repo

import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.remote.TwitterService
import com.ralphevmanzano.mutwits.data.remote.models.MuteListResponse
import com.ralphevmanzano.mutwits.util.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MutwitsRepo @Inject constructor(val prefs: Prefs, private val twitterService: TwitterService) {
  suspend fun getMutedUsers(nextCursor: String? = null): MuteListResponse = withContext(Dispatchers.IO) {
    return@withContext twitterService.getMutedUsers(nextCursor)
  }

  suspend fun searchUsers(query: String): List<User> = withContext(Dispatchers.IO) {
    return@withContext twitterService.searchUsers(query)
  }
}