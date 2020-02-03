package fr.isen.mayeul.androidtoolbox

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import fr.isen.mayeul.androidtoolbox.randomuser.RandomUser
import fr.isen.mayeul.androidtoolbox.recyclerview.RandomUserAdapter
import fr.isen.mayeul.androidtoolbox.utils.PermissionManager
import kotlinx.android.synthetic.main.activity_random_user_item.*
import kotlinx.android.synthetic.main.activity_web.*
import maes.tech.intentanim.CustomIntent

// Web page
class WebActivity : AppCompatActivity() {

    // Permission manager
    private val permManager = PermissionManager(this)

    // Permissions required
    private val internetStoragePermissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        // Set up layout manager
        recyclerRandomUsers.layoutManager = LinearLayoutManager(this)
        // Random users feature
        loadRandomUsers()

        // Fade transition
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    // Loads random users
    private fun loadRandomUsers() {
        val gson = Gson()
        val queue = Volley.newRequestQueue(this)
        val url = getUrl(10, "fr")
        val randomUsers = arrayListOf<RandomUser>()

        // JSON Request
        val randomUserReq = JsonObjectRequest(Request.Method.GET, url, null, Response.Listener { r ->
            val results = r.getJSONArray("results")

            // Fills the list
            for (i in 0 until results.length()) {
                randomUsers.add(gson.fromJson(results[i].toString(), RandomUser::class.java))
            }

            // Update display
            recyclerRandomUsers.adapter = RandomUserAdapter(randomUsers)
        }, Response.ErrorListener { e ->
            println("ERROR : $e")
            Toast.makeText(this, "N'a pas pu récupérer les résultats", Toast.LENGTH_SHORT).show()
        })
        queue.add(randomUserReq)
    }

    // Initialize random users feature
    private fun initRandomUsers() {
        if (permManager.arePermissionsOk(internetStoragePermissions)) {
            loadRandomUsers()
        } else {
            permManager.requestMultiplePermissions(this, internetStoragePermissions, INTERNET_STORAGE_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // All permission cases
        when (requestCode) {
            // Internet connexion
            INTERNET_STORAGE_PERMISSIONS -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initRandomUsers()
            } else {
                Toast.makeText(this, "Connexion internet non autorisée", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private fun getUrl(nbUsers: Int, nationalities: String): String {
            return "$API_URL$INCLUDES&results=$nbUsers&nat=$nationalities"
        }

        // API url
        private const val API_URL = "https://randomuser.me/api/"
        private const val INCLUDES = "?inc=name,location,email,picture"

        // Internet permission cde
        private const val INTERNET_STORAGE_PERMISSIONS = 40
    }
}
