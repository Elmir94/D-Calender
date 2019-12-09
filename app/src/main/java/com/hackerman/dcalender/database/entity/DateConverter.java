package com.hackerman.dcalender.database.entity;

import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {

    //converts from time to long
    @TypeConverter
    public static Long toTimestamp(java.util.Date date){
        return date == null ? null : date.getTime();
    }

    //converts from long to time
    @TypeConverter
    public static java.util.Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }
}
