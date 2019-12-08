package com.ralphevmanzano.mutwits.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.databinding.MutedUserItemBinding

class HomeAdapter : ListAdapter<User, ViewHolder>(UserDiffCallBack()) {

  class UserDiffCallBack : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
      return oldItem.id_str == newItem.id_str
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
      return oldItem == newItem
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    ViewHolder.from(parent)

  override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))
}

class ViewHolder private constructor(val binding: MutedUserItemBinding) :
  RecyclerView.ViewHolder(binding.root) {

  companion object {
    fun from(parent: ViewGroup): ViewHolder {
      val layoutInflater = LayoutInflater.from(parent.context)
      val binding = MutedUserItemBinding.inflate(layoutInflater, parent, false)

      return ViewHolder(binding)
    }
  }

  fun bind(user: User) {
    binding.user = user
    binding.executePendingBindings()
  }

}