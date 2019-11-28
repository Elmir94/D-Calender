package com.hackerman.dcalender.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Template")
public class Template{

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

//    if none primitive datatype ex objeckts
//    @Embedded(prifix = "dataType_")


    public void setId(int id) {this.id = id;}

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

