package com.hackerman.dcalender.database;

import com.hackerman.dcalender.database.Dao.MainActivityDao;
import com.hackerman.dcalender.database.Dao.SubActivityDao;
import com.hackerman.dcalender.database.Dao.TaskDao;
import com.hackerman.dcalender.database.entity.DBTask;
import com.hackerman.dcalender.database.entity.DateConverter;
import com.hackerman.dcalender.database.entity.MainActivity;
import com.hackerman.dcalender.database.entity.SubActivity;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@Database(entities = {MainActivity.class, SubActivity.class, DBTask.class}, version = 25)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MainActivityDao mainActivityDao();
    public abstract SubActivityDao subActivityDao();
    public abstract TaskDao taskDao();

//    static final Migration MIGRATION_7_8 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("CREATE TABLE `sainActivity` (`mainActivityName` STRING, PRIMARY KEY(`mainActivityName`))");
//            database.execSQL("CREATE TABLE `subActivity` (`id` INTEGER,`mainActivityName` STRING,`mainActivityName` STRING,`activityColor` INTEGER, PRIMARY KEY(`id`))");
//            database.execSQL("CREATE TABLE `sainActivity` (`mainActivityName` STRING, PRIMARY KEY(`mainActivityName`))");
//        }
//    };

}
