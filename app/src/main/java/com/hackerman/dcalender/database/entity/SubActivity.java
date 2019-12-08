package com.hackerman.dcalender.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import static androidx.room.ForeignKey.CASCADE;

//@Entity(tableName = "subActivity")
@Entity(tableName = "subActivity", foreignKeys = @ForeignKey(entity = com.hackerman.dcalender.database.entity.MainActivity.class, parentColumns = "mainActivityName", childColumns = "mainActivityName", onDelete = CASCADE))
public class SubActivity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    //@ForeignKey(entity = MainActivity.class, parentColumns = "mainActivityName" , childColumns = "mainActivityName", onDelete = CASCADE)
    private String mainActivityName;

    @ColumnInfo(name = "subActivityName")
    private String subActivityName;

    @ColumnInfo(name = "activityColor")
    private int activityColor;

    public SubActivity(String mainActivityName, String subActivityName, int activityColor) {
        this.mainActivityName = mainActivityName;
        this.subActivityName = subActivityName;
        this.activityColor = activityColor;
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

    public int getActivityColor() {
        return activityColor;
    }

    public void setActivityColor(int activityColor) {
        this.activityColor = activityColor;
    }

    //public String getMainActivityName() {
    //    return mainActivityName;
    //}




}
