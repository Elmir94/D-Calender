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
    List<SubActivity> getAllsubActivityis();

    @Query("Select subActivityName From subActivity")
    List<String> getAllSubActivityNames();


    @Query("SELECT * FROM subActivity WHERE mainActivityName = :mainActivityName")
    public List<SubActivity> GetAllRelatedSubActivities(String mainActivityName);

    @Query("SELECT id FROM subActivity WHERE mainActivityName = :mainActivityName and subActivityName = :subActivityName")
    public int getSpecificId(String mainActivityName, String subActivityName);


    //test for TemplateView
    @Query("SELECT subActivityName FROM subActivity WHERE id = :id")
    public String getSubActivityNameById(int id);

    @Query("SELECT mainActivityName FROM subActivity WHERE id = :id")
    public String getMainActivityNameById(int id);

    @Query("SELECT activityColor FROM subActivity WHERE id = :id")
    public int getSubActivityColorById(int id);

    @Query("SELECT timeFrom FROM subActivity WHERE id = :id")
    public float getTimeFromById(int id);

    @Query("SELECT timeFromString FROM subActivity WHERE id = :id")
    public String getTimeFromStringById(int id);

    @Query("SELECT timeTo FROM subActivity WHERE id = :id")
    public float getTimeToById(int id);

    @Query("SELECT timeToString FROM subActivity WHERE id = :id")
    public String getTimeToStringById(int id);

    @Query("SELECT date FROM subActivity WHERE id = :id")
    public String getDateById(int id);

    @Query("SELECT taskName FROM subActivity WHERE id = :id")
    public String getTaskNameById(int id);

    @Query("SELECT taskText FROM subActivity WHERE id = :id")
    public String getTaskTextById(int id);



    @Query("Update subActivity set subActivityName = :newSubActivityName where id = :subActivityId")
    void updateSubActivityNameOnId(int subActivityId,String newSubActivityName);

    @Query("Update subActivity set mainActivityName = :newMainActivityName where id = :subActivityId")
    void updateMainActivityNameOnId(int subActivityId ,String newMainActivityName);

    @Query("Update subActivity set activityColor = :newActivityColor where id = :subActivityId")
    void updateActivityColorOnId(int subActivityId ,int newActivityColor);

    @Query("Update subActivity set timeFrom = :newTimeFrom where id = :subActivityId")
    void updateTimeFromOnId(int subActivityId ,float newTimeFrom);

    @Query("Update subActivity set timeFromString = :newTimeFromString where id = :subActivityId")
    void updateTimeFromStringOnId(int subActivityId ,String newTimeFromString);

    @Query("Update subActivity set timeFrom = :newTimeTo where id = :subActivityId")
    void updateTimeToOnId(int subActivityId ,float newTimeTo);

    @Query("Update subActivity set timeToString = :newTimeToString where id = :subActivityId")
    void updateTimeToStringOnId(int subActivityId ,String newTimeToString);

    @Query("Update subActivity set date = :newDate where id = :subActivityId")
    void updateDateOnId(int subActivityId ,String newDate);

    @Query("Update subActivity set taskName = :newTaskName where id = :subActivityId")
    void updateTaskNameOnId(int subActivityId ,String newTaskName);

    @Query("Update subActivity set taskText = :newTaskText where id = :subActivityId")
    void updateTaskTextOnId(int subActivityId ,String newTaskText);

//    @Query("Update mainActivity, subActivity,  set mainActivityName = :newMainActivityName where id = :id")
//    void updaterealtedSubs(int id, String newMainActivityName, String subActivityName, int activityColor, float timeFrom, float timeTo, String date, String taskName, String taskText);

//    @Query("SELECT * FROM subActivity where mainActivityId = "1"" )
//    List<SubActivity> getSubActivityName();

//    @Query("SELECT * FROM repo WHERE userId=:userId")
//    List<Repo> findRepositoriesForUser(final int userId);

}
