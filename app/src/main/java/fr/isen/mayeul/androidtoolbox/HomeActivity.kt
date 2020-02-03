package fr.isen.mayeul.androidtoolbox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*
import maes.tech.intentanim.CustomIntent

// Home page
class HomeActivity : AppCompatActivity() {

    // Preference name
    private val prefName: String = "Shared Preferences"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Listeners
        // Disconnect button
        disconnectButton.setOnClickListener {
            onClickDisconnectButton()
        }
        // Life cycle button
        lifeCycleButton.setOnClickListener {
            newIntent(this, LifeCycleActivity::class.java)
        }
        // Save activity
        saveButton.setOnClickListener {
            newIntent(this, SaveActivity::class.java)
        }
        // Permission activity
        permButton.setOnClickListener {
            newIntent(this, PermActivity::class.java)
        }
        // Web activity
        webButton.setOnClickListener {
            newIntent(this, WebActivity::class.java)
        }

        // Fade transition
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    // Logout button event
    private fun onClickDisconnectButton() {
        // Get the main preferences
        val preferences: SharedPreferences = getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val editor = preferences.edit()

        // Delete all the preferences
        editor.clear()
        editor.apply()

        // Disconnect
        newIntent(this, LoginActivity::class.java)
        finish()
    }

    // Create a new activity and launch it
    private fun newIntent(context: Context, c: Class<*>) {
        startActivity(Intent(context, c))
    }

    // Resume fade transition
    override fun onResume() {
        super.onResume()
        CustomIntent.customType(this, "fadein-to-fadeout")
    }
}
