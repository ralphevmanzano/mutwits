package com.ralphevmanzano.mutwits.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.databinding.MutedUserItemBinding
import com.ralphevmanzano.mutwits.ui.common.BaseAdapter
import com.ralphevmanzano.mutwits.util.UserDiffCallBack

class HomeAdapter : BaseAdapter<User, MutedUserItemBinding>(UserDiffCallBack()) {

  override fun bind(binding: MutedUserItemBinding, position: Int) {
    binding.user = getItem(position)
    binding.executePendingBindings()
  }

  override fun getItemViewType(position: Int): Int = R.layout.muted_user_item
}
