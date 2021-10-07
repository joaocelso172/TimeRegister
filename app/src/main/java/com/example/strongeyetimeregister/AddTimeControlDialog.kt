package com.example.strongeyetimeregister

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.strongeyetimeregister.DateConverter.Companion.convertToISO
import com.example.strongeyetimeregister.DateConverter.Companion.convertToTime
import com.example.strongeyetimeregister.dao.TimeControlDAO
import com.example.strongeyetimeregister.model.TimeControl
import com.example.strongeyetimeregister.model.dto.TimeControlDTO
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class AddTimeControlDialog(context: Context, timeControlDTO: TimeControlDTO) : Dialog(context) {

    private val timeControlDTO = timeControlDTO
    private lateinit var editInitHour: EditText
    private lateinit var editEndHour:EditText
    private lateinit var txtInitClock:TextView
    private lateinit var txtEndClock:TextView
    private lateinit var txtHourClock:TextView
    private lateinit var textInputEditTextDesc:TextInputEditText
    private val timeControlDAO: TimeControlDAO = TimeControlDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.add_time_control_dialog)
        setInfoLayout()
        setButton()

        calculateInterval()
    }

    private fun setButton() {
        val btnDismiss = findViewById<View>(R.id.btn_no) as Button
        btnDismiss.setOnClickListener { v: View? -> dismiss() }
        val btnConfirmRegister = findViewById<View>(R.id.btn_yes) as Button
        btnConfirmRegister.setOnClickListener { view: View? ->
            saveTimeControl()
            btnConfirmRegister.isEnabled = false
            btnDismiss.isEnabled = false
            dismiss()
        }
    }


    private fun setInfoLayout() {
        editInitHour = findViewById(R.id.edit_init_hour)
        editEndHour = findViewById(R.id.edit_end_hour)
        txtInitClock = findViewById(R.id.txt_init_time)
        txtEndClock = findViewById(R.id.txt_end_time)
        txtHourClock = findViewById(R.id.txt_hour_clock)
        textInputEditTextDesc = findViewById(R.id.edit_desc)

        editInitHour!!.setText(convertToTime(timeControlDTO.initHour))
        editEndHour!!.setText(timeControlDTO.endHour)
        txtInitClock!!.setText("Início: ${timeControlDTO.initDate} às")
        txtEndClock!!.setText("Fim: ${timeControlDTO.endDate} às")
        textInputEditTextDesc!!.setText("")

        editInitHour.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        popTimePicker(editInitHour)
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        editEndHour.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> popTimePicker(editEndHour)
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

    }

    private fun calculateInterval(){
        var initDate = splitString(editInitHour.text.toString())
        var endDate = splitString(editEndHour.text.toString())

        var finalDate: Int = endDate - initDate

        var finalMin: Int = if (finalDate % 60 == 0) 0 else (finalDate % 60)
        var finalHour: Int = (finalDate / 60)

        var finalMinString = if (finalMin < 10) "0${finalMin}" else finalMin
        var finalHourString = if (finalHour < 10) "0${finalHour}" else finalHour

        var stringHour = "$finalHourString:$finalMinString"

        Log.d("AddTimeControlDialog", stringHour + finalDate + finalMin + finalHour + initDate + endDate)
        txtHourClock.text = stringHour
    }

    private fun saveTimeControl(){

        var timeControl = TimeControl("${timeControlDTO.initDate} - ${editInitHour.text}", "${timeControlDTO.endDate} - ${editEndHour.text}", textInputEditTextDesc?.text.toString())

        timeControlDAO.addTimeControl(timeControl)
    }

    fun popTimePicker(editText: EditText){
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            editText.setText(convertToTime(cal.time))
            calculateInterval()
        }
        TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    fun splitString(string: String) : Int{
        val separated = string.split(":").toTypedArray()
        // this will contain "Fruit"

        separated[1] // this will contain " they taste good"
        var hoursToMin = (separated[0].toInt()*60)
        var totalMin = hoursToMin + separated[1].toInt()
        Log.d("AddTimeControlDialog", "$hoursToMin - $totalMin - $string - ${separated.size}")
        return totalMin
    }

}