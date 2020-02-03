package fr.isen.mayeul.androidtoolbox

import android.Manifest
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
import android.provider.ContactsContract
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.mayeul.androidtoolbox.recyclerview.contact.ContactAdapter
import fr.isen.mayeul.androidtoolbox.utils.PermissionManager
import kotlinx.android.synthetic.main.activity_perm.*
import maes.tech.intentanim.CustomIntent

class PermActivity : AppCompatActivity(), LocationListener {

    // To use permission methods
    private val permManager = PermissionManager(this)

    // Permission codes for picture feature
    private val picturePermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    // Permission codes for Location and Contact read
    private val locContactPermissions = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    // Location providers
    private var isGpsLocation = false
    private var isNetworkLocation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perm)

        // Init
        init()

        // Listeners
        // Picture click
        imgSelect.setOnClickListener {
            onImgClick()
        }

        // Fade transition
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    // Init location and contacts by asking permissions
    private fun init() {
        if (permManager.arePermissionsOk(locContactPermissions)) {
            initLocation()
            displayContacts()
        } else {
            permManager.requestMultiplePermissions(this, locContactPermissions, LOC_CONTACT_PERMISSION)
        }
    }

    // Display the contact located in the phone
    private fun displayContacts() {
        val contacts = loadContacts() // Get the contacts

        // Initialize the recycler
        recyclerContact.apply {
            layoutManager = LinearLayoutManager(this@PermActivity)
            adapter = ContactAdapter(contacts)
        }
    }

    // Get the phone's contacts
    private fun loadContacts(): ArrayList<Contact> {
        val contactList = arrayListOf<Contact>()

        // Initialize cursor
        val phoneCursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)

        // Cursor iteration
        phoneCursor?.let { cursor ->
            while (cursor.moveToNext()) {
                // Contact ID
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                // Contact name
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                // Contact number(s)
                val phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)).toInt()

                // If there is(are) number(s)
                if (phoneNumber > 0) {
                    // New cursor on phone numbers
                    val numberCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                        arrayOf(id),
                        null
                    )

                    numberCursor?.let { numCursor ->
                        while (numCursor.moveToNext()) {
                            // Contact number
                            val phoneNumValue =
                                numCursor.getString(numCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            contactList.add(Contact(name, phoneNumValue))
                        }
                        numCursor.close()
                    }
                }
            }
            cursor.close()
        }
        return contactList
    }

    // Launch new activity (action = Gallery)
    private fun getGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { getImageIntent ->
            getImageIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(getImageIntent, REQUEST_LOAD_IMG)
            }
        }
    }

    // Launch new activity (action = CAMERA)
    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    // On picture button event
    private fun onImgClick() {
        // Check all picture permissions
        if (permManager.arePermissionsOk(picturePermissions)) {
            val dialog = AlertDialog.Builder(this)

            // New dialog ==> asks to take or get a photo
            dialog.setTitle("Changer d'image")
            dialog.setPositiveButton("Prendre une photo") { _, _ -> takePhoto() }
            dialog.setNegativeButton("Récupérer dans la gallerie") { _, _ -> getGallery() }

            dialog.show()
        } else {
            // Ask the permission
            permManager.requestMultiplePermissions(this, picturePermissions, PICTURE_PERMISSIONS)
        }
    }

    // Display the location on the page
    private fun printLocation(location: Location) {
        // If it couldn't get the coordinates, try again
        if (location.latitude == 0.0000 && location.longitude == 0.0000) {
            initLocation()
        } else {
            latText.text = ("Latitude : " + location.latitude.toString())
            longText.text = ("Longitude : " + location.longitude.toString())
        }
    }

    // Determine the location provider
    private fun getLocation(lm: LocationManager) {
        try {
            // If GPS is available
            if (isGpsLocation) {
                // Update location
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60 * 1, 10.0f, this)
                val loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (loc != null)
                    printLocation(loc)
            } // if NETWORK is available
            else if (isNetworkLocation) {
                // Update location
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

    // Ask to enable location settings
    private fun askLocationSettings() {
        val dialog = AlertDialog.Builder(this)

        dialog.setTitle("Localisation désactivée")
        dialog.setMessage("Voulez-vous l'activer ?")
        dialog.setPositiveButton("Oui") { _, _ ->
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).also {
                startActivity(it)
            }
        }
        dialog.show()
    }

    // Initialize location features
    private fun initLocation() {
        val locationManager: LocationManager = getSystemService(Service.LOCATION_SERVICE) as LocationManager

        // Determine the available providers
        isGpsLocation = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        isNetworkLocation = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        // If none
        if (!isGpsLocation && !isNetworkLocation) {
            askLocationSettings()
        } else {
            getLocation(locationManager)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Image change from camera
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imgSelect.setImageBitmap(imageBitmap)
        } // Image change from gallery
        else if (requestCode == REQUEST_LOAD_IMG && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val selectedImage = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                    imgSelect.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    Toast.makeText(this, "N'a pas pu récupérer l'image de la galerie", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // All permissions asked
        when (requestCode) {
            // Camera and file access
            PICTURE_PERMISSIONS -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onImgClick()
            } else {
                Toast.makeText(this, "Accès aux photos + caméra non accordée", Toast.LENGTH_LONG).show()
            }
            // Location and contacts
            LOC_CONTACT_PERMISSION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init()
            } else {
                Toast.makeText(this, "Localisation non accordée", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        // Request codes for image changes
        private const val REQUEST_IMAGE_CAPTURE = 11
        private const val REQUEST_LOAD_IMG = 22

        // Permission codes for features
        private const val LOC_CONTACT_PERMISSION = 10
        private const val PICTURE_PERMISSIONS = 20
    }

    override fun onLocationChanged(location: Location?) {}
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String?) {}
    override fun onProviderDisabled(provider: String?) {}
}
