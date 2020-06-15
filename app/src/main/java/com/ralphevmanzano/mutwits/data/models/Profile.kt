package com.ralphevmanzano.mutwits.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Profile(
    val userName: String,
    val imgUrl: String,
    val noOfFriends: Int = 0,
    val noOfMutedFriends: Int = 0
)