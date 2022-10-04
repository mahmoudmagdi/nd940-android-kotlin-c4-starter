package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityAuthenticationBinding
import com.udacity.project4.locationreminders.RemindersActivity

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */

// COMPLETED-TODO: Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google
// COMPLETED-TODO: If the user was authenticated, send him to RemindersActivity
// COMPLETED-TODO: a bonus is to customize the sign in flow to look nice using
// https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout

class AuthenticationActivity : AppCompatActivity() {

    private var _binding: ActivityAuthenticationBinding? = null
    val binding: ActivityAuthenticationBinding
        get() = _binding!!

    private lateinit var authenticationViewModel: AuthenticationViewModel

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        authenticationViewModel.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationViewModel = ViewModelProvider(this)[AuthenticationViewModel::class.java]
        authenticationViewModel.currentUser.observe(this) { currentUser ->
            if (currentUser != null) {
                continueToReminderActivity()
            }
        }

        with(binding) {
            login.setOnClickListener {
                startAuthentication()
            }
            register.setOnClickListener {
                startAuthentication()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun startAuthentication() {

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Customize the authentication layout
        val customLayout = AuthMethodPickerLayout.Builder(R.layout.custom_auth)
            .setEmailButtonId(R.id.login_with_mail)
            .setGoogleButtonId(R.id.login_with_google)
            .build()

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setAuthMethodPickerLayout(customLayout)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun continueToReminderActivity() {
        startActivity(Intent(this, RemindersActivity::class.java))
        finish()
    }
}
