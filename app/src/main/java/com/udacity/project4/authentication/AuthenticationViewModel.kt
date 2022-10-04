package com.udacity.project4.authentication

import android.app.Application
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthenticationViewModel(application: Application) : AndroidViewModel(application) {

    private val _currentUser = MutableLiveData<FirebaseUser>()
    val currentUser: LiveData<FirebaseUser>
        get() = _currentUser

    init {
        _currentUser.value = FirebaseAuth.getInstance().currentUser
    }

    fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {

            // Successfully signed in
            _currentUser.value = FirebaseAuth.getInstance().currentUser

        } else {
            result.idpResponse?.error?.message?.let {
                Toast.makeText(getApplication(), it, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}