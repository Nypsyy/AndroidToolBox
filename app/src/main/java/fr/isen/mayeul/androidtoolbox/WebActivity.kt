package fr.isen.mayeul.androidtoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import maes.tech.intentanim.CustomIntent

class WebActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        // Fade transition
        CustomIntent.customType(this, "fadein-to-fadeout")
    }
}
