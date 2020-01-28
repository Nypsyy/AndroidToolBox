package fr.isen.mayeul.androidtoolbox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import fr.isen.mayeul.androidtoolbox.lifecycle.LifeCycleActivity
import kotlinx.android.synthetic.main.activity_home.*
import maes.tech.intentanim.CustomIntent

// Home page
class HomeActivity : AppCompatActivity() {

    // Preference name
    private val prefName: String = "Shared Preferences"

    // Buttons on the page (array)
    private val pageButtons = arrayListOf<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        pageButtons.add(disconnectButton)
        pageButtons.add(lifeCycleButton)
        pageButtons.add(saveButton)
        pageButtons.add(disconnectButton)

        // Listeners
        for (button in pageButtons) {
            when (button) {
                disconnectButton -> button.setOnClickListener { onClickDisconnectButton() } // Login page
                lifeCycleButton -> button.setOnClickListener { onClickLifeCycleButton() } // Life cycle page
                saveButton -> button.setOnClickListener { onClickSaveButton() } // Save page
                permButton -> button.setOnClickListener { onClickPermButton() } // Permissions page
            }
        }

        // Fade transition
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    // Life cycle button event
    private fun onClickLifeCycleButton() {
        newIntent(this, LifeCycleActivity::class.java) // Life cycle activity
    }

    // Save button event
    private fun onClickSaveButton() {
        newIntent(this, SaveActivity::class.java) // Save activity
    }

    // Permissions button event
    private fun onClickPermButton() {
        newIntent(this, PermActivity::class.java) // Perm activity
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
}
