package fr.isen.mayeul.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.activity_life_cycle.*

class LifeCycleActivity : AppCompatActivity() {

    private val fm = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)
    }

    private fun notification(message: String, isActive: Boolean) {
        if (isActive)
            lifeCycleState.text = message
        else
            Log.d("INFO", message)
    }

    override fun onStart() {
        super.onStart()
        notification("onStart...", true)
    }

    override fun onResume() {
        super.onResume()
        notification("onResume...", true)
    }

    override fun onPause() {
        super.onPause()
        notification("onPause", false)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Activité détruite", Toast.LENGTH_LONG).show()
    }
}