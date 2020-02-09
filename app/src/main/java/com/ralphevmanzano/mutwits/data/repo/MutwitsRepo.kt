package com.ralphevmanzano.mutwits.data.repo

import com.ralphevmanzano.mutwits.data.local.dao.UserDao
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.remote.Result
import com.ralphevmanzano.mutwits.data.remote.TwitterService
import com.ralphevmanzano.mutwits.data.remote.models.FriendIdsResponse
import com.ralphevmanzano.mutwits.data.remote.models.MuteListResponse
import com.ralphevmanzano.mutwits.data.remote.safeApiCall
import com.ralphevmanzano.mutwits.util.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MutwitsRepo @Inject constructor(
  val prefs: Prefs,
  private val userDao: UserDao,
  private val twitterService: TwitterService
) {
  suspend fun getMutedUsers(nextCursor: String? = null): MuteListResponse =
    withContext(Dispatchers.IO) {
      return@withContext twitterService.getMutedUsers(nextCursor)
    }

  suspend fun searchUsers(query: String): List<User> = withContext(Dispatchers.IO) {
    return@withContext twitterService.searchUsers(query)
  }

  suspend fun getFriendIds(): Result<FriendIdsResponse> = safeApiCall(Dispatchers.IO) {
    twitterService.getFriendIds()
  }

  suspend fun lookupFriends(ids: String): Result<List<User>> = safeApiCall(Dispatchers.IO) {
    twitterService.lookupUsers(ids)
  }

  suspend fun saveFriends(users: List<User>) = userDao.saveUsers(users)

  suspend fun updateUser(user: User) = userDao.updateUser(user)

  fun getFriends() = userDao.getUsers()

  fun getFriendsByName(name: String) = userDao.getUserDistinctUntilChanged(name)
}