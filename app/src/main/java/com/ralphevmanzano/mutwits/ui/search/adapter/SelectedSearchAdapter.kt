package com.ralphevmanzano.mutwits.ui.search.adapter

import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.databinding.SelectedSearchItemBinding
import com.ralphevmanzano.mutwits.ui.common.BaseAdapter
import com.ralphevmanzano.mutwits.util.UserDiffCallBack
import javax.inject.Inject

class SelectedSearchAdapter @Inject constructor(): BaseAdapter<User, SelectedSearchItemBinding>(UserDiffCallBack()) {

  override fun getItemViewType(position: Int) = R.layout.selected_search_item

  override fun bind(binding: SelectedSearchItemBinding, position: Int) {
    binding.user = getItem(position)
    binding.executePendingBindings()
  }
}