package com.ralphevmanzano.mutwits.data.remote.models

import com.google.gson.annotations.JsonAdapter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
  @Json(name = "errors")
  val errors: List<ErrorItem>
) {
  data class ErrorItem(
    @Json(name = "code")
    val code: Int,
    @Json(name = "message")
    val message: String)
}