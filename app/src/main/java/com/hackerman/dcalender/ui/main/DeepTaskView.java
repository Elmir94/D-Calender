package com.hackerman.dcalender.ui.main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackerman.dcalender.MainActivity;
import com.hackerman.dcalender.R;
import com.hackerman.dcalender.database.AppDatabase;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

public class DeepTaskView extends AppCompatActivity {

    int activityColor;
    FloatingActionButton selectColor;
    AppDatabase db;
    Integer taskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_task_view);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries()
                .build();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            taskID = extras.getInt("taskID");
        }

        activityColor = db.subActivityDao().getSubActivityColorById(taskID);
        selectColor = findViewById(R.id.selectColorFab);
        selectColor.setBackgroundTintList(ColorStateList.valueOf(activityColor));
        selectColor.setRippleColor(activityColor);

        TextView header = findViewById(R.id.taskHeader);
        TextView taskTime = findViewById(R.id.taskTime);
        header.setText(db.subActivityDao().getDateById(taskID).substring(0,5));
        long fromHour = (long)(db.subActivityDao().getTimeFromById(taskID)-1);
        int fromMinutes = Math.round((((db.subActivityDao().getTimeFromById(taskID)-1) % 1) / 0.25f) * 15);
        long toHour = (long)(db.subActivityDao().getTimeToById(taskID)-1);
        int toMinutes = Math.round((((db.subActivityDao().getTimeToById(taskID)-1) % 1) / 0.25f) * 15);
        String time = (fromHour+":"+fromMinutes+ "  -  " +toHour+":"+toMinutes);
        taskTime.setText(time);

        TextView name = findViewById(R.id.taskName);
        name.setText(db.subActivityDao().getTaskNameById(taskID));
        TextView tasks = findViewById(R.id.taskTasks);
        tasks.setText(db.subActivityDao().getTaskTextById(taskID));

        selectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker2();
            }
        });
    }

    private void openColorPicker2() {
        String title = "";
        ColorChooserDialog dialog = new ColorChooserDialog(this);
        dialog.setTitle(title);
        dialog.setColorListener(new ColorListener() {
            @Override
            public void OnColorClick(View v, int color) {
                activityColor = color;
                selectColor.setBackgroundTintList(ColorStateList.valueOf(activityColor));
                selectColor.setRippleColor(activityColor);
            }
        });
        dialog.show();
    }

    public void goToSchedule(View view) {
        TextView name = findViewById(R.id.taskName);
        TextView tasks = findViewById(R.id.taskTasks);

        com.hackerman.dcalender.database.entity.SubActivity update = db.subActivityDao().getSubActivityOnId(taskID);
        update.setTaskName(name.getText().toString());
        update.setTaskText(tasks.getText().toString());
        Log.i("##Return##","activityColor: "+activityColor);
        update.setActivityColor(activityColor);
        db.subActivityDao().update(update);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
