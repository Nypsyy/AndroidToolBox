package fr.isen.mayeul.androidtoolbox

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.mayeul.androidtoolbox.lifecycle.LifeCycleActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val prefName: String = "Shared Preferences"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        lifeCycleButton.setOnClickListener {
            onClickLifeCycleButton()
        }

        disconnectButton.setOnClickListener {
            onClickDisconnectButton()
        }

        formButton.setOnClickListener {
            onClickSaveButton()
        }

        permButton.setOnClickListener {
            onClickPermButton()
        }
    }

    private fun onClickLifeCycleButton() {
        newIntent(this, LifeCycleActivity::class.java)
    }

    private fun onClickSaveButton() {
        newIntent(this, SaveActivity::class.java)
    }

    private fun onClickPermButton() {
        newIntent(this, PermActivity::class.java)
    }

    private fun onClickDisconnectButton() {
        val preferences: SharedPreferences = getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.clear()
        editor.apply()

        newIntent(this, LoginActivity::class.java)
        finish()
    }

    private fun newIntent(context: Context, c: Class<*>) {
        startActivity(Intent(context, c))
    }
}
