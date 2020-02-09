package com.ralphevmanzano.mutwits.data.remote

import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.data.remote.models.FriendIdsResponse
import com.ralphevmanzano.mutwits.data.remote.models.MuteListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitterService {

  @GET("mutes/users/list.json")
  suspend fun getMutedUsers(@Query("cursor") nextCursor: String? = null): MuteListResponse

  @GET("users/search.json")
  suspend fun searchUsers(@Query("q") query: String): List<User>

  @GET("friends/ids.json?stringify_ids=true")
  suspend fun getFriendIds(): FriendIdsResponse

  @GET("users/lookup.json")
  suspend fun lookupUsers(@Query(value = "user_id", encoded = true) ids: String): List<User>

}