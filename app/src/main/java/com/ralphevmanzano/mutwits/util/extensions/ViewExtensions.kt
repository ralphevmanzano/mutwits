package com.ralphevmanzano.mutwits.util.extensions

import android.view.View

fun View.setVisible(toVisibility: Int) {
	if (visibility == toVisibility) {
		return
	} else {
		visibility = toVisibility
	}
}