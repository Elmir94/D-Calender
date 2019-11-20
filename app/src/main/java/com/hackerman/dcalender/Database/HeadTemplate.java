package com.hackerman.dcalender.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "HeadTemplate")
public class HeadTemplate
{
    @PrimaryKey
    private int id;

    @ColumnInfo (name = "TemplateName")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
