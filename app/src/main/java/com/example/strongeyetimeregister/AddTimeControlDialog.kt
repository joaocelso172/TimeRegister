package com.example.strongeyetimeregister

import android.app.DatePickerDialog
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
import com.example.strongeyetimeregister.dao.TimeControlDAO
import com.example.strongeyetimeregister.model.dto.TimeControlDTO
import com.google.android.material.textfield.TextInputEditText
import java.time.Duration
import java.util.*


class AddTimeControlDialog(context: Context, timeControlDTO: TimeControlDTO) : Dialog(context) {

    private val TAG = "AddTimeControlDialog"
    private var timeControlDTO = timeControlDTO
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

        editInitHour.setText(DateConverter.localDateTimeToHours(timeControlDTO.initialTime))
        editEndHour!!.setText(DateConverter.localDateTimeToHours(timeControlDTO.endTime))
        txtInitClock!!.text = "${DateConverter.localDateTimeToYears(timeControlDTO.initialTime)} às"
        txtEndClock!!.text = "${DateConverter.localDateTimeToYears(timeControlDTO.endTime)} às"
        txtHourClock.text = calculateInterval()

        editInitHour.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        //popTimePicker(editInitHour, true)
                        showDateTimePicker(true)
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        editEndHour.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN ->
                        showDateTimePicker(false)
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

    }

    private fun calculateInterval() : String{
        return DateConverter.durationToHours(Duration.between(timeControlDTO.initialTime, timeControlDTO.endTime))
    }

    private fun saveTimeControl(){
        timeControlDTO.desc = textInputEditTextDesc.text.toString()
        timeControlDAO.addTimeControl(timeControlDTO)
    }

    fun popTimePicker(editText: EditText, isInitDate: Boolean){
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            var date = DateConverter.dateToLocalDateTime(cal.time)
            if (isInitDate) timeControlDTO.initialTime = date else timeControlDTO.endTime = date
            editText.setText(DateConverter.localDateTimeToHours(date))
            txtHourClock.text = calculateInterval()
        }
        TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    fun popDatePicker(editText: EditText, isInitDate: Boolean){
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, month)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            var date = DateConverter.dateToLocalDateTime(cal.time)
            if (isInitDate) timeControlDTO.initialTime = date else timeControlDTO.endTime = date
            editText.setText(DateConverter.localDateTimeToHours(date))
            txtHourClock.text = calculateInterval()
        }
        DatePickerDialog(context, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
    }

    fun showDateTimePicker(isInitDate: Boolean) {
        val date: Calendar
        val currentDate = Calendar.getInstance()
        date = Calendar.getInstance()
        DatePickerDialog(context,
            { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(context,
                    { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        var newDate = DateConverter.dateToLocalDateTime(date.time)
                        if (isInitDate) {
                            timeControlDTO.initialTime = newDate
                            txtInitClock.text = "${DateConverter.localDateTimeToYears(timeControlDTO.initialTime)} às"
                            editInitHour.setText(DateConverter.localDateTimeToHours(newDate))
                        } else {
                            timeControlDTO.endTime = newDate
                            txtEndClock.text = "${DateConverter.localDateTimeToYears(timeControlDTO.endTime)} às"
                            editEndHour.setText(DateConverter.localDateTimeToHours(newDate))
                        }
                        txtHourClock.text = calculateInterval()
                    }, currentDate[Calendar.HOUR_OF_DAY], currentDate[Calendar.MINUTE], true
                ).show()
            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE]
        ).show()
    }

}