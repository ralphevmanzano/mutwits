package com.ralphevmanzano.mutwits.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  lateinit var viewModel: MainViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
    initToolbar()
  }

  private fun initToolbar() {
    setSupportActionBar(toolbar)
  }

  fun setupToolbar(
    title: String = "",
    show: Boolean,
    showBackButton: Boolean = false,
    isSearch: Boolean = false
  ) {
    supportActionBar?.apply {
      if (show) {
        if (title.isNotEmpty()) {
          setDisplayShowTitleEnabled(true)
          setTitle(title)
        } else {
          setDisplayShowTitleEnabled(false)
        }

        tilSearch.visibility = if (isSearch) View.VISIBLE else View.GONE

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
