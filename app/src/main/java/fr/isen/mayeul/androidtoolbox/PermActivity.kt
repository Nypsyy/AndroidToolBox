package fr.isen.mayeul.androidtoolbox

import android.app.Activity
import android.app.AlertDialog
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_perm.*

class PermActivity : AppCompatActivity(), LocationListener {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_LOAD_IMG = 2

    private val LOCATION_PERM = 10
    private val PICTURE_PERMISSIONS = 20

    private val picPermissions = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var isGpsLocation = false
    private var isNetworkLocation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perm)

        if (checkPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            initLocation()
        } else {
            requestPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERM)
        }

        imgSelect.setOnClickListener {
            if (checkPicturePermissions())
                onImgClick()
            else
                requestPicPermissions()
        }
    }

    private fun printLocation(location: Location) {
        if (location.latitude == 0.0000 && location.longitude == 0.0000) {
            initLocation()
        } else {
            latText.text = location.latitude.toString()
            longText.text = location.longitude.toString()
        }
    }

    private fun getLocation(lm: LocationManager) {
        try {
            if (isGpsLocation) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60 * 1, 10.0f, this)
                val loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (loc != null)
                    printLocation(loc)
            } else if (isNetworkLocation) {
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 60 * 1, 10.0f, this)
                val loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (loc != null)
                    printLocation(loc)
            } else {
                Toast.makeText(this, "Localisation échouée", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SecurityException) {
            Toast.makeText(this, "Localisation échouée", Toast.LENGTH_SHORT).show()
        }
    }

    private fun askLocation() {
        val dialog = AlertDialog.Builder(this)

        dialog.setTitle("Localisation pas activée")
        dialog.setMessage("Voulez-vous l'activer ?")
        dialog.setPositiveButton("Oui") { _, _ ->
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).also {
                startActivity(it)
            }
        }
        dialog.show()
    }

    private fun initLocation() {
        val locationManager: LocationManager = getSystemService(Service.LOCATION_SERVICE) as LocationManager
        isGpsLocation = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetworkLocation = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsLocation && !isNetworkLocation) {
            askLocation()
        } else {
            getLocation(locationManager)
        }
    }

    private fun onImgClick() {
        val dialog = AlertDialog.Builder(this)

        dialog.setTitle("Changer d'image")
        dialog.setPositiveButton("Prendre une photo") { _, _ -> takePhoto() }
        dialog.setNegativeButton("Récupérer dans la gallerie") { _, _ -> getGallery() }

        dialog.show()
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun getGallery() {
        Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).also { getImageIntent ->
            getImageIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(getImageIntent, REQUEST_LOAD_IMG)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imgSelect.setImageBitmap(imageBitmap)
        } else if (requestCode == REQUEST_LOAD_IMG && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImage = data.data
                try {
                    val bitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                    imgSelect.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    Toast.makeText(
                        this,
                        "N'a pas pu récupérer l'image de la gallerie",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun requestPermissions(perm: String, code: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(perm), code)
    }

    private fun requestPicPermissions() {
        ActivityCompat.requestPermissions(this, picPermissions, PICTURE_PERMISSIONS)
    }

    private fun checkPermissions(perm: String): Boolean {
        val result = ContextCompat.checkSelfPermission(this, perm)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun checkPicturePermissions(): Boolean {
        for (p in picPermissions) {
            if (checkPermissions(p))
                continue
            else
                return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERM -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initLocation()
            } else {
                Toast.makeText(this, "Permission non accordée", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLocationChanged(location: Location?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }
}
