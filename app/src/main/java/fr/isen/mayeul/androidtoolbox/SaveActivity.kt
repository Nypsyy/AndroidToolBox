package fr.isen.mayeul.androidtoolbox

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
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
        birthDateInput.text?.append(simpleDateFormat.format(calendar.time))
    }

    private fun onFormClick() {
        val userInfo: Triple<String, String, String> = Triple(
            lastNameInput.text.toString(),
            firstNameInput.text.toString(),
            birthDateInput.text.toString()
        )

        try {
            jsonObject.addProperty("last_name", userInfo.first)
            jsonObject.addProperty("first_name", userInfo.second)
            jsonObject.addProperty("birth_date", userInfo.third)
        } catch (e: Exception) {
        }
    }

    private fun onSeeClick() {
        try {
            val age = getAge()

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

    private fun getAge(): Int {
        var offset = -1
        val birthList = jsonObject.get("birth_date").asString.split("/")
        val todayList = LocalDate.now().toString().split("-")

        val monthDiff = todayList.elementAt(1).toInt() - birthList.elementAt(1).toInt()
        if (monthDiff == 0) {
            val dayDiff = todayList.elementAt(2).toInt() - birthList.elementAt(0).toInt()
            offset = if (dayDiff < 0) -1 else 0
        } else if (monthDiff > 0) {
            offset = 0
        }

        return todayList.elementAt(0).toInt() - birthList.elementAt(2).toInt() - offset
    }
}
