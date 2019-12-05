package com.hackerman.dcalender.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hackerman.dcalender.database.Dao.MainActivityDao;
import com.hackerman.dcalender.database.Dao.SubActivityDao;
import com.hackerman.dcalender.database.entity.MainActivity;
import com.hackerman.dcalender.database.entity.SubActivity;


@Database(entities = {MainActivity.class,SubActivity.class}, version = 7)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MainActivityDao mainActivityDao();
    public abstract SubActivityDao subActivityDao();
}
