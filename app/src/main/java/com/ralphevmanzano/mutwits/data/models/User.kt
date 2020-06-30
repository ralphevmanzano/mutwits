package com.ralphevmanzano.mutwits.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import timber.log.Timber

@IgnoreExtraProperties
@Entity
@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "id_str")
    @PrimaryKey
    val id: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "screen_name")
    val userName: String = "",
    @Json(name = "profile_image_url_https")
    val imgUrl: String = "",
    @Exclude
    var isSelected: Boolean = false,
    @Json(name = "muting")
    var isMuted: Boolean = false
) {
    val profileImage: String
        get() {
            Timber.d("")
            return imgUrl.replace("_normal", "_bigger")
        }

    val formattedUserName: String
        get() = "@$userName"

    fun toggleSelected() {
        isSelected = !isSelected
    }

    override fun toString(): String {
        return "[User]: id_str=$id\t" +
                "name=$name\t" +
                "screen_name=$userName\t" +
                "profile_image_url_https=$imgUrl\t" +
                "isSelected=$isSelected"
    }
}