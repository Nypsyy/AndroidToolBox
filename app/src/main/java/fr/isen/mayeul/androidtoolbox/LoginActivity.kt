package fr.isen.mayeul.androidtoolbox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val adminCredentials: Pair<String, Int> = Pair("admin", 123)
    private val prefName: String = "Shared Preferences"
    private val prefId: String = "Login ID"
    private val prefPass: String = "Login Password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val preferences: SharedPreferences = getSharedPreferences(prefName, Context.MODE_PRIVATE)

        val loginCache: Pair<String, Int> =
            Pair(preferences.getString(prefId, null), preferences.getInt(prefPass, 0))

        if (loginCache == adminCredentials) {
            redirectHome()
        }

        confirmButton.setOnClickListener {
            onConfirmClick(preferences)
        }
    }

    private fun onConfirmClick(pref: SharedPreferences) {
        val credentials: Pair<String, Int> =
            Pair(idInput.text.toString(), passInput.text.toString().toInt())

        if (credentials == adminCredentials) {
            val editor = pref.edit()

            editor.putString(prefId, credentials.first)
            editor.putInt(prefPass, credentials.second)
            editor.apply()

            redirectHome()
        }
    }

    private fun redirectHome() {
        newIntent(this, HomeActivity::class.java)
        finish()
    }

    private fun newIntent(context: Context, c: Class<*>) {
        startActivity(Intent(context, c))
    }
}
