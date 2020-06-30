package com.ralphevmanzano.mutwits.data.repo

import com.google.firebase.firestore.FirebaseFirestore
import com.ralphevmanzano.mutwits.data.local.dao.UserDao
import com.ralphevmanzano.mutwits.data.models.Resource
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.models.UserListDocument
import com.ralphevmanzano.mutwits.data.remote.Result
import com.ralphevmanzano.mutwits.data.remote.models.MutedIdsResponse
import com.ralphevmanzano.mutwits.data.remote.safeApiCall
import com.ralphevmanzano.mutwits.data.remote.services.TwitterService
import com.ralphevmanzano.mutwits.util.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
class MutwitsRepo @Inject constructor(
    private val db: FirebaseFirestore,
    private val userDao: UserDao,
    private val twitterService: TwitterService
) {

    companion object {
        const val FIRESTORE_LIST_PATH = "list"
    }

    suspend fun fetchMutedUsers(nextCursor: Long? = null) = twitterService.getMutedUsers(nextCursor)

    @FlowPreview
    suspend fun fetchFriends(): Result<Unit> = safeApiCall(Dispatchers.IO) {
        val response = twitterService.getFriendIds()
        val chunkedIds = response.ids.chunked(100)

        chunkedIds.asFlow()
            .map { cids -> cids.joinToString(",") }
            .flatMapMerge { flow { emit(twitterService.getUsersByIds(it)) } }
            .collect { userDao.saveUsersIgnoreConflict(it) }
    }

    @FlowPreview
    suspend fun fetchMutedUsers(): Result<Unit> = safeApiCall(Dispatchers.IO) {
        val response = twitterService.getMutedIds()
        val chunkedIds = response.ids.chunked(100)

        chunkedIds.asFlow()
            .map { cids -> cids.joinToString(",") }
            .flatMapMerge { flow { emit(twitterService.getUsersByIds(it)) } }
            .collect {
                it.forEach { user -> user.isSelected = true }
                saveList(it)
            }
    }

    suspend fun fetchMutedIds(): Result<MutedIdsResponse> = safeApiCall(Dispatchers.IO) {
        twitterService.getMutedIds()
    }

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    fun getFriends() = userDao.getUsers()

    fun getFriendsByName(name: String) = userDao.getUserDistinctUntilChanged(name)

    suspend fun saveList(users: List<User>): Result<Void> {
        val firestoreResult = saveListToFirestore(users)
        if (firestoreResult is Result.Success) {
            saveListToDB(users)
        }

        return firestoreResult
    }

    suspend fun saveListToFirestore(users: List<User>): Result<Void> = safeApiCall(Dispatchers.IO) {
        val userId = Prefs.userId

        db.collection(FIRESTORE_LIST_PATH).document(userId).set(
            hashMapOf<String, Any>(
                FIRESTORE_LIST_PATH to users
            )
        ).await()
    }

    suspend fun saveListToDB(users: List<User>) = userDao.saveUsers(users)

    suspend fun fetchList(): Flow<Resource<List<User>>> = callbackFlow {
        val userId = Prefs.userId
        val ref = db.collection(FIRESTORE_LIST_PATH).document(userId)

        val subscription = ref.addSnapshotListener { snapshot, _ ->
            snapshot?.let {
                if (it.exists()) {
                    val list = snapshot.toObject(UserListDocument::class.java)
                    Timber.d("List!! $list")
                    list?.let {
                        offer(Resource.Success(it.list))
                    }
                }
            }
        }

        awaitClose { subscription.remove() }
    }
}