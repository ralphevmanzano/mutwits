package com.ralphevmanzano.mutwits.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.ralphevmanzano.mutwits.data.models.Profile
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import java.lang.NullPointerException
import javax.inject.Inject

object Prefs {
  private const val PREF_NAME = "mutwits_pref"

  private val ACCESS_TOKEN = Pair("ACCESS_TOKEN", "")
  private val SECRET_KEY = Pair("SECRET_KEY", "")
  private val USER_ID = Pair("USER_ID", "")
  private val USER_PROFILE = Pair("USER_PROFILE", null)

  private lateinit var preferences: SharedPreferences
  private lateinit var moshi: Moshi
  private lateinit var profileAdapter: JsonAdapter<Profile>

  fun init (context: Context) {
    preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    moshi = Moshi.Builder().build()
    profileAdapter = moshi.adapter(Profile::class.java)
  }

  private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
    val editor = edit()
    operation(editor)
    editor.apply()
  }

  var accesToken: String
    get() = preferences.getString(ACCESS_TOKEN.first, ACCESS_TOKEN.second) ?: ACCESS_TOKEN.second
    set(value) = preferences.edit {
      it.putString(ACCESS_TOKEN.first, value)
    }

  var secretKey: String
    get() = preferences.getString(SECRET_KEY.first, SECRET_KEY.second) ?: SECRET_KEY.second
    set(value) = preferences.edit {
      it.putString(SECRET_KEY.first, value)
    }

  var userId: String
    get() = preferences.getString(USER_ID.first, USER_ID.second) ?: USER_ID.second
    set(value) = preferences.edit {
      it.putString(USER_ID.first, value)
    }

  var userProfile: Profile?
    get() {
      val profileStr = preferences.getString(USER_PROFILE.first, USER_PROFILE.second)
      profileStr?.let { return profileAdapter.fromJson(it) }
      return null
    }
    set(value) = preferences.edit {
      it.putString(USER_PROFILE.first, profileAdapter.toJson(value))
    }
}