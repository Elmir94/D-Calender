package com.hackerman.dcalender.database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.hackerman.dcalender.database.entity.Template;

import java.util.List;

@Dao
public interface TemplateDao {
    @Query("SELECT * FROM Template")
    List<Template> getAlltemplates();

    @Insert
    void insertAll(Template... templates);
}
