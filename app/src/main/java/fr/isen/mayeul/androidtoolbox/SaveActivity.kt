package fr.isen.mayeul.androidtoolbox

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_save.*
import maes.tech.intentanim.CustomIntent
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

// Save page
class SaveActivity : AppCompatActivity() {

    // Date picker
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    // JSON file
    private var jsonObject: JSONObject = JSONObject()
    // Calendar for date picker
    private var calendar = Calendar.getInstance()
    // Date picker format
    private val format = "dd/MM/yyyy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        // Date picker config
        initDatePicker()

        // Listeners
        // Save button
        saveJsonButton.setOnClickListener {
            onSaveClick()
        }
        // Display button
        displayButton.setOnClickListener {
            onDisplayClick()
        }
        // Date picker button
        datePicker.setOnClickListener {
            onDatePickerClick()
        }

        // Fade transition
        CustomIntent.customType(this, "fadein-to-fadeout")
    }

    // Save JSON file event
    private fun onSaveClick() {
        // User info
        val userInfo: Triple<String, String, String> = Triple(
            lastNameInput.text.toString(),
            firstNameInput.text.toString(),
            birthDateText.text.toString()
        )

        try {
            // Add in a JSON file all the info
            jsonObject.put("last_name", userInfo.first)
            jsonObject.put("first_name", userInfo.second)
            jsonObject.put("birth_date", userInfo.third)

            println(jsonObject)

            Toast.makeText(this, "Enregistré !", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Veuillez remplir les informations ci-dessus", Toast.LENGTH_SHORT).show()
        }
    }

    // Show button event
    private fun onDisplayClick() {
        try {
            val dialog = AlertDialog.Builder(this)

            // Dialog set up
            dialog.setTitle("Info utilisateur")
            dialog.setMessage(
                "Nom : " + jsonObject.getString("last_name")
                        + "\nPrénom : " + jsonObject.getString("first_name")
                        + "\nDate de naissance : " + jsonObject.getString("birth_date")
                        + "\nAge : " + jsonObject.getString("age")
            )
            // Dialog show
            dialog.create().show()
        } catch (e: Exception) {
            Toast.makeText(this, "Erreur : n'a pas pu lire le fichier utilisateur", Toast.LENGTH_LONG).show()
        }
    }

    // Date picker click event
    private fun onDatePickerClick() {
        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    // Calculates age based on birth date picked
    private fun getAge(year: Int, month: Int, day: Int) {
        var age: Int
        val today = Calendar.getInstance()
        val birth = Calendar.getInstance()

        birth[year, month] = day

        // Year calculation
        age = today[Calendar.YEAR] - birth[Calendar.YEAR]
        // If we've not passed his birth day
        if (today[Calendar.DAY_OF_YEAR] < birth[Calendar.DAY_OF_YEAR])
            age--

        // Add in JSON file
        jsonObject.put("age", age)
    }

    // Update the date text input
    private fun updateDateInView() {
        val simpleDateFormat = SimpleDateFormat(format, Locale.FRANCE)
        birthDateText.text = simpleDateFormat.format(calendar.time)
    }

    // Date picker init
    private fun initDatePicker() {
        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            birthDateText.text = "" // Birth date input reset
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
            // Age calculator
            getAge(year, month, dayOfMonth)
        }
    }
}
