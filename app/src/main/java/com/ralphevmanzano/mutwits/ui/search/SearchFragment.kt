package com.ralphevmanzano.mutwits.ui.search

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlin_starter_app.ui.BaseFragment

import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.SearchFragmentBinding

class SearchFragment : BaseFragment<SearchViewModel, SearchFragmentBinding>() {
  override val layoutRes: Int
    get() = R.layout.search_fragment

  override val viewModelClass: Class<SearchViewModel>
    get() = SearchViewModel::class.java

}
