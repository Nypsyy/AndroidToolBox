package fr.isen.mayeul.androidtoolbox

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import fr.isen.mayeul.androidtoolbox.lifecycle.LifeCycleFragmentOne
import fr.isen.mayeul.androidtoolbox.lifecycle.LifeCycleFragmentTwo
import kotlinx.android.synthetic.main.activity_life_cycle.*
import maes.tech.intentanim.CustomIntent

// LifeCycle page
class LifeCycleActivity : AppCompatActivity(), LifeCycleFragmentOne.OnFragmentInteractionListener,
    LifeCycleFragmentTwo.OnFragmentInteractionListener {

    // For fragment swap
    private var isFragmentOneVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)

        // Notification
        notification("onCreate", true)

        // Set up first fragment display
        supportFragmentManager.beginTransaction().add(R.id.fragmentLayout, LifeCycleFragmentOne()).commit()
        // First fragment is displayed
        isFragmentOneVisible = true

        buttonFragment.setOnClickListener {
            changeFragmentDisplay()
        }

        // Fade transition
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    // Change fragment display
    private fun changeFragmentDisplay() {
        // Check which fragment is displayed
        if (isFragmentOneVisible) {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentLayout, LifeCycleFragmentTwo()).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentLayout, LifeCycleFragmentOne()).commit()
        }
        // Update boolean
        isFragmentOneVisible = !isFragmentOneVisible
    }

    // Set a notification message on the life cycle screen
    private fun notification(message: String, isActive: Boolean) {
        if (isActive)
            lifeCycleState.text = message
        else
            Log.d("INFO", message)
    }

    override fun onStart() {
        super.onStart()
        notification("LifeCycleActivity onStart...", true)
    }

    override fun onResume() {
        super.onResume()
        notification("LifeCycleActivity onResume...", true)
    }

    override fun onPause() {
        super.onPause()
        notification("LifeCycleActivity onPause...", false)
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Activité détruite", Toast.LENGTH_LONG).show()
    }

    override fun onFragmentInteraction(uri: Uri) {}
}