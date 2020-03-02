package com.ralphevmanzano.mutwits.ui.search.adapter

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.databinding.QueryUserItemBinding
import com.ralphevmanzano.mutwits.ui.common.BaseAdapter
import com.ralphevmanzano.mutwits.ui.common.BaseViewHolder
import com.ralphevmanzano.mutwits.util.UserDiffCallBack
import javax.inject.Inject

class SearchAdapter @Inject constructor() :
  BaseAdapter<User, QueryUserItemBinding>(UserDiffCallBack()) {

  private var addToListListener: ((User, Int) -> Unit)? = null

  fun setOnAddToListListener(listener: ((User, Int) -> Unit)?) {
    addToListListener = listener
  }

  init {
    setHasStableIds(true)
  }

  override fun getItemId(position: Int) = getItem(position).id.toLong()

  override fun getItemViewType(position: Int) = R.layout.query_user_item

  override fun bind(binding: QueryUserItemBinding, position: Int) {
    binding.user = getItem(position)
    binding.executePendingBindings()
  }

  override fun onViewHolderCreated(holder: BaseViewHolder<QueryUserItemBinding>) {
    holder.binding.btnAdd.setOnClickListener {
      addToListListener?.invoke(getItem(holder.adapterPosition), holder.adapterPosition)
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