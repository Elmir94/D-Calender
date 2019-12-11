package com.hackerman.dcalender.database.Dao;

//test
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hackerman.dcalender.database.entity.SubActivity;

import java.util.List;

@Dao
public interface SubActivityDao {
    @Insert
    void insertAll(SubActivity... subActivity);

    @Update
    void update(SubActivity... subActivities);

    @Delete
    void delete(SubActivity... subActivities);


    @Query("SELECT * FROM subActivity")
    List<SubActivity> getAllsubActivities();

    @Query("Select subActivityName From subActivity")
    List<String> getAllSubActivityNames();


    @Query("SELECT * FROM subActivity WHERE mainActivityName = :mainActivityName")
    public List<SubActivity> GetAllRelatedSubActivities(String mainActivityName);

    @Query("SELECT id FROM subActivity WHERE mainActivityName = :mainActivityName and subActivityName = :subActivityName")
    public int getSpecificId(String mainActivityName, String subActivityName);

    @Query("SELECT * FROM subActivity WHERE date = :date")
    public List<SubActivity> getAllSubActivitiesOnDate(String date);

    @Query("SELECT * FROM subActivity WHERE timeFrom = :timeFrom")
    public SubActivity getSubActivityOnTimeFrom(Float timeFrom);

    @Query("SELECT * FROM subActivity WHERE id = :id")
    public SubActivity getSubActivityOnId(int id);


    @Query("DELETE FROM subActivity WHERE id = :id")
    public void deleteSubActivityById(int id);

    @Query("UPDATE subActivity SET timeFrom = :timeFrom, timeTo = :timeTo WHERE id = :id")
    public void updateFromAndToById(Float timeFrom, Float timeTo, int id);


    //test for TemplateView
    @Query("SELECT subActivityName FROM subActivity WHERE id = :id")
    public String getSubActivityNameById(int id);

    @Query("SELECT mainActivityName FROM subActivity WHERE id = :id")
    public String getMainActivityNameById(int id);

    @Query("SELECT activityColor FROM subActivity WHERE id = :id")
    public int getSubActivityColorById(int id);

    @Query("SELECT timeFrom FROM subActivity WHERE id = :id")
    public float getTimeFromById(int id);

    @Query("SELECT timeTo FROM subActivity WHERE id = :id")
    public float getTimeToById(int id);

    @Query("SELECT date FROM subActivity WHERE id = :id")
    public String getDateById(int id);

    @Query("SELECT taskName FROM subActivity WHERE id = :id")
    public String getTaskNameById(int id);

    @Query("SELECT taskText FROM subActivity WHERE id = :id")
    public String getTaskTextById(int id);

//    @Query("SELECT * FROM subActivity where mainActivityId = "1"" )
//    List<SubActivity> getSubActivityName();

//    @Query("SELECT * FROM repo WHERE userId=:userId")
//    List<Repo> findRepositoriesForUser(final int userId);

}
