package com.hackerman.dcalender.database.entity;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.sql.Date;
import java.time.format.DateTimeFormatter;

public class DateConverter {

    //converts from time to long
    @TypeConverter
    public static Long toTimestamp(java.sql.Date date){
        return date == null ? null : date.getTime();
    }

    //converts from long to time
    @TypeConverter
    public static java.sql.Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
}
