package com.hackerman.dcalender.ui.main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

        activityColor = ContextCompat.getColor(this, R.color.activityBackground);
        selectColor = findViewById(R.id.selectColorFab);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            taskID = extras.getInt("taskID");
        }

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries() //Allows database to read & writ on main UI thread. This is a terrible idea DO NOT DO THIS!!!
                .build();

        TextView header = findViewById(R.id.taskHeader);
        header.setText(db.subActivityDao().getDateById(taskID));

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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
