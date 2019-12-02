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
    @ForeignKey(entity = MainActivity.class, parentColumns = "id" , childColumns = "mainActivityId", onDelete = CASCADE)
    private int mainActivityId;
    @ColumnInfo(name = "subActivityName")
    private String subActivityName;

    public SubActivity(int mainActivityId, String subActivityName) {
        this.mainActivityId = mainActivityId;
        this.subActivityName = subActivityName;
    }

    // TODO: 28/11/2019 add the rest of the relevent columns


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMainActivityId() {
        return mainActivityId;
    }

    public void setMainActivityId(int mainActivityId) {
        this.mainActivityId = mainActivityId;
    }

    public int getMainActivityName() {
        return mainActivityId;
    }

    public void setMainActivityName(int mainActivityId) {
        this.mainActivityId = mainActivityId;
    }

    public String getSubActivityName() {
        return subActivityName;
    }

    public void setSubActivityName(String subActivityName) {
        this.subActivityName = subActivityName;
    }
}
