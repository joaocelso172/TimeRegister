package com.example.strongeyetimeregister

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class DateConverter {
    companion object {

        var ISO_8601_FORMAT: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'")

        var locale: Locale = Locale("pt", "br")

        fun convertToISO(date: Date): String {
            return ISO_8601_FORMAT.format(date)
        }

        /*fun convertToTime(date: String) : String{
            return toHoursFormat(date)
        }

        fun convertToTime(date: Date) : String{
            return toHoursFormat(convertToISO(date))
        }*/

        @RequiresApi(Build.VERSION_CODES.O)
        fun localDateTimeToHours(date: LocalDateTime): String {
            return date.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        }


        @RequiresApi(Build.VERSION_CODES.O)
        fun localDateTimeToHours(date: String): LocalDateTime {
            var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            var dateTime: LocalDateTime = LocalDateTime.parse(date, formatter);
            return dateTime
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun localDateTimeToYears(date: LocalDateTime): String {
            return date.format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", locale))
        }

        fun convertToCompleteDate(date: String) {}

        @RequiresApi(Build.VERSION_CODES.O)
        fun durationToHours(duration: Duration): String {
            return String.format(
                "%02d:%02d:%02d",
                duration.toHours() % 24,
                duration.toMinutes() % 60,
                duration.seconds % 60
            )
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun stringToLocalDateTime(date: String) : LocalDateTime{
            return LocalDateTime.parse(date)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun completeDateToDisplayDate(date: LocalDateTime) : String{
            return "${localDateTimeToYears(date)} às ${localDateTimeToHours(date)}"
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun dateToLocalDateTime (date: Date) : LocalDateTime{
            return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        }
    }

}