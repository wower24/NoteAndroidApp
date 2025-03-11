package hoods.com.noteapplication.data.local.converters

import androidx.room.TypeConverter
import java.util.Date



class DateConverter {
    //convert Date to Long so database can understand it
    @TypeConverter
    fun toDate(date :Long) :Date? {
        return date?.let { Date(it) }
    }

    //convert Long to Date so app can understand it
    @TypeConverter
    fun fromDate(date :Date?) :Long? {
        return date?.time
    }

}