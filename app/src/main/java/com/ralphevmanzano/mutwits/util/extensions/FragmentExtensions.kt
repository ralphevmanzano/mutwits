package com.ralphevmanzano.mutwits.util.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.todo_app.util.Event
import com.example.todo_app.util.EventObserver

fun <T: Any, L: LiveData<T>> Fragment.observe(liveData: L, body: (T?) -> Unit) {
  liveData.observe(viewLifecycleOwner, Observer(body))
}

fun <T: Any, L: LiveData<Event<T>>> Fragment.observeEvent(liveData: L, body: (T?) -> Unit) {
  liveData.observe(viewLifecycleOwner, EventObserver(body))
}