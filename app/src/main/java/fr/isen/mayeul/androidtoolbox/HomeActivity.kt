package fr.isen.mayeul.androidtoolbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        lifeCycleButton.setOnClickListener {
            this.onClickLifeCycleButton()
        }
    }

    private fun onClickLifeCycleButton() {
        val lifeCycle = Intent(this, LifeCycleActivity::class.java)
        startActivity(lifeCycle)
    }
}
