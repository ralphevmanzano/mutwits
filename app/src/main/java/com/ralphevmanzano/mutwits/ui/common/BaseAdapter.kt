package com.ralphevmanzano.mutwits.ui.common

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseAdapter<T, DB : ViewDataBinding> constructor(diffCallBack: DiffUtil.ItemCallback<T>) :
  ListAdapter<T, BaseViewHolder<DB>>(diffCallBack) {

  private var listener: (() -> Unit)? = null

  abstract override fun getItemViewType(position: Int): Int

  abstract fun bind(binding: DB, position: Int)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DB> {
    val holder: BaseViewHolder<DB> = BaseViewHolder.from(parent, viewType)
    onViewHolderCreated(holder)
    holder.binding.root.setOnClickListener {
      listener
    }
    return holder
  }

  override fun onBindViewHolder(holder: BaseViewHolder<DB>, position: Int) {
    bind(holder.binding, holder.adapterPosition)
  }

  open fun onViewHolderCreated(holder: BaseViewHolder<DB>) {}

  fun setOnItemClickListener(listener: () -> Unit) {
    this.listener = listener
  }
}