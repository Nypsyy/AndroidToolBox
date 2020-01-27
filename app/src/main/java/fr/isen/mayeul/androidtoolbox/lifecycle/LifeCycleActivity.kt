package fr.isen.mayeul.androidtoolbox.lifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import fr.isen.mayeul.androidtoolbox.R
import kotlinx.android.synthetic.main.activity_life_cycle.*
import maes.tech.intentanim.CustomIntent

class LifeCycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)

        notification("onCreate", true)

        val fragment1 =
            LifeCycleFragment1()
        val fragment2 =
            LifeCycleFragment2()
        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(R.id.fragmentLayout, fragment1).commit()

        buttonFragment.setOnClickListener {
            if (fragment1.isResumed) {
                Log.d("INFO", "Fragment 1 resumed")
                supportFragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment2)
                    .commit()
            } else {
                Log.d("INFO", "Fragment 2 resumed")
                supportFragmentManager.beginTransaction().replace(R.id.fragmentLayout, fragment1)
                    .commit()
            }
        }

        CustomIntent.customType(this, "fadein-to-fadeout")
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