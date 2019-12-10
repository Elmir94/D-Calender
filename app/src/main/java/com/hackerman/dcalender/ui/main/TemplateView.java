package com.hackerman.dcalender.ui.main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.room.Room;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackerman.dcalender.MainActivity;
import com.hackerman.dcalender.R;
import com.hackerman.dcalender.adapters.MyExpandableAdapter;
import com.hackerman.dcalender.database.AppDatabase;
import com.hackerman.dcalender.database.entity.SubActivity;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class TemplateView extends AppCompatActivity{

    private static final String TAG = "TemplateView";

    //mainActivity layout components
    EditText mainActivity;
    Spinner selectMainActivity;
    List<String> mainActivityList;

    //subActivity layout components
    EditText subActivity;
    Spinner selectSubActivity;
    List<String> subActivityList;

    //color
    int activityColor;
    FloatingActionButton selectColor;

    //time & date
    EditText timeFrom;
    EditText timeTo;
    EditText date;

    //task name & text
    EditText taskName;
    EditText taskText;

    //db & variables for db if-statements
    AppDatabase db;
    int subActivityId = 1;
    String mainActivityName;
    String subActivityName;
    int subActivityColor;
    String subTaskText;

    FloatingActionButton back;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_view);

        //mainActivity layout components
        mainActivity = findViewById(R.id.mainActivityEditView);
        selectMainActivity = findViewById(R.id.mainActivitySpiner);

        //subActivity layout components
        subActivity = findViewById(R.id.subActivityEditView);
        selectSubActivity = findViewById(R.id.subActivitySpiner);

        //color
        activityColor = ContextCompat.getColor(this, R.color.activityBackground);
        selectColor = findViewById(R.id.selectColorFab);

        //time & date
        timeFrom = findViewById(R.id.timeFromEditText);
        timeTo = findViewById(R.id.timeToEditText);
        date = findViewById(R.id.dateEditText);

        //task name & text
        taskName =  findViewById(R.id.taskNameEditText);
        taskText = findViewById(R.id.taskText);

        back = findViewById(R.id.fab_add);


        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries() //Allows database to read & writ on main UI thread. This is a terrible idea DO NOT DO THIS!!!
                .build();

        mainActivity.setText(db.subActivityDao().getMainActivityNameById(subActivityId));
        subActivity.setText(db.subActivityDao().getSubActivityNameById(subActivityId));

        activityColor = db.subActivityDao().getSubActivityColorById(subActivityId);
        selectColor.setBackgroundTintList(ColorStateList.valueOf(activityColor));
        selectColor.setRippleColor(activityColor);

        timeFrom.setText(String.valueOf(db.subActivityDao().getTimeFromById(subActivityId)));
        timeTo.setText(String.valueOf(db.subActivityDao().getTimeToById(subActivityId)));

        date.setText(db.subActivityDao().getDateById(subActivityId));

        //date.setText();-----------------------------------------------------------------------------------------------




        // Make sure user insert date into edittext in this format.
//        DateFormat formatter = new DateFormat(getString(R.string.dataFormat));
//
//        Date dateObject;
//        String date;
//        String time;
//
//        try {
//            String dob_var=(date.getText().toString());
//
//            dateObject = formatter.parse(dob_var);
//
//            date = new SimpleDateFormat("dd/MM/yyyy").format(dateObject);
//            time = new SimpleDateFormat("h:mmaa").format(dateObject);
//            Toast.makeText(getBaseContext(), date + time, Toast.LENGTH_LONG).show();
//        }
//        catch (java.text.ParseException e) {
//            e.printStackTrace();
//        }







        //------------------------------------------------------------------------------------

        taskName.setText(db.subActivityDao().getTaskNameById(subActivityId));
        taskText.setText(db.subActivityDao().getTaskTextById(subActivityId));


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CreateNewTemplate view -> ViewTemplates view (without saving)
                startActivity(new Intent(TemplateView.this, MainActivity.class));
            }
        });
    }

}