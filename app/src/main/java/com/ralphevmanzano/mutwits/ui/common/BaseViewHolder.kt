package com.ralphevmanzano.mutwits.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder<T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root) {

  companion object {
    fun <T: ViewDataBinding> from(parent: ViewGroup, layoutId: Int) : BaseViewHolder<T> {
      val layoutInflater = LayoutInflater.from(parent.context)
      val binding: T = DataBindingUtil.inflate(layoutInflater, layoutId, parent, false)

      return BaseViewHolder(binding)
    }
  }
}