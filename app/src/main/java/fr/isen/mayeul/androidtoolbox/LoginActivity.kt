package fr.isen.mayeul.androidtoolbox

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val id: String = "admin"
    private val pass: Short = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        confirmButton.setOnClickListener {
            this.onConfirmClick()
        }
    }

    private fun onConfirmClick() {
        if (idInput.text.toString() == this.id && passInput.text.toString().toShort() == this.pass) {
            val homePage = Intent(this, HomeActivity::class.java)
            startActivity(homePage)
        }
    }
}
