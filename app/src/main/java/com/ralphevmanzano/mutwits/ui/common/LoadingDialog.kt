package com.ralphevmanzano.mutwits.ui.common

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.ralphevmanzano.mutwits.R

class LoadingDialog: DialogFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    val view = inflater.inflate(R.layout.loading_dialog, container, false)
    if (dialog != null && dialog?.window != null) {
      dialog?.window?.apply {
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestFeature(Window.FEATURE_NO_TITLE)
      }
    }

    return view
  }

}