package com.hackerman.dcalender.database.Dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hackerman.dcalender.database.entity.SubActivity;

import java.util.List;

@Dao
public interface SubActivityDao {
    @Insert
    void insertAll(SubActivity... subActivity);

    @Update
    void update(SubActivity... subActivities);

    @Delete
    void delete(SubActivity... subActivities);

    @Query("SELECT * FROM subActivity")
    List<SubActivity> getAllsubActivityis();


//    @Query("SELECT * FROM repo WHERE userId=:userId")
//    List<Repo> findRepositoriesForUser(final int userId);

}
