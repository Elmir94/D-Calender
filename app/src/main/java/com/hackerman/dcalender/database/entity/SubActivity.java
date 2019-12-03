package com.hackerman.dcalender.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "subActivity")
public class SubActivity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ForeignKey(entity = MainActivity.class, parentColumns = "mainActivityName" , childColumns = "mainActivityName", onDelete = CASCADE)
    private String mainActivityName;

    @ColumnInfo(name = "subActivityName")
    private String subActivityName;

    public SubActivity(String mainActivityName, String subActivityName) {
        this.mainActivityName = mainActivityName;
        this.subActivityName = subActivityName;
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

    public String getSubActivityName() {
        return subActivityName;
    }

    public void setSubActivityName(String subActivityName) {
        this.subActivityName = subActivityName;
    }


    //public String getMainActivityName() {
    //    return mainActivityName;
    //}




}
