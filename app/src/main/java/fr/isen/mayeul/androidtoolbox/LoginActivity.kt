package fr.isen.mayeul.androidtoolbox

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val id: String = "admin"
    private val pass: Short = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        confirmInput.setOnClickListener {
            val message: String = "Tu as entré" + idInput.text.toString()
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        }

        /*confirmInput.setOnClickListener {
            if (idInput.text.toString() == this.id && passInput.text.toString().toShort() == this.pass)
                Toast.makeText(this, "Authentication succeeded !", Toast.LENGTH_LONG).show()
        }*/
    }
}
