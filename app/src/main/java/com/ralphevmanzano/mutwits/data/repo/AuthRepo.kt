package com.ralphevmanzano.mutwits.data.repo

import com.ralphevmanzano.mutwits.util.Prefs
import javax.inject.Inject

class AuthRepo @Inject constructor(val prefs: Prefs) {

  fun saveTokens(accessToken: String, secret: String) {
    prefs.save(Prefs.ACCESS_TOKEN, accessToken)
    prefs.save(Prefs.SECRET, secret)
  }

}