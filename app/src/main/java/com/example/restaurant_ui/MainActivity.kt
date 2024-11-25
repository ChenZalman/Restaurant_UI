package com.example.restaurant_ui


import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //All of the widgets and variables declaration
        val dateBtn: Button = findViewById(R.id.date_picker_button)
        val dateText: TextView = findViewById(R.id.date_text)
        val timeText: TextView = findViewById(R.id.time_text)
        val spinner: Spinner = findViewById(R.id.vegan_spinner)
        val veganText: TextView = findViewById(R.id.vegan_id)
        val reserveBtn: Button = findViewById(R.id.reserve_btn)
        val numDiners: EditText = findViewById(R.id.num_diners)
        var paymentMethod: String? = null
        val radioGroup: RadioGroup = findViewById(R.id.radio_group)
        val linearView: LinearLayout = findViewById(R.id.linearLayoutMain)
        val seekBarNumOfPeople: SeekBar = findViewById(R.id.seekBarNumOfDiners)
        val enlargeAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.enlarge)

        //Function calls to distribute the load from the main function
        linearView.startAnimation(enlargeAnim)
        spinnerListener(spinner, veganText)

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            paymentMethod = changePaymentOption(radioGroup = group)
        }

        dateBtn.setOnClickListener {
            dateAndTime(dateText, timeText, this)
        }
        reserveBtn.setOnClickListener {
//            linearView.startAnimation(fadeout)
            val alpha = ObjectAnimator.ofFloat(linearView, "alpha", 1f, 0f)
            alpha.duration = 2000
            alpha.start()
            dialogCreator(
                numDiners,
                dateText,
                timeText,
                veganText,
                paymentMethod
            )
        }

        seekBarNumOfPeople.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                numDiners.setText(seekBar?.progress.toString())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                numDiners.setText(seekBar?.progress.toString())
            }
        })

        // Create an ArrayAdapter using the string array and a default spinner layout
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
    }

    //This function sets the payment method to the paymentMethod String.
    fun changePaymentOption(radioGroup: RadioGroup): String? {
        var paymentMethod: String? = null
        if (radioGroup.findViewById<RadioButton?>(R.id.cash).isChecked)
            paymentMethod = radioGroup.findViewById<RadioButton?>(R.id.cash).text.toString()

        if (radioGroup.findViewById<RadioButton?>(R.id.credit_card).isChecked)
            paymentMethod = radioGroup.findViewById<RadioButton?>(R.id.credit_card).text.toString()

        if (radioGroup.findViewById<RadioButton?>(R.id.payment_app).isChecked)
            paymentMethod = radioGroup.findViewById<RadioButton?>(R.id.payment_app).text.toString()
        Toast.makeText(this, "Payment method changed to: $paymentMethod", Toast.LENGTH_SHORT).show()
        return paymentMethod
    }

    //This function creates a custom dialog to display multiple lines of text to create a summary of the reservation and display and option to return or finish the program (alternative to send a reservation)
    fun dialogCreator(
        numDiners: EditText,
        dateText: TextView,
        timeText: TextView,
        veganText: TextView,
        paymentMethod: String? = null
    ): Unit {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
        val checkBoxTos = dialogView.findViewById<CheckBox>(R.id.checkBoxTOS)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.setCancelable(false)
        dialogView.findViewById<TextView>(R.id.textViewNumberOfDiners).text =
            getString(R.string.number_of_diners) + ": ${numDiners.text}"
        dialogView.findViewById<TextView>(R.id.textViewPaymentMethod).text =
            getString(R.string.payment_method) + " ${paymentMethod}"
        dialogView.findViewById<TextView>(R.id.textViewDateOfReservation).text =
            getString(R.string.date_reserved_at, dateText.text, timeText.text)
        dialogView.findViewById<TextView>(R.id.textViewAnyVegans).text =
            getString(R.string.any_vegans) + " ${veganText.text}"
        dialogView.findViewById<Button>(R.id.buttonReserve).setOnClickListener {
            Toast.makeText(this, getString(R.string.reserve_sits), Toast.LENGTH_LONG).show()
            finish()
        }
        dialogView.findViewById<Button>(R.id.buttonEdit).setOnClickListener {
            Toast.makeText(this, getString(R.string.edit_and_return), Toast.LENGTH_SHORT).show()
            val alpha =
                ObjectAnimator.ofFloat(this.findViewById(R.id.linearLayoutMain), "alpha", 0f, 1f)
            alpha.duration = 2000
            alpha.start()
            dialog.dismiss()
        }
        dialog.show()

        //This listener waits for the checkbox to be checked so the user can press the reserve button
        checkBoxTos.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) dialogView.findViewById<Button>(R.id.buttonReserve).isEnabled = true
            else dialogView.findViewById<Button>(R.id.buttonReserve).isEnabled = false
        }
    }

    //This function set the selected option from the spinner (dropdown) menu to the text
    fun spinnerListener(
        spinner: Spinner,
        veganText: TextView
    ): Unit {

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                // An item is selected. You can retrieve the selected item using
                veganText.text = parent.getItemAtPosition(pos).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                veganText.text = getString(R.string.no_one_is_vegan)
            }
        }

    }

    //This function create the time dialog and then creates the date dialog and display the date dialog above the time dialog
    fun dateAndTime(
        dateText: TextView,
        timeText: TextView,
        context: Context
    ): Unit {
        val calendar = Calendar.getInstance()

        val timeListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            timeText.text = ("$hourOfDay:$minute")
        }
        val tpd = TimePickerDialog(context, timeListener, Calendar.HOUR, Calendar.MINUTE, true)
        tpd.show()

        val listener = DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            dateText.text = ("$i.$i2.$i3")
        }
        val dpd = DatePickerDialog(
            context, //this,
            listener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dpd.show()
    }
}



