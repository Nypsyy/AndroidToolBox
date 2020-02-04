package fr.isen.mayeul.androidtoolbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_home.*
import maes.tech.intentanim.CustomIntent


// Home page
class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Listeners
        // Disconnect button
        disconnectButton.setOnClickListener {
            signOut()
        }
        // Delete user button
        deleteButton.setOnClickListener {
            delete()
        }
        // Life cycle button
        lifeCycleButton.setOnClickListener {
            newIntent(this, LifeCycleActivity::class.java)
        }
        // Save activity
        saveButton.setOnClickListener {
            newIntent(this, SaveActivity::class.java)
        }
        // Permission activity
        permButton.setOnClickListener {
            newIntent(this, PermActivity::class.java)
        }
        // Web activity
        webButton.setOnClickListener {
            newIntent(this, WebActivity::class.java)
        }

        // Fade transition
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    // Sign out user
    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                newIntent(this, LoginActivity::class.java)
                finish()
                Toast.makeText(this, "Déconnexion !", Toast.LENGTH_LONG).show()
            }
    }

    // Delete user
    private fun delete() {
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
                if (it.isSuccessful) { // Deletion succeeded
                    newIntent(this, LoginActivity::class.java)
                    finish()
                    Toast.makeText(this, "Compte supprimé !", Toast.LENGTH_LONG).show()
                } else { // Deletion failed
                    Toast.makeText(this, "N'a pas pu supprimer le compte", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Create a new activity and launch it
    private fun newIntent(context: Context, c: Class<*>) {
        startActivity(Intent(context, c))
    }

    // Resume fade transition
    override fun onResume() {
        super.onResume()
        CustomIntent.customType(this, "fadein-to-fadeout")
    }
}
