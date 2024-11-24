package com.example.restaurant_ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.res.Resources
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val dateBtn: Button = findViewById(R.id.date_picker_button)
        val dateText: TextView = findViewById(R.id.date_text)
        val timeText: TextView = findViewById(R.id.time_text)
        val spinner: Spinner = findViewById(R.id.vegan_spinner)
        val veganText: TextView = findViewById(R.id.vegan_id)
        val reserveBtn: Button = findViewById(R.id.reserve_btn)

        dateBtn.setOnClickListener {
            dateAndTime(dateText, timeText, this)
        }

        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.vegan_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }
        spinnerListener(spinner,veganText)
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
//                // An item is selected. You can retrieve the selected item using
//                veganText.text = parent.getItemAtPosition(pos).toString()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                veganText.text = getString(R.string.no_one_is_vegan)
//
//            }
//        }
    }
}

fun dateAndTime(dateText: TextView, timeText: TextView, context: Context): Unit {
    val calendar = Calendar.getInstance()
    val listener = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
        dateText.text = ("$i.$i2.$i3")
    }
    val dtd = DatePickerDialog(
        context, //this,
        listener,
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    dtd.show()


    val time = Calendar.getInstance().time
    val timeListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
        timeText.text = ("$hourOfDay:$minute")
    }
    val ttd = TimePickerDialog(context, timeListener, Calendar.HOUR, Calendar.MINUTE, true)
    ttd.show()
}

fun spinnerListener(spinner: Spinner, veganText: TextView): Unit{

    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
            // An item is selected. You can retrieve the selected item using
            veganText.text = parent.getItemAtPosition(pos).toString()
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            veganText.text = Resources.getSystem().getString(R.string.no_one_is_vegan)

        }
    }

}