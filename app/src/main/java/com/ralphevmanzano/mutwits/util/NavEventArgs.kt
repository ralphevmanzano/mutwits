package com.ralphevmanzano.mutwits.util

import android.os.Bundle

sealed class NavEventArgs {
  data class Destination(val actionId: Int, val bundle: Bundle?= null): NavEventArgs()
  object Pop: NavEventArgs()
}