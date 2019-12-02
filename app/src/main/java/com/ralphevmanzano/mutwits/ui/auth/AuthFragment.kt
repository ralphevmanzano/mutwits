package com.ralphevmanzano.mutwits.ui.auth

import android.util.Log
import com.example.kotlin_starter_app.ui.BaseFragment
import com.example.todo_app.util.EventObserver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.ralphevmanzano.mutwits.R
import com.ralphevmanzano.mutwits.databinding.AuthFragmentBinding
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and


class AuthFragment : BaseFragment<AuthViewModel, AuthFragmentBinding>() {

  private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

  override fun getViewModel(): Class<AuthViewModel> {
    return AuthViewModel::class.java
  }

  override fun getLayoutRes(): Int {
    return R.layout.auth_fragment
  }

  override fun observeNavigationEvents() {
    viewModel.loginEvent.observe(viewLifecycleOwner, EventObserver {
      checkPendingResult()
    })
  }

  private fun checkPendingResult() {
    val pendingResultTask = firebaseAuth.pendingAuthResult
    if (pendingResultTask != null) {
      pendingResultTask.addOnSuccessListener {
        Log.d("Main", it.toString())
      }.addOnFailureListener {
        Log.e("Main", it.localizedMessage!!)
      }
    } else {
      startSignInFlow()
    }
  }

  private fun startSignInFlow() {
    val provider = OAuthProvider.newBuilder("twitter.com")
    activity?.let { it ->
      firebaseAuth.startActivityForSignInWithProvider(it, provider.build())
        .addOnSuccessListener { authResult ->

          Log.d("Main", "Auth user: ${authResult.additionalUserInfo?.profile}")
          Log.d("Main", "Auth user: ${(authResult.credential as OAuthCredential).accessToken}")
          Log.d("Main", "Auth user: ${(authResult.credential as OAuthCredential).idToken}")
          Log.d("Main", "Auth user: ${(authResult.credential as OAuthCredential).secret}")
          Log.d(
            "Main",
            "Secret key: ${hmacSha1(
              (authResult.credential as OAuthCredential).accessToken,
              (authResult.credential as OAuthCredential).secret!!
            )}"
          )
        }.addOnFailureListener { e ->
          Log.e("Main", e.message!!)
        }
    }
  }

  @Throws(
    UnsupportedEncodingException::class,
    NoSuchAlgorithmException::class,
    InvalidKeyException::class
  )
  private fun hmacSha1(value: String, key: String): String? {
    val type = "HmacSHA1"
    val secret = SecretKeySpec(key.toByteArray(), type)
    val mac: Mac = Mac.getInstance(type)
    mac.init(secret)
    val bytes: ByteArray = mac.doFinal(value.toByteArray())
    return bytesToHex(bytes)
  }

  private val hexArray = "0123456789abcdef".toCharArray()

  private fun bytesToHex(bytes: ByteArray): String? {
    val hexChars = CharArray(bytes.size * 2)
    var v: Int
    for (j in bytes.indices) {
      v = (bytes[j] and 0xFF.toByte()).toInt()
      hexChars[j * 2] = hexArray[v ushr 4]
      hexChars[j * 2 + 1] = hexArray[v and 0x0F]
    }
    return String(hexChars)
  }
}
