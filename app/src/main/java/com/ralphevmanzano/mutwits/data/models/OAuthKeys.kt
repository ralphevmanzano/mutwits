package com.ralphevmanzano.mutwits.data.models

data class OAuthKeys(
  val consumerKey: String,
  val consumerSecret: String,
  val accessToken: String? = null,
  val accessSecret: String? = null
)