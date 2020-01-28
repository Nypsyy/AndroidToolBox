package fr.isen.mayeul.androidtoolbox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import maes.tech.intentanim.CustomIntent

// Login page
class LoginActivity : AppCompatActivity() {

    // Credentials for logging in
    private val adminCredentials: Pair<String, Int> = Pair("admin", 123)
    // Preference names
    private val prefName: String = "Shared Preferences" // Main
    private val prefId: String = "Login ID" // ID
    private val prefPass: String = "Login Password" // Password

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Init main preferences
        val preferences: SharedPreferences = getSharedPreferences(prefName, Context.MODE_PRIVATE)

        // Get the cache
        val loginCache: Pair<String, Int> = Pair(preferences.getString(prefId, null), preferences.getInt(prefPass, 0))

        // If already logged in
        if (loginCache == adminCredentials) {
            redirectHome() // Go to home page
        }

        // When you click "login"
        confirmButton.setOnClickListener {
            onConfirmClick(preferences)
        }

        // Fade transition
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    // "Login" click even
    private fun onConfirmClick(pref: SharedPreferences) {
        // Get both id and password inputs
        val credentials: Pair<String, Int> = Pair(idInput.text.toString(), passInput.text.toString().toInt())

        // If credentials are OK
        if (credentials == adminCredentials) {
            val editor = pref.edit()

            // Save credentials
            editor.putString(prefId, credentials.first)
            editor.putInt(prefPass, credentials.second)
            editor.apply()

            redirectHome() // Go to home page
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
}
