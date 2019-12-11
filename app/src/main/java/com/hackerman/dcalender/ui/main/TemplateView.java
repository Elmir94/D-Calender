package com.hackerman.dcalender.ui.main;

import android.app.Activity;
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
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
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



    // save & cancel
    TextView saveTemplate;
    TextView cancel;

    //db & variables for db if-statements
    AppDatabase db;
    int subActivityId;
    String mainActivityName;
    String subActivityName;
    int subActivityColor;
    String subTaskText;
    DecimalFormat timeFormat;

    //FloatingActionButton back;



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_view);


        timeFormat = new DecimalFormat("####,00.00");

        Intent intent = getIntent();
        subActivityId = intent.getIntExtra(TemplateManager.SUB_ACTIVITY_ID, 0);

        //subActivityId=1;

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

        //back = findViewById(R.id.fab_add);

        saveTemplate = findViewById(R.id.saveBtn);
        cancel = findViewById(R.id.cancelBtn);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries() //Allows database to read & writ on main UI thread. This is a terrible idea DO NOT DO THIS!!!
                .build();

        selectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openColorPicker();
                openColorPicker2();
            }
        });

        mainActivity.setText(db.subActivityDao().getMainActivityNameById(subActivityId));
        subActivity.setText(db.subActivityDao().getSubActivityNameById(subActivityId));

        activityColor = db.subActivityDao().getSubActivityColorById(subActivityId);
        selectColor.setBackgroundTintList(ColorStateList.valueOf(activityColor));
        selectColor.setRippleColor(activityColor);

        timeFrom.setText(db.subActivityDao().getTimeFromStringById(subActivityId));
        timeTo.setText(db.subActivityDao().getTimeToStringById(subActivityId));

//        try{
//            float temp1 = Float.parseFloat(timeFormat.format(db.subActivityDao().getTimeFromById(subActivityId)));
            //timeFrom.setText(timeFrom);

//        }catch (Exception e){
//            timeFrom.setText(String.valueOf(db.subActivityDao().getTimeFromById(subActivityId)));
//        }

//        try{
//            float temp2 = Float.parseFloat(timeFormat.format(db.subActivityDao().getTimeToById(subActivityId)));
           // timeTo.setText(String.valueOf(temp2));

//        }catch (Exception e){
//            timeTo.setText(String.valueOf(db.subActivityDao().getTimeToById(subActivityId)));
//        }

        date.setText(db.subActivityDao().getDateById(subActivityId));

        taskName.setText(db.subActivityDao().getTaskNameById(subActivityId));
        taskText.setText(db.subActivityDao().getTaskTextById(subActivityId));

        saveTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String test = "07.00";
                float ftest = Float.parseFloat(test);
                float tempTimeFrom = Float.parseFloat(timeFrom.getText().toString());
                float tempTimeTo = Float.parseFloat(timeTo.getText().toString());
//                db.subActivityDao().update(new SubActivity(mainActivity.getText().toString(), subActivity.getText().toString(), activityColor,
//                        tempTimeFrom,tempTimeTo ,date.getText().toString(),taskName.getText().toString(),taskText.getText().toString()));
                //CreateNewTemplate view -> ViewTemplates view (saves to database)
                try{
                    db.mainActivityDao().insertAll(new com.hackerman.dcalender.database.entity.MainActivity(mainActivity.getText().toString()));
                }catch (Exception e){

                }

                db.subActivityDao().updateSubActivityNameOnId(subActivityId,subActivity.getText().toString());
                db.subActivityDao().updateMainActivityNameOnId(subActivityId,mainActivity.getText().toString());
                db.subActivityDao().updateActivityColorOnId(subActivityId, activityColor);
                db.subActivityDao().updateTimeFromOnId(subActivityId,tempTimeFrom);
                db.subActivityDao().updateTimeFromStringOnId(subActivityId,timeFrom.getText().toString());
                db.subActivityDao().updateTimeToOnId(subActivityId,tempTimeTo);
                db.subActivityDao().updateTimeToStringOnId(subActivityId,timeTo.getText().toString());
                db.subActivityDao().updateTaskNameOnId(subActivityId,taskName.getText().toString());
                db.subActivityDao().updateTaskTextOnId(subActivityId,taskText.getText().toString());
                startActivity(new Intent(TemplateView.this, TemplateManager.class));
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CreateNewTemplate view -> ViewTemplates view (without saving)
                startActivity(new Intent(TemplateView.this, TemplateManager.class));
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

}