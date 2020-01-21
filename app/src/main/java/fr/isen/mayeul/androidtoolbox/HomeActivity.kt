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
    }

    private fun onClickLifeCycleButton() {
        val lifeCyclePage = Intent(this, LifeCycleActivity::class.java)
        startActivity(lifeCyclePage)
    }

    private fun onClickDisconnectButton() {
        val preferences: SharedPreferences = getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val editor = preferences.edit()

        editor.clear()
        editor.apply()

        val loginPage = Intent(this, LoginActivity::class.java)
        startActivity(loginPage)
        finish()
    }
}
