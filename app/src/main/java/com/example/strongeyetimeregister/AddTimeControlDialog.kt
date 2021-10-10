package com.example.strongeyetimeregister

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.strongeyetimeregister.DateConverter.Companion.convertToISO
import com.example.strongeyetimeregister.dao.TimeControlDAO
import com.example.strongeyetimeregister.model.TimeControl
import com.example.strongeyetimeregister.model.dto.TimeControlDTO
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class AddTimeControlDialog(context: Context, timeControlDTO: TimeControlDTO) : Dialog(context) {

    private var timeControlDTO = timeControlDTO
    private lateinit var editInitHour: EditText
    private lateinit var editEndHour:EditText
    private lateinit var txtInitClock:TextView
    private lateinit var txtEndClock:TextView
    private lateinit var txtHourClock:TextView
    private lateinit var textInputEditTextDesc:TextInputEditText
    private val timeControlDAO: TimeControlDAO = TimeControlDAO()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.add_time_control_dialog)
        setInfoLayout()
        setButton()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setInfoLayout() {
        editInitHour = findViewById(R.id.edit_init_hour)
        editEndHour = findViewById(R.id.edit_end_hour)
        txtInitClock = findViewById(R.id.txt_init_time)
        txtEndClock = findViewById(R.id.txt_end_time)
        txtHourClock = findViewById(R.id.txt_hour_clock)
        textInputEditTextDesc = findViewById(R.id.edit_desc)

        editInitHour.setText(DateConverter.localDateTimeToHours(timeControlDTO.initialTime))
        editEndHour!!.setText(DateConverter.localDateTimeToHours(timeControlDTO.endTime))
        txtInitClock!!.text = "${DateConverter.localDateTimeToYears(timeControlDTO.initialTime)} às"
        txtEndClock!!.text = "${DateConverter.localDateTimeToYears(timeControlDTO.endTime)} às"
        txtHourClock.text = calculateInterval()

        editInitHour.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        popTimePicker(editInitHour, true)
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        editEndHour.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        popTimePicker(editEndHour, false)
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateInterval() : String{
        return DateConverter.durationToHours(Duration.between(timeControlDTO.initialTime, timeControlDTO.endTime))
    }

    private fun saveTimeControl(){
        timeControlDTO.desc = textInputEditTextDesc.text.toString()
        timeControlDAO.addTimeControl(timeControlDTO)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun popTimePicker(editText: EditText, isInitDate: Boolean){
        val cal = Calendar.getInstance()
        lateinit var date: LocalDateTime
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            date = DateConverter.dateToLocalDateTime(cal.time)
            if (isInitDate) timeControlDTO.initialTime = date else timeControlDTO.endTime = date
            editText.setText(DateConverter.localDateTimeToHours(date))
            txtHourClock.text = calculateInterval()
        }
        TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

}