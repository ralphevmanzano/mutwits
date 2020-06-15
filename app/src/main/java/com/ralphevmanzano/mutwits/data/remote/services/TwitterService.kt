package com.ralphevmanzano.mutwits.data.remote.services

import com.ralphevmanzano.mutwits.data.models.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TwitterService @Inject constructor(private val twitterApiService: TwitterApiService) : TwitterApiService {
    override suspend fun getMutedUsers(nextCursor: String?) = withContext(Dispatchers.IO) {
        return@withContext twitterApiService.getMutedUsers(nextCursor)
    }

    override suspend fun searchUsers(query: String): List<User> {
        // No need I think
        return emptyList()
    }

    override suspend fun getFriendIds() = withContext(Dispatchers.IO) {
        return@withContext twitterApiService.getFriendIds()
    }

    override suspend fun getFriendsByIds(ids: String) = withContext(Dispatchers.IO) {
        return@withContext twitterApiService.getFriendsByIds(ids)
    }

}