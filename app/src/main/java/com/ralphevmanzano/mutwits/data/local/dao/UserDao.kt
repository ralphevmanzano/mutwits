package com.ralphevmanzano.mutwits.data.local.dao

import androidx.room.*
import com.ralphevmanzano.mutwits.data.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
abstract class UserDao {
  @Query("SELECT * FROM user ORDER BY LOWER(userName) ASC")
  abstract fun getUsers(): Flow<List<User>>

  @Query("SELECT * FROM user WHERE name LIKE '%' || :name || '%' OR userName LIKE '%' || :name || '%'")
  abstract fun getUsersByName(name: String? = ""): Flow<List<User>>

  @ExperimentalCoroutinesApi
  fun getUserDistinctUntilChanged(name: String) = getUsersByName(name).distinctUntilChanged()

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  abstract suspend fun saveUsers(users: List<User>)

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  abstract suspend fun saveUsersIgnoreConflict(users: List<User>)

  @Update
  abstract suspend fun updateUser(user: User)

  @Query("DELETE FROM user")
  abstract suspend fun deleteEntries()
}