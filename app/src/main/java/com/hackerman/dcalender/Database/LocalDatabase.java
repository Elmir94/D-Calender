package com.hackerman.dcalender.Database;

import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

// File: Head.java
@Entity
class User {
    @PrimaryKey
    private int id;
    private String name;
    @ColumnInfo(name = "release_year")
    private int releaseYear;
    // getters and setters are ignored for brevity but they are required for Room to work.
}
// File: HeadDao.java
@Dao
interface HeadDao {
    @Query("SELECT * FROM Head")
    List<Head> loadAll();
    @Query("SELECT * FROM Head WHERE id IN (:HeadIds)")
    List<Head> loadAllByHeadId(int... HeadIds);
    @Query("SELECT * FROM Head WHERE name LIKE :name AND release_year = :year LIMIT 1")
    Head loadOneByNameAndReleaseYear(String first, int year);
    @Insert
    void insertAll(Head... Heads);
    @Delete
    void delete(Head Head);
}