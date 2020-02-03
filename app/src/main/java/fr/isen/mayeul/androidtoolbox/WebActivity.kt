package fr.isen.mayeul.androidtoolbox

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.*
import fr.isen.mayeul.androidtoolbox.utils.PermissionManager
import kotlinx.android.synthetic.main.activity_web.*
import maes.tech.intentanim.CustomIntent

// Web page
class WebActivity : AppCompatActivity() {

    // Permission manager
    private val permManager = PermissionManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        loadRandomUsers()

        // Fade transition
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    private fun simpleVolleyRequest() {
        val queue = Volley.newRequestQueue(this)
        val url = getUrl(10, "fr")

//        val stringRequest = StringRequest(
//            Request.Method.GET, url, Response.Listener<String> { r ->
//                randomName.text = r.substring(0, 500)
//            },
//            Response.ErrorListener { randomName.text = "Fail" })
//
//        queue.add(stringRequest)
    }

    private fun loadRandomUsers() {
        if (permManager.isPermissionOk(Manifest.permission.INTERNET)) {
            simpleVolleyRequest()
        } else {
            permManager.requestAPermission(this, Manifest.permission.INTERNET, INTERNET_PERMISSION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // All permission cases
        when (requestCode) {
            // Internet connexion
            INTERNET_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadRandomUsers()
            } else {
                Toast.makeText(this, "Connexion internet non autorisée", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private fun getUrl(nbUsers: Int, nationalities: String): String {
            return "$API_URL?results=$nbUsers?nat=$nationalities"
        }

        // API url
        private const val API_URL = "https://randomuser.me/api/"

        // Internet permission cde
        private const val INTERNET_PERMISSION = 40
    }
}
