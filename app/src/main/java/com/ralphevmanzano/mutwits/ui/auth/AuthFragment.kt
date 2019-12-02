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
import java.net.URLEncoder
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
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

          var signatureBaseString: StringBuilder = StringBuilder("POST&https%3A%2F%2Fapi.twitter.com%2F1.1%2Fstatuses%2Fupdate.json&")
          var oauth_signature: String
          val parameterString: StringBuilder = StringBuilder()

          val oauth_consumer_key = "tsezcSYZilDxgmgNCKguTdpMS"
          val oauth_nonce = UUID.randomUUID().toString()
          val oauth_signature_method = "HMAC-SHA1"
          val oauth_timestamp = System.currentTimeMillis()/1000L
          val oauth_token = (authResult.credential as OAuthCredential).accessToken
          val oauth_version = "1.0"
          val consumer_sercret = "qJgHZK4Y1alzW9T1rp398TZIYhvHwpdqVVSBbvfgqEf9bZ1vLc"
          val oauth_secret = (authResult.credential as OAuthCredential).secret
          val secret = "$consumer_sercret$oauth_secret"

          val parameterMap = mutableMapOf(
            "include_entities" to true,
            "oauth_consumer_key" to "tsezcSYZilDxgmgNCKguTdpMS",
            "oauth_nonce" to UUID.randomUUID().toString(),
            "oauth_signature_method" to "HMAC-SHA1",
            "oauth_timestamp" to System.currentTimeMillis()/1000L,
            "oauth_token" to oauth_token,
            "oauth_version" to oauth_version
          )

          parameterMap["status"] = "Hello from Twitter API"

          for ((k, v) in parameterMap) {
            val encodedKey = percentEncode(k)
            val encodedValue = percentEncode(v.toString())

            parameterString.append(encodedKey)
            parameterString.append("=")
            parameterString.append(encodedValue)

            if (k != "status") {
              parameterString.append("&")
            }
          }

          signatureBaseString.append(percentEncode(parameterString.toString()))

          Log.d("Main", "Parameter String: $parameterString")
          Log.d("Main", "Signature Base String: $signatureBaseString")
          Log.d("Main", "Signature Secret Key: $secret")
          Log.d("Main", "OAuth signature ${hmacSha1(signatureBaseString.toString(), secret)}")
        }.addOnFailureListener { e ->
          Log.e("Main", e.message!!)
        }
    }
  }

  private fun percentEncode(input: String) : String {
    return URLEncoder.encode(input, "UTF-8").replace("%2B", "%20")
  }


  @Throws(
    UnsupportedEncodingException::class,
    NoSuchAlgorithmException::class,
    InvalidKeyException::class
  )
  private fun hmacSha1(data: String, key: String): String? {
    val keySpec = SecretKeySpec(key.toByteArray(), "HmacSHA1")
    val mac = Mac.getInstance("HmacSHA1")
    mac.init(keySpec)

    return mac.doFinal(data.toByteArray()).joinToString("")
  }
}
