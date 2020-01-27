package fr.isen.mayeul.androidtoolbox

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_save.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class SaveActivity : AppCompatActivity() {

    private var jsonObject: JsonObject = JsonObject()
    private var calendar = Calendar.getInstance()
    private val format = "dd/MM/yyyy"
    private var age = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save)

        formButton.setOnClickListener {
            onFormClick()
        }

        seeButton.setOnClickListener {
            onSeeClick()
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            birthDateText.text = ""
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
            age = getAge(year, month, dayOfMonth)
        }

        datePicker.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun updateDateInView() {
        val simpleDateFormat = SimpleDateFormat(format, Locale.FRANCE)
        birthDateText.text = simpleDateFormat.format(calendar.time)
    }

    private fun onFormClick() {
        val userInfo: Triple<String, String, String> = Triple(
            lastNameInput.text.toString(),
            firstNameInput.text.toString(),
            birthDateText.text.toString()
        )

        try {
            jsonObject.addProperty("last_name", userInfo.first)
            jsonObject.addProperty("first_name", userInfo.second)
            jsonObject.addProperty("birth_date", userInfo.third)

            Toast.makeText(this, "Enregistré !", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
        }
    }

    private fun onSeeClick() {
        try {
            val dialog = AlertDialog.Builder(this)

            dialog.setTitle("Info utilisateur")
            dialog.setMessage(
                "Nom : " + jsonObject.get("last_name").asString +
                        "\nPrénom : " + jsonObject.get("first_name").asString +
                        "\nDate de naissance : " + jsonObject.get("birth_date").asString +
                        "\nAge : $age"
            )
            dialog.create().show()
        } catch (e: Exception) {
        }
    }

    private fun getAge(year: Int, month: Int, day: Int): Int {
        val today = Calendar.getInstance()
        val birth = Calendar.getInstance()

        birth[year, month] = day

        var age = today[Calendar.YEAR] - birth[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < birth[Calendar.DAY_OF_YEAR])
            age--

        return age
    }
}
