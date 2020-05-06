package com.ralphevmanzano.mutwits.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.ralphevmanzano.mutwits.data.local.dao.UserDao
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.remote.Result
import com.ralphevmanzano.mutwits.data.remote.TwitterService
import com.ralphevmanzano.mutwits.data.remote.models.FriendIdsResponse
import com.ralphevmanzano.mutwits.data.remote.models.MuteListResponse
import com.ralphevmanzano.mutwits.data.remote.safeApiCall
import com.ralphevmanzano.mutwits.util.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class MutwitsRepo @Inject constructor(
  private val db: FirebaseFirestore,
  private val userDao: UserDao,
  private val twitterService: TwitterService
) {

  companion object {
    const val FIRESTORE_LIST_PATH = "list"
  }

  suspend fun getMutedUsers(nextCursor: String? = null): MuteListResponse =
    withContext(Dispatchers.IO) {
      return@withContext twitterService.getMutedUsers(nextCursor)
    }

  @FlowPreview
  suspend fun fetchFriends(): Result<Unit> = safeApiCall(Dispatchers.IO) {
    val response = twitterService.getFriendIds()
    val chunkedIds = response.ids.chunked(100)

    chunkedIds.asFlow()
      .map { cids -> cids.joinToString(",") }
      .flatMapMerge { flow { emit(twitterService.lookupUsers(it)) } }
      .collect { userDao.saveUsers(it) }
  }

  suspend fun updateUser(user: User) = userDao.updateUser(user)

  fun getFriends() = userDao.getUsers()

  fun getFriendsByName(name: String) = userDao.getUserDistinctUntilChanged(name)

  suspend fun saveListToFirestore(users: List<User>): Result<Void> = safeApiCall(Dispatchers.IO) {
    val userId = Prefs.userId

    db.collection(FIRESTORE_LIST_PATH).document(userId).set(
      hashMapOf<String, Any>(
        FIRESTORE_LIST_PATH to users
      )
    ).await()
  }

  suspend fun saveListToDB(users: List<User>) = userDao.saveUsers(users)
}