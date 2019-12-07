package com.hackerman.dcalender.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Date;
import java.sql.Time;
import java.time.OffsetDateTime;

import static androidx.room.ForeignKey.CASCADE;

@Entity (tableName = "task")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ForeignKey(entity = SubActivity.class, parentColumns = "id" , childColumns = "subTemplateId", onDelete = CASCADE)
    private int subTemplateId;

    @ColumnInfo(name = "taskName")
    private String taskName;

    @ColumnInfo(name = "taskColor")
    private int activityColor;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "timeFrom")
    private Date timeFrom;

    @ColumnInfo(name = "timeTo")
    private Date timeTo;

    public Task(int subTemplateId, String taskName, int activityColor, Date date, Date timeFrom, Date timeTo) {
        this.subTemplateId = subTemplateId;
        this.taskName = taskName;
        this.activityColor = activityColor;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
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

    public int getActivityColor() {
        return activityColor;
    }

    public void setActivityColor(int activityColor) {
        this.activityColor = activityColor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Date timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Date getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Date timeTo) {
        this.timeTo = timeTo;
    }
}
