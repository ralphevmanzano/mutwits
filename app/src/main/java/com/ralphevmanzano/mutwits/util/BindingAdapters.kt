package com.ralphevmanzano.mutwits.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ralphevmanzano.mutwits.data.models.User
import com.ralphevmanzano.mutwits.ui.home.HomeAdapter
import com.ralphevmanzano.mutwits.ui.search.SearchAdapter

@BindingAdapter("setImage")
fun loadImage(iv: ImageView, imgUrl: String) {
  Glide.with(iv.context).load(imgUrl).apply(RequestOptions().circleCrop()).into(iv)
}

@BindingAdapter("setMutedUsers")
fun setMutedUsers(rv: RecyclerView, users: List<User>) {
  (rv.adapter as HomeAdapter).submitList(users)
}

@BindingAdapter("setQueryUsers")
fun setQueryUsers(rv: RecyclerView, users: List<User>) {
  (rv.adapter as SearchAdapter).submitList(users)
}