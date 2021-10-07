package com.example.strongeyetimeregister

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    companion object{
    var ISO_8601_FORMAT: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'")

        fun convertToISO(date: Date) : String {
            return ISO_8601_FORMAT.format(date)
        }

        fun convertToTime(date: String) : String{
            return toHoursFormat(date)
        }

        fun convertToTime(date: Date) : String{
            return toHoursFormat(convertToISO(date))
        }

        fun convertToCompleteDate(date: String){}

        fun toHoursFormat(string: String) : String{
            val dateTime = string.split("T").toTypedArray()
            val hours = dateTime[1].split(":").toTypedArray()

            var minString = hours[1].toInt()
            var hoursString = hours[0].toInt()

            hours[1] = (if (minString < 10) "0${minString}" else minString) as String
            hours[0] = (if (hoursString < 10) "0${hoursString}" else hoursString) as String

            return ("${hours[0]}:${hours[1]}")
        }
    }

}