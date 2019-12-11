package com.hackerman.dcalender.database.Dao;

import com.hackerman.dcalender.database.entity.DBTask;

import java.util.Date;
import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface TaskDao {

    @Insert
    void insertAll(DBTask... dbTasks);

    @Query("SELECT taskName FROM dbtask")
    String getIdOnTimeFrom();

    @Query("SELECT * FROM dbtask")
    List<DBTask> getAllTasks();

    @Query("SELECT * FROM dbtask WHERE date = :date")
    List<DBTask> findTaskByDate(Date date);

}