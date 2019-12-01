package com.ralphevmanzano.mutwits.ui.auth

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlin_starter_app.ui.BaseFragment

import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.AuthFragmentBinding

class AuthFragment : BaseFragment<AuthViewModel, AuthFragmentBinding>() {

  override fun getViewModel(): Class<AuthViewModel> {
    return AuthViewModel::class.java
  }

  override fun getLayoutRes(): Int {
    return R.layout.auth_fragment
  }


}
