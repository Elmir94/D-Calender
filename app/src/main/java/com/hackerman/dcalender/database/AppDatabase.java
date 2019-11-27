package com.hackerman.dcalender.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Template.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TemplateDao templateDao();
}
