package com.hackerman.dcalender.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hackerman.dcalender.database.entity.MainActivity;
import com.hackerman.dcalender.database.entity.Template;

import java.util.List;

@Dao
public interface MainActivityDao {

    @Query("SELECT * FROM mainActivity")
        List<MainActivity> getAllmainActivities();

    @Insert
        void insertAll(MainActivity... mainActivities);

}
