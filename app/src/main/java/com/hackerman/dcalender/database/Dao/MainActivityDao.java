package com.hackerman.dcalender.database.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.hackerman.dcalender.database.entity.MainActivity;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MainActivityDao {

    @Query("SELECT * FROM mainActivity")
    List<MainActivity> getAllmainActivities();

    @Query("Select mainActivityName From mainActivity")
    List<String> getAllMainActivityNames();

    @Insert
        void insertAll(MainActivity... mainActivities);
    @Delete
    void delete(MainActivity... mainActivities);

    @Query("Update mainActivity set mainActivityName = :newMainActivityName where mainActivityName = :oldMinActivityName")
    void update(String oldMinActivityName,String newMainActivityName);

    @Query("Update subActivity set mainActivityName = :newMainActivityName where mainActivityName = :oldMinActivityName")
    void updaterealtedSubs(String oldMinActivityName,String newMainActivityName);

//    @Query("Update mainActivity set mainActivityName = :newMainActivityName where id = :id")
//    int updateTour(int id, String newMainActivityName);

}
