package com.hackerman.dcalender.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hackerman.dcalender.database.Dao.MainActivityDao;
import com.hackerman.dcalender.database.Dao.SubActivityDao;
import com.hackerman.dcalender.database.Dao.TemplateDao;
import com.hackerman.dcalender.database.entity.MainActivity;
import com.hackerman.dcalender.database.entity.SubActivity;
import com.hackerman.dcalender.database.entity.Template;


@Database(entities = {Template.class,MainActivity.class,SubActivity.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TemplateDao templateDao();
    public abstract MainActivityDao mainActivityDao();
    public abstract SubActivityDao subActivityDao();
}
