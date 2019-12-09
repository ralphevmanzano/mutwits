package com.ralphevmanzano.mutwits.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.databinding.MutedUserItemBinding
import com.ralphevmanzano.mutwits.ui.BaseAdapter

class HomeAdapter : BaseAdapter<User, MutedUserItemBinding>(UserDiffCallBack()) {

  class UserDiffCallBack : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
      return oldItem.id_str == newItem.id_str
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
      return oldItem == newItem
    }
  }

  override fun bind(binding: MutedUserItemBinding, position: Int) {
    binding.user = getItem(position)
    binding.executePendingBindings()
  }

  override fun getItemViewType(position: Int): Int = R.layout.muted_user_item
}
