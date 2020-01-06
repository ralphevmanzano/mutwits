package com.ralphevmanzano.mutwits.ui.search.adapter

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.databinding.QueryUserItemBinding
import com.ralphevmanzano.mutwits.ui.common.BaseAdapter
import com.ralphevmanzano.mutwits.ui.common.BaseViewHolder
import com.ralphevmanzano.mutwits.ui.search.viewmodel.SearchViewModel
import com.ralphevmanzano.mutwits.util.UserDiffCallBack
import javax.inject.Inject

class SearchAdapter @Inject constructor(private val viewModel: SearchViewModel) :
  BaseAdapter<User, QueryUserItemBinding>(UserDiffCallBack()) {

  override fun getItemViewType(position: Int) = R.layout.query_user_item

  override fun bind(binding: QueryUserItemBinding, position: Int) {
    binding.user = getItem(position)
    binding.executePendingBindings()
  }

  override fun onViewHolderCreated(holder: BaseViewHolder<QueryUserItemBinding>) {
    holder.binding.btnAdd.setOnClickListener {
      viewModel.selectUser(getItem(holder.adapterPosition))
    }
  }

  override fun onBindViewHolder(
    holder: BaseViewHolder<QueryUserItemBinding>,
    position: Int,
    payloads: MutableList<Any>
  ) {
    if (payloads.isEmpty()) {
      super.onBindViewHolder(holder, position, payloads)
    } else {
      val isSelected: Boolean = payloads[0] as Boolean

      holder.binding.btnAdd.apply {
        text = if (isSelected) "Remove" else "Add"
        setTextColor(
          ContextCompat.getColor(
            this.context,
            if (isSelected) R.color.red else R.color.colorPrimary
          )
        )
        strokeColor = ContextCompat.getColorStateList(
          this.context,
          if (isSelected) R.color.red else R.color.colorPrimary
        )
      }
    }
  }

}