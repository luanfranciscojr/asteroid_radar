package com.udacity.asteroidradar.common

import androidx.room.TypeConverter
import com.udacity.asteroidradar.Constants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun String.toDate(): Date = try {
    SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault()).parse(this)
} catch (e: ParseException) {
    Date()
}

fun Date.parserToString(): String = try {
    SimpleDateFormat(Constants.DATE_FORMAT, Locale.getDefault()).format(this)
} catch (e: ParseException) {
    ""
}

fun getLatterDate(amountDays: Int): Date = try {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    calendar.add(Calendar.DATE, amountDays)
    calendar.time
} catch (e: ParseException) {
    Date()
}

class Converters {
    @TypeConverter
    fun stringToDate(value: String): Date {
        return value.toDate()
    }

    @TypeConverter
    fun dateToString(date: Date): String {
        return date.parserToString()
    }
}