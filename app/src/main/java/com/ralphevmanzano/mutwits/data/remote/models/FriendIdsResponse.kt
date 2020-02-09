package com.ralphevmanzano.mutwits.data.remote.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FriendIdsResponse(
  @Json(name = "ids")
  val ids: List<Long>
)