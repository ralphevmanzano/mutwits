package com.ralphevmanzano.mutwits.util

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import com.ralphevmanzano.mutwits.data.models.User

class UserDiffCallBack : DiffUtil.ItemCallback<User>() {
  override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
    return oldItem.id == newItem.id
  }

  override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
    return oldItem == newItem
  }

  override fun getChangePayload(oldItem: User, newItem: User): Any? {
    val list = mutableListOf<Boolean>()

    val isSelectedEqual = oldItem.isSelected == newItem.isSelected
    if (isSelectedEqual.not()) {
      list.add(newItem.isSelected)
    }
    return list
  }
}