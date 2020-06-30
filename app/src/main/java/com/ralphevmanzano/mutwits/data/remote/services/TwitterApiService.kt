package com.ralphevmanzano.mutwits.data.remote.services

import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.remote.models.FriendIdsResponse
import com.ralphevmanzano.mutwits.data.remote.models.MuteListResponse
import com.ralphevmanzano.mutwits.data.remote.models.MutedIdsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitterApiService {

  @GET("mutes/users/list.json")
  suspend fun getMutedUsers(@Query("cursor") nextCursor: Long? = null): MuteListResponse

  @GET("users/search.json")
  suspend fun searchUsers(@Query("q") query: String): List<User>

  @GET("friends/ids.json?stringify_ids=true")
  suspend fun getFriendIds(): FriendIdsResponse

  @GET("mutes/users/ids.json")
  suspend fun getMutedIds(): MutedIdsResponse

  @GET("users/lookup.json")
  suspend fun getUsersByIds(@Query(value = "user_id", encoded = true) ids: String): List<User>

}