package fr.isen.mayeul.androidtoolbox

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthMethodPickerLayout
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import maes.tech.intentanim.CustomIntent

// Login page
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // If already signed in
        if (FirebaseAuth.getInstance().currentUser != null) {
            Toast.makeText(this, "BIENVENUE", Toast.LENGTH_SHORT).show()
            redirectHome()
        }

        // When you click "login"
        welcomeImgInput.setOnClickListener {
            createSignInIntent()
        }

        // Fade transition
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    // Sign in process intent
    private fun createSignInIntent() {
        // Custom layout
        val customLayout = AuthMethodPickerLayout
            .Builder(R.layout.activity_firebase_login)
            .setGoogleButtonId(R.id.googleButton)
            .build()

        // Classic authentication intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(
                    listOf(
                        AuthUI.IdpConfig.GoogleBuilder().build() // Google authentication provider
                    )
                ).setAuthMethodPickerLayout(customLayout)
                .setTheme(R.style.AppTheme)
                .build(),
            RC_SIGN_INC
        )
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RC_SIGN_INC -> {
                val response = IdpResponse.fromResultIntent(data)

                // Success
                if (resultCode == Activity.RESULT_OK) {
                    FirebaseAuth.getInstance().currentUser
                    redirectHome()
                } else {
                    // User cancel
                    if (response == null) {
                        Toast.makeText(this, "Opération annulée", Toast.LENGTH_SHORT).show()
                        return
                    }

                    // Network error
                    if (response.error?.errorCode == ErrorCodes.NO_NETWORK) {
                        Toast.makeText(this, "Pas de connexion internet", Toast.LENGTH_SHORT).show()
                        return
                    }

                    // Other errors
                    Toast.makeText(this, "Erreur...", Toast.LENGTH_SHORT).show()
                    Log.e("ERR", "Erreur connexion", response.error)
                }
            }
        }
    }

    // New activity to the home page, FINISHES login activity
    private fun redirectHome() {
        newIntent(this, HomeActivity::class.java)
        finish()
    }

    // Start an activity
    private fun newIntent(context: Context, c: Class<*>) {
        startActivity(Intent(context, c))
    }

    companion object {
        // Sign in code
        private const val RC_SIGN_INC = 123
    }
}