package com.hackerman.dcalender.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hackerman.dcalender.database.entity.SubActivity;
import com.hackerman.dcalender.database.entity.Task;

import java.sql.Date;
import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insertAll(Task... tasks);

    @Query("SELECT * FROM task")
    List<Task> getAllTasks();

    @Query("SELECT * FROM task WHERE date = :date")
    List<Task> findTaskByDate(Date date);

}
