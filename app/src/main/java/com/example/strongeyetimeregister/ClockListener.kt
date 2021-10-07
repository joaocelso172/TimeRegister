package com.example.strongeyetimeregister

import android.content.Context
import android.os.Handler
import android.util.Log
import android.widget.TextView
import com.example.strongeyetimeregister.Constants.*
import com.example.strongeyetimeregister.dao.TimeControlDAO
import com.example.strongeyetimeregister.model.dto.TimeControlDTO
import java.text.SimpleDateFormat
import java.util.*

class ClockListener (c: Context) {

        private val c = c

        private val timeControlDAO: TimeControlDAO = TimeControlDAO()

        var timeControlDTO: TimeControlDTO? = null
        val timerHandler = Handler()
        lateinit var timerRunnable: Runnable

        fun showDateInfo(txtInitialTime: TextView?, txtActualTime: TextView?) {
            val hourFormatDate = SimpleDateFormat("HH:mm:ss")
            val weeklyFormatDate = SimpleDateFormat("EEEE")
            val monthlyFormatDate = SimpleDateFormat("dd/MM/yyyy")

            val initialTime = System.currentTimeMillis()

            val initialDate = Date(initialTime)

            val initialISOdate = DateConverter.convertToISO(initialDate)

            val monthlyInitialDate = monthlyFormatDate.format(initialDate)

            val initialTimeString = hourFormatDate.format(initialDate)

            if (txtInitialTime != null) txtInitialTime.text = initialTimeString

            timerRunnable = object : Runnable {
                override fun run() {

                    val actualTime = System.currentTimeMillis()
                    val actualDate = Date(actualTime)
                    val actualISOTime = DateConverter.convertToISO(actualDate)
                    val time = hourFormatDate.format(actualDate)
                    val weeklyDate = weeklyFormatDate.format(actualDate)
                    val monthlyDate = monthlyFormatDate.format(actualDate)
                    if (txtActualTime != null) txtActualTime.text = time

                    val sec = (actualDate.time - initialDate.time) / 1000
                    val min: Long = sec / 60

                    val intervalMin: Int = if (min % 60 == 0L) 0 else (min % 60).toInt()
                    val intervalHour: Int = (intervalMin / 60)

                    val intervalMinString = if (intervalMin < 10) "0${intervalMin}" else intervalMin
                    val intervalHourString = if (intervalHour < 10) "0${intervalHour}" else intervalHour

                    val intervalHourMin = "${intervalHourString}:${intervalMinString}"

                    Log.d("ClockListener", "Intervalo: ${intervalHourMin}")

                    timeControlDTO = TimeControlDTO(actualISOTime, initialISOdate)

                    timerHandler.postDelayed(this, 1000)

                }
            }
            timerHandler.postDelayed(timerRunnable, 0)


        }

        fun addTimeRegister(){
            val signInDialog = AddTimeControlDialog(c, timeControlDTO!!)
            signInDialog.show()
            timerHandler.removeCallbacks(timerRunnable)
        }

}