package fr.isen.mayeul.androidtoolbox

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import fr.isen.mayeul.androidtoolbox.lifecycle.LifeCycleFragmentOne
import fr.isen.mayeul.androidtoolbox.lifecycle.LifeCycleFragmentTwo
import kotlinx.android.synthetic.main.activity_life_cycle.*
import maes.tech.intentanim.CustomIntent

// LifeCycle page
class LifeCycleActivity : FragmentActivity(), LifeCycleFragmentOne.OnFragmentInteractionListener,
    LifeCycleFragmentTwo.OnFragmentInteractionListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)

        // Notification
        notification("onCreate", true)

        // Pager adapter
        pagerLayout.adapter = ScreenSlidePagerAdapter(supportFragmentManager)

        // Fade transition
        CustomIntent.customType(this, "fadein-to-fadeout")
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

    // When the back button is pressed
    override fun onBackPressed() {
        // If we display the first fragment
        if (pagerLayout.currentItem == 0) {
            super.onBackPressed()
        } else {
            pagerLayout.currentItem--
        }
    }

    override fun onFragmentInteraction(uri: Uri) {}

    private inner class ScreenSlidePagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
        override fun getCount(): Int = NUM_FRAGMENT

        override fun getItem(position: Int): Fragment {
            if (position != 0)
                return LifeCycleFragmentTwo()
            return LifeCycleFragmentOne()
        }
    }

    companion object {
        private const val NUM_FRAGMENT: Int = 2
    }

}