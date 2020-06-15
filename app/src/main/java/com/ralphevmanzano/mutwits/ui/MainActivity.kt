package com.ralphevmanzano.mutwits.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.ActivityMainBinding
import com.ralphevmanzano.mutwits.util.Prefs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject lateinit var firebaseAuth: FirebaseAuth

  lateinit var binding: ActivityMainBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

    initToolbar()
    initAuth()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId) {
      R.id.signOut -> firebaseAuth.signOut()
    }
    return true
  }

  private fun initAuth() {
    val user = firebaseAuth.currentUser
    if (user != null) {
      saveUserId(user)
      navigateTo(R.id.act_auth_to_home)
    }
    initAuthListener()
  }

  private fun saveUserId(user: FirebaseUser) {
    Prefs.userId = user.uid
  }

  private fun initAuthListener() {
    firebaseAuth.addAuthStateListener {
      val user = it.currentUser
      if (user == null) {
        if (!isCurrentFragment(R.id.authFragment)) navigateTo(R.id.act_home_to_auth)
      }
    }
  }

  private fun isCurrentFragment(@IdRes id: Int) = findNavController().currentDestination?.id == id

  private fun findNavController() = findNavController(R.id.nav_host_fragment)

  private fun initToolbar() {
    setSupportActionBar(binding.toolbar)
  }

  fun setupToolbar(
    title: String = "",
    show: Boolean = true,
    showBackBtn: Boolean = false,
    showSignOutBtn: Boolean = false
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
        setHomeButtonEnabled(showBackBtn)
        setDisplayHomeAsUpEnabled(showBackBtn)

        show()
      } else hide()
    }
  }

  override fun onSupportNavigateUp(): Boolean {
    findNavController().navigateUp()
    return super.onSupportNavigateUp()
  }

  fun navigateTo(@IdRes id: Int) {
    findNavController().navigate(id)
  }

}
