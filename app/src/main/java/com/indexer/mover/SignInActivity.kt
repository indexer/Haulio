package com.indexer.mover

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_sign_in.google_sign_in

class SignInActivity : AppCompatActivity() {

  private lateinit var mGoogleSignInClient: GoogleSignInClient
  private val signInRequest = 10001

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_in)

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

    val account = GoogleSignIn.getLastSignedInAccount(this)
    if (account != null) {
      if (account.isExpired) {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
      }
    }

    google_sign_in.setOnClickListener {
      signIn()
    }

  }

  private fun signIn() {
    val signInIntent = mGoogleSignInClient.signInIntent
    startActivityForResult(signInIntent, signInRequest)
  }

  private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
    try {
      val account = completedTask.getResult(ApiException::class.java)
      if (account != null) {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
      }

    } catch (e: ApiException) {

    }

  }

  public override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent
  ) {
    super.onActivityResult(requestCode, resultCode, data)
    // Result returned from launching the Intent from GoogleSignInClient
    if (requestCode == signInRequest) {
      val task = GoogleSignIn.getSignedInAccountFromIntent(data)
      handleSignInResult(task)
    }
  }
}
