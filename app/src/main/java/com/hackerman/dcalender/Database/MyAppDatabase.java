package com.hackerman.dcalender.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {HeadTemplate.class}, version = 1)
public abstract class MyAppDatabase extends RoomDatabase
{
    public abstract MyDao myDao();

}
