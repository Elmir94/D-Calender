package com.hackerman.dcalender.Database;


import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface MyDao {
    @Insert
    public void addTemplate (HeadTemplate headTemplate);

}
