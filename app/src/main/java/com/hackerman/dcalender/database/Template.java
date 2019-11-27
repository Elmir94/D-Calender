package com.hackerman.dcalender.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public
class Template{

    public Template(String mainActivity, String subActivity){
        this.mainActivity = mainActivity;
        this.subActivity = subActivity;

    }
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "mainActivity")
    private String mainActivity;

    @ColumnInfo(name = "subActivity")
    private String subActivity;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(String mainActivity) {
        this.mainActivity = mainActivity;
    }

    public String getSubActivity() {
        return subActivity;
    }

    public void setSubActivity(String subActivity) {
        this.subActivity = subActivity;
    }
}
