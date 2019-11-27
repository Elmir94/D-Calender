package com.hackerman.dcalender.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TemplateDao {
    @Query("SELECT * FROM template")
    List<Template> getAlltemplates();

    @Insert
    void insertAll(Template... templates);
}
