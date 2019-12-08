package com.ralphevmanzano.mutwits.util

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {

  companion object {
    private const val PREF_NAME = "mutwits_pref"

    const val ACCESS_TOKEN = "access_token"
    const val SECRET = "secret"
  }

  val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

  fun save(KEY_NAME: String, value: String) {
    val editor: SharedPreferences.Editor = sharedPref.edit()
    editor.putString(KEY_NAME, value)
    editor.apply()
  }

  fun getValue(KEY_NAME: String): String? {
    return sharedPref.getString(KEY_NAME, null)
  }


}