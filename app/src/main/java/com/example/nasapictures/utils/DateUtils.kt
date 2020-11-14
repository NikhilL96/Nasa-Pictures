package com.example.nasapictures.utils

import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

object DateUtils {
    fun getDate(dateString: String?, format:String?): Date? {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return try {
            formatter.parse(dateString)
        } catch (ex: Exception) {
            null
        }
    }
}