package fr.isen.mayeul.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_life_cycle.*

class LifeCycleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)
    }

    override fun onStart() {
        super.onStart()
        lifeCycleState.text = "En cours..."
    }

    override fun onRestart() {
        super.onRestart()
        lifeCycleState.text = "En cours..."
    }

    override fun onResume() {
        super.onResume()
        lifeCycleState.text = "En cours..."
    }

    override fun onPause() {
        super.onPause()
        Log.d("INFO", "En arrière plan...")
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Activité détruite", Toast.LENGTH_LONG).show()
    }
}