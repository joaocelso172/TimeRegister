package com.example.strongeyetimeregister

import android.content.Context
import android.os.Build
import android.os.Handler
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.strongeyetimeregister.model.dto.TimeControlDTO
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ClockListener(c: Context) {

    private val c = c

    var timeControl: TimeControlDTO? = null
    val timerHandler = Handler()
    lateinit var timerRunnable: Runnable

    fun showDateInfo( txtInitialTime: TextView?, txtActualTime: TextView?,
                      txtDurationTime: TextView ) {

        val initialDate = LocalDateTime.now()

        txtInitialTime?.text = DateConverter.localDateTimeToHours(initialDate)

        timerRunnable = object : Runnable {
            override fun run() {

                val actualDate = LocalDateTime.now()

                txtActualTime?.text = DateConverter.localDateTimeToHours(actualDate)

                val duration = Duration.between(initialDate, actualDate)

                txtDurationTime?.text = DateConverter.durationToHours(duration)

                timeControl = TimeControlDTO(initialDate, actualDate)

                timerHandler.postDelayed(this, 1000)
            }
        }

        timerHandler.postDelayed(timerRunnable, 0)

    }

    fun addTimeRegister() {
        val signInDialog = AddTimeControlDialog(c, timeControl!!)
        signInDialog.show()
        timerHandler.removeCallbacks(timerRunnable)
    }

}