package com.ralphevmanzano.mutwits.util.extensions

import android.view.View

fun View.setVisible(toVisibility: Int) {
	if (visibility == toVisibility) {
		return
	} else {
		visibility = toVisibility
	}
}

fun View.show() {
	if (visibility == View.VISIBLE) return else visibility = View.VISIBLE
}

fun View.hide() {
	if (visibility == View.GONE) return else visibility = View.GONE
}