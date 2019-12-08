package com.ralphevmanzano.mutwits.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
  val id_str: String,
  val name: String,
  val screen_name: String,
  val profile_image_url_https: String
) {
  val profileImage: String
    get() = profile_image_url_https.replace("_normal", "_bigger")

  val userName: String
    get() = "@$screen_name"
}