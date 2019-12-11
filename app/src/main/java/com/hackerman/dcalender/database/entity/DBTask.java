package com.hackerman.dcalender.database.entity;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "dbtask")
public class DBTask {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "templateID")
    private int subTemplateId;

    @ColumnInfo(name = "taskName")
    private String taskName;

    @ColumnInfo(name = "taskColor")
    private String activityColor;

    @ColumnInfo(name = "date")
    private java.util.Date date;

    @ColumnInfo(name = "timeFrom")
    private Float timeFrom;

    @ColumnInfo(name = "timeTo")
    private Float timeTo;

    public DBTask(int subTemplateId, String taskName, String activityColor, Date date, Float timeFrom, Float timeTo) {
        this.subTemplateId = subTemplateId;
        this.taskName = taskName;
        this.activityColor = activityColor;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }
    public com.hackerman.dcalender.Task convertToScheduleTask() {

        return new com.hackerman.dcalender.Task(this.subTemplateId, this.taskName, this.date, this.timeFrom, this.timeTo, this.activityColor);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubTemplateId() {
        return subTemplateId;
    }

    public void setSubTemplateId(int subTemplateId) {
        this.subTemplateId = subTemplateId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getActivityColor() {
        return activityColor;
    }

    public void setActivityColor(String activityColor) {
        this.activityColor = activityColor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Float timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Float getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Float timeTo) {
        this.timeTo = timeTo;
    }
}