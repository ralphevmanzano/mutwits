package com.ralphevmanzano.mutwits.data.remote

import com.ralphevmanzano.mutwits.data.remote.models.MuteListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitterService {

  @GET("mutes/users/list.json")
  suspend fun getMutedUsers(@Query("cursor") nextCursor: String? = null): MuteListResponse

}