package com.ralphevmanzano.mutwits.data.remote.models

import com.ralphevmanzano.mutwits.data.models.User
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MuteListResponse(val users: List<User>, val next_cursor: Long)