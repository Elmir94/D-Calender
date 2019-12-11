package com.hackerman.dcalender;

import android.view.View;

public class Task {
    private final int schedule_hour = 180;

    public Integer id;
    public String headTemplateID;
    public String subTemplateID;
    public String name;
    public String date;
    public Float timeFrom;
    public Float timeTo;
    public String hexColor;

    public Task(){
        this.id = 0;
        this.headTemplateID = "None";
        this.subTemplateID = "None";
        this.name = "Empty";
        this.date = "None";
        this.timeFrom = 0f;
        this.timeTo = 0f;
        this.hexColor = "None";
    }

    public Task(String headTemplateID, String subTemplateID, String name, String date, Float timeFrom, Float timeTo, String hexColor) {
        this.id = View.generateViewId();
        this.headTemplateID = headTemplateID;
        this.subTemplateID = subTemplateID;
        this.name = name;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.hexColor = hexColor;
    }

    Task (String name, String date, Float timeFrom, Float timeTo, String hexColor) {
        this.id = View.generateViewId();
        this.headTemplateID = "None";
        this.subTemplateID = "None";
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
