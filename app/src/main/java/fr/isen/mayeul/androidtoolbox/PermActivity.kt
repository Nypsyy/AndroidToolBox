package fr.isen.mayeul.androidtoolbox

import android.app.Activity
import android.app.AlertDialog
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_perm.*
import java.lang.Exception
import java.util.jar.Manifest

class PermActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_LOAD_IMG = 2
    private val PERMISSION_REQUEST_CAMERA = 10
    private val PERMISSION_REQUEST_EXT_STORAGE = 20
    private val PERMISSION_REQUEST_LOCATION = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perm)

        checkPermissions()

        val locationManager: LocationManager = getSystemService(Service.LOCATION_SERVICE) as LocationManager
        val isGpsLocation = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkLocation = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (!isGpsLocation && !isNetworkLocation) {
            askLocation()
        } else {
            getLocation()
        }

        imgSelect.setOnClickListener {
            onImgClick()
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

    private fun getLocation() {

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

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
            }
        }
        // GALLERY permission
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    PERMISSION_REQUEST_EXT_STORAGE
                )
            }
        }

        // LOCATION permission
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_LOCATION
                )
                getLocation()
            } else {
                Toast.makeText(this, "Permission non accordée", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
