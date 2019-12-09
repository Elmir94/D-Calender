package com.hackerman.dcalender;

import android.view.View;

import java.util.Date;

public class Task {
    private final int schedule_hour = 180;

    public Integer id;
    public Integer templateID;
    public String name;
    public Date date;
    public Float timeFrom;
    public Float timeTo;
    public String hexColor;

    public Task(Integer templateID, String name, Date date, Float timeFrom, Float timeTo, String hexColor) {
        this.id = View.generateViewId();
        this.templateID = templateID;
        this.name = name;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.hexColor = hexColor;
    }

    Task (String name, Date date, Float timeFrom, Float timeTo, String hexColor) {
        this.id = View.generateViewId();
        this.templateID = 0; //No template
        this.name = name;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.hexColor = hexColor;
    }

    int getHeight(){
        return (int)(timeTo - timeFrom)*schedule_hour;
    }
}
