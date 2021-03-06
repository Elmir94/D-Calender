package com.hackerman.dcalender.database.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

//@Entity(tableName = "subActivity")
@Entity(tableName = "subActivity")
public class SubActivity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ForeignKey(entity = com.hackerman.dcalender.database.entity.MainActivity.class, parentColumns = "mainActivityName" , childColumns = "mainActivityName", onDelete = CASCADE)
    private String mainActivityName;

    @ColumnInfo(name = "subActivityName")
    private String subActivityName;

    @ColumnInfo(name = "activityColor")
    private int activityColor;

    @ColumnInfo(name = "timeFrom")
    private float timeFrom;

    @ColumnInfo(name = "timeTo")
    private float timeTo;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "taskName")
    private String taskName;

    @ColumnInfo(name = "taskText")
    private String taskText;

    public SubActivity(String mainActivityName, String subActivityName, int activityColor, float timeFrom, float timeTo, String date, String taskName, String taskText) {
        this.mainActivityName = mainActivityName;
        this.subActivityName = subActivityName;
        this.activityColor = activityColor;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.date = date;
        this.taskName = taskName;
        this.taskText = taskText;
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

    public float getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(float timeFrom) {
        this.timeFrom = timeFrom;
    }

    public float getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(float timeTo) {
        this.timeTo = timeTo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }
}
