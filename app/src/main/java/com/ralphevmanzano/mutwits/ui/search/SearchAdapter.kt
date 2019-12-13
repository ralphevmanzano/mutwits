package com.ralphevmanzano.mutwits.ui.search

import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.databinding.QueryUserItemBinding
import com.ralphevmanzano.mutwits.ui.common.BaseAdapter
import com.ralphevmanzano.mutwits.util.UserDiffCallBack

class SearchAdapter: BaseAdapter<User, QueryUserItemBinding>(UserDiffCallBack()) {

  override fun getItemViewType(position: Int) = R.layout.query_user_item

  override fun bind(binding: QueryUserItemBinding, position: Int) {
    binding.user = getItem(position)
    binding.executePendingBindings()
  }
}