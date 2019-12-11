package com.ralphevmanzano.mutwits.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ralphevmanzano.mutwits.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    initToolbar()
  }

  private fun initToolbar() {
    setSupportActionBar(toolbar)

    val navController = findNavController(R.id.nav_host_fragment)
    val appBarConfiguration = AppBarConfiguration(navController.graph)
    toolbar?.setupWithNavController(navController, appBarConfiguration)
  }

  fun setupToolbar(show: Boolean) {
    supportActionBar?.apply {
      if (show) show()
      else hide()
    }
  }

}
