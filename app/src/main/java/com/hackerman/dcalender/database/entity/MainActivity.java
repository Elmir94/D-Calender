package com.hackerman.dcalender.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mainActivity")
public class MainActivity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "mainActivityName")
    private String mainActivityName;

    public MainActivity( String mainActivityName) {

        this.mainActivityName = mainActivityName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMainActivityName() {
        return mainActivityName;
    }

    public void setMainActivityName(String mainActivityName) {
        this.mainActivityName = mainActivityName;
    }
}
