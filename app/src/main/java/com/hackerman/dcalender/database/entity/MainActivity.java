package com.hackerman.dcalender.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "mainActivity")
public class MainActivity {


    @ColumnInfo(name = "mainActivityName")
    @PrimaryKey
    @NonNull
    private String mainActivityName;

    public MainActivity( String mainActivityName) {

        this.mainActivityName = mainActivityName;
    }

    public String getMainActivityName() {
        return mainActivityName;
    }

    public void setMainActivityName(String mainActivityName) {
        this.mainActivityName = mainActivityName;
    }

}
