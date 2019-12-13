package com.ralphevmanzano.mutwits.util

import androidx.recyclerview.widget.DiffUtil
import com.ralphevmanzano.mutwits.data.models.User

class UserDiffCallBack : DiffUtil.ItemCallback<User>() {
  override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
    return oldItem.id_str == newItem.id_str
  }

  override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
    return oldItem == newItem
  }
}