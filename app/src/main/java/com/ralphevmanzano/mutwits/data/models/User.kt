package com.ralphevmanzano.mutwits.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
  val id_str: String,
  val name: String,
  val screen_name: String,
  val profile_image_url_https: String,
  var isSelected: Boolean = false
) {
  val profileImage: String
    get() = profile_image_url_https.replace("_normal", "_bigger")

  val userName: String
    get() = "@$screen_name"


  fun toggleSelected() {
    isSelected = !isSelected
  }

  override fun toString(): String {
    return "[User]: id_str=$id_str\t" +
        "name=$name\t" +
        "screen_name=$screen_name\t" +
        "profile_image_url_https=$profile_image_url_https\t" +
        "isSelected=$isSelected"
  }
}