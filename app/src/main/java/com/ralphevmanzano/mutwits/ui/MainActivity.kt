package com.ralphevmanzano.mutwits.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    initToolbar()
    initAuth()
  }

  private fun initAuth() {
    val user = FirebaseAuth.getInstance().currentUser
    if (user != null) {
      findNavController(R.id.nav_host_fragment).navigate(R.id.act_auth_to_home)
    }
  }

  private fun initToolbar() {
    setSupportActionBar(binding.toolbar)
  }

  fun setupToolbar(
    title: String = "",
    show: Boolean = true,
    showBackButton: Boolean = false
  ) {
    supportActionBar?.apply {
      if (show) {
        if (title.isNotEmpty()) {
          setDisplayShowTitleEnabled(true)
          setTitle(title)
        } else {
          setDisplayShowTitleEnabled(false)
        }

        setHomeAsUpIndicator(R.drawable.ic_arrow)
        setHomeButtonEnabled(showBackButton)
        setDisplayHomeAsUpEnabled(showBackButton)

        show()
      } else hide()
    }
  }

  override fun onSupportNavigateUp(): Boolean {
    findNavController(R.id.nav_host_fragment).navigateUp()
    return super.onSupportNavigateUp()
  }

}
