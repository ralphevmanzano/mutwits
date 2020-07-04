package com.ralphevmanzano.mutwits.ui.search.adapter

import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.databinding.QueryUserItemBinding
import com.ralphevmanzano.mutwits.ui.common.BaseAdapter
import com.ralphevmanzano.mutwits.ui.common.BaseViewHolder
import com.ralphevmanzano.mutwits.util.UserDiffCallBack
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class SearchAdapter @Inject constructor() :
    BaseAdapter<User, QueryUserItemBinding>(UserDiffCallBack()) {

    private var addToListListener: ((User, Int) -> Unit)? = null

    private val selectedUsers = mutableListOf<User>()

    fun setOnAddToListListener(listener: ((User, Int) -> Unit)?) {
        addToListListener = listener
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int) = getItem(position).id.toLong()

    override fun getItemViewType(position: Int) = R.layout.query_user_item

    override fun bind(binding: QueryUserItemBinding, position: Int) {
        // Handled on on bind with payload
    }

    override fun onViewHolderCreated(holder: BaseViewHolder<QueryUserItemBinding>) {
        holder.binding.btnAdd.setOnClickListener {
            handleOnClick(holder)
        }
    }

    private fun handleOnClick(holder: BaseViewHolder<QueryUserItemBinding>) {
        val selectedUser = getItem(holder.adapterPosition)

        selectedUser.isSelected = !selectedUser.isSelected
        addToListListener?.invoke(getItem(holder.adapterPosition), holder.adapterPosition)
        notifyItemChanged(holder.adapterPosition, selectedUser.isSelected)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<QueryUserItemBinding>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.binding.user = getItem(position)
        holder.binding.executePendingBindings()

        var isSelected = false

        if (payloads.isEmpty()) {
            isSelected = getItem(position).isSelected
            super.onBindViewHolder(holder, position, payloads)
        } else {
            isSelected = payloads[0] as Boolean
        }

        holder.binding.btnAdd.apply {
            text = if (isSelected) "Remove" else "Add"
            setTextColor(
                ContextCompat.getColor(
                    context,
                    if (isSelected) android.R.color.white else R.color.colorPrimary
                )
            )
            strokeColor = ContextCompat.getColorStateList(
                context,
                if (isSelected) R.color.red else R.color.colorPrimary
            )
            setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    if (isSelected) R.color.red else android.R.color.transparent
                )
            )
        }
    }
}