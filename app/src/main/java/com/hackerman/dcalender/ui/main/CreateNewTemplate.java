package com.hackerman.dcalender.ui.main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackerman.dcalender.R;
import com.hackerman.dcalender.Task;
import com.hackerman.dcalender.database.AppDatabase;
import com.hackerman.dcalender.database.entity.DBTask;
import com.hackerman.dcalender.database.entity.MainActivity;
import com.hackerman.dcalender.database.entity.SubActivity;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateNewTemplate extends AppCompatActivity {

    private static final String TAG = "CreateNewTemplate";
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_template);

        mainActivity = findViewById(R.id.mainActivityEditView);
        selectMainActivity = findViewById(R.id.mainActivitySpiner);

        subActivity = findViewById(R.id.subActivityEditView);
        selectSubActivity = findViewById(R.id.subActivitySpiner);
        //color
        activityColor = ContextCompat.getColor(this, R.color.activityBackground);
        selectColor = findViewById(R.id.selectColorFab);

        timeFrom = findViewById(R.id.timeFromEditText);
        timeTo = findViewById(R.id.timeToEditText);
        date = findViewById(R.id.dateEditText);

        //task name & text
        taskName =  findViewById(R.id.taskNameEditText);
        taskText = findViewById(R.id.taskText);

        saveTemplate = findViewById(R.id.saveBtn);
        cancel = findViewById(R.id.cancelBtn);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries() //Allows database to read & writ on main UI thread. This is a terrible idea DO NOT DO THIS!!!
                .build();

        mainActivityList = db.mainActivityDao().getAllMainActivityNames();
        mainActivityList.add(0, getResources().getString(R.string.selectActivity));

        ArrayAdapter<String> mainSpinnerAdapter = new ArrayAdapter<>(CreateNewTemplate.this, android.R.layout.simple_spinner_item, mainActivityList);
        mainSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectMainActivity.setAdapter(mainSpinnerAdapter);

        subActivityList = db.subActivityDao().getAllSubActivityNames();
        subActivityList.add(0, getResources().getString(R.string.selectActivity));

        ArrayAdapter<String> subSpinnerAdapter = new ArrayAdapter<>(CreateNewTemplate.this, android.R.layout.simple_spinner_item, subActivityList);
        subSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSubActivity.setAdapter(subSpinnerAdapter);

        selectMainActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spinnerItem = selectMainActivity.getSelectedItem().toString();
                if(spinnerItem.matches(getResources().getString(R.string.selectActivity))){
                    mainActivity.setText("");
                } else {
                    mainActivity.setText(spinnerItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mainActivity.setText("");
            }
        });

        selectSubActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spinnerItem = selectSubActivity.getSelectedItem().toString();
                if(spinnerItem.matches(getResources().getString(R.string.selectActivity))){
                    subActivity.setText("");
                } else {
                    subActivity.setText(spinnerItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        selectColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //openColorPicker();
                openColorPicker2();
            }
        });

        saveTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Saves EditText to strings for use in if-statement
                mainActivityName = mainActivity.getText().toString();
                subActivityName = subActivity.getText().toString();

                if(mainActivityName.matches("") || subActivityName.matches("") ) {
                    Toast.makeText(getApplicationContext(), "Enter Main Activity & Sub Activity name!", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        db.mainActivityDao().insertAll(new MainActivity(mainActivity.getText().toString()));
                    }
                    catch (Exception e) {
                        Log.d(TAG, "This main activity does already exist: " + mainActivity.getText().toString());
                    }

                    subActivityId = db.subActivityDao().getSpecificId(mainActivity.getText().toString(), subActivity.getText().toString());

                    if (subActivityId > 0) {
                        Toast.makeText(getApplicationContext(), "This activity already exists", Toast.LENGTH_LONG).show();
                    }
                    else {
                        float tempTimeFrom = Float.parseFloat(timeFrom.getText().toString());
                        float tempTimeTo = Float.parseFloat(timeTo.getText().toString());
                        String tempDate = date.getText().toString();

                        Date currentDate = Calendar.getInstance().getTime();
                        String pattern = getResources().getString(R.string.dataFormat);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                        String testdate = simpleDateFormat.format(currentDate);

                        try {
                            Date strToDate = new SimpleDateFormat(pattern).parse(tempDate);
                            Log.d(TAG, "Date!!!!_________ " + strToDate);
                            System.out.println("Auto cur date from android Calender: "+currentDate+"\tCalender date converted to string: "+tempDate+"\tString converted to date:"+strToDate);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        // String currentDate = DateFormat.getDateInstance().format(helo);

                        db.subActivityDao().insertAll(new SubActivity(mainActivity.getText().toString(), subActivity.getText().toString(), activityColor,
                                tempTimeFrom,timeFrom.getText().toString(),tempTimeTo,timeTo.getText().toString() ,tempDate,taskName.getText().toString(),taskText.getText().toString()));

//                        db.taskDao().insertAll(new DBTask(subActivityId, taskName.getText().toString(), taskText.getText().toString(), activityColor,
//                                tempTimeFrom,tempTimeTo,date.getText().toString()));
                        //CreateNewTemplate view -> ViewTemplates view (saves to database)
                        startActivity(new Intent(CreateNewTemplate.this, TemplateManager.class));
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CreateNewTemplate view -> ViewTemplates view (without saving)
                startActivity(new Intent(CreateNewTemplate.this, TemplateView.class));
            }
        });
    }

    //Color picker function for AmbilWarna color picker
    public void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, activityColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) { }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                activityColor = color;
                selectColor.setBackgroundTintList(ColorStateList.valueOf(activityColor));
                selectColor.setRippleColor(activityColor);
            }
        });
        colorPicker.show();
    }

    //material ui color picker
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





//package com.hackerman.dcalender.ui.main;
//
//import android.content.Intent;
//import android.content.res.ColorStateList;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import androidx.room.Room;
//
//import com.google.android.material.floatingactionbutton.FloatingActionButton;
//import com.hackerman.dcalender.R;
//import com.hackerman.dcalender.database.AppDatabase;
//import com.hackerman.dcalender.database.entity.MainActivity;
//import com.hackerman.dcalender.database.entity.SubActivity;
//import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
//import com.turkialkhateeb.materialcolorpicker.ColorListener;
//
//import java.text.DecimalFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import yuku.ambilwarna.AmbilWarnaDialog;
//
//public class CreateNewTemplate extends AppCompatActivity {
//
//    private static final String TAG = "CreateNewTemplate";
//    //mainActivity layout components
//    String mainActivityName;
//    String oldMainActivityName;
//    EditText mainActivity;
//    Spinner selectMainActivity;
//    List<String> mainActivityList;
//
//    //subActivity layout components
//    int subActivityId;
//    String subActivityName;
//    String oldSubActivityName;
//    EditText subActivity;
//    Spinner selectSubActivity;
//    List<String> subActivityList;
//
//    //color
//    int activityColor;
//    FloatingActionButton selectColor;
//
//    //time & date
//    float oldTimeFrom;
//    EditText timeFrom;
//
//    float oldTimeTo;
//    EditText timeTo;
//
//    String oldDate;
//    EditText date;
//
//    //task name & text
//    String oldTaskName;
//    EditText taskName;
//
//    String oldTaskText;
//    EditText taskText;
//
//    // save & cancel
//    TextView saveTemplate;
//    TextView cancel;
//
//    //db & variables for db if-statements
//    AppDatabase db;
//    ArrayAdapter<String> mainSpinnerAdapter;
//    ArrayAdapter<String> subSpinnerAdapter;
//    DecimalFormat timeFormat;
//
//    //int subActivityId;
//
//
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.create_new_template);
//        timeFormat = new DecimalFormat("0000,00.00");
//
//        Intent intent = getIntent();
//        subActivityId = intent.getIntExtra(TemplateManager.SUB_ACTIVITY_ID, 0);
//
//        mainActivity = findViewById(R.id.mainActivityEditView);
//        selectMainActivity = findViewById(R.id.mainActivitySpiner);
//
//        subActivity = findViewById(R.id.subActivityEditView);
//        selectSubActivity = findViewById(R.id.subActivitySpiner);
//        //color
//        activityColor = ContextCompat.getColor(this, R.color.activityBackground);
//        selectColor = findViewById(R.id.selectColorFab);
//
//        timeFrom = findViewById(R.id.timeFromEditText);
//        timeTo = findViewById(R.id.timeToEditText);
//        date = findViewById(R.id.dateEditText);
//
//        //task name & text
//        taskName = findViewById(R.id.taskNameEditText);
//        taskText = findViewById(R.id.taskText);
//
//        saveTemplate = findViewById(R.id.saveBtn);
//        cancel = findViewById(R.id.cancelBtn);
//
//        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
//                .allowMainThreadQueries() //Allows database to read & writ on main UI thread. This is a terrible idea DO NOT DO THIS!!!
//                .build();
//
//
//        if(subActivityId > 0)
//        {
//            populateFields(subActivityId);
//        }
//
//
//            mainActivityList = db.mainActivityDao().getAllMainActivityNames();
//            mainActivityList.add(0, getResources().getString(R.string.selectActivity));
//
//            mainSpinnerAdapter = new ArrayAdapter<>(CreateNewTemplate.this, android.R.layout.simple_spinner_item, mainActivityList);
//            mainSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            selectMainActivity.setAdapter(mainSpinnerAdapter);
//
//            subActivityList = db.subActivityDao().getAllSubActivityNames();
//            subActivityList.add(0, getResources().getString(R.string.selectActivity));
//
//            subSpinnerAdapter = new ArrayAdapter<>(CreateNewTemplate.this, android.R.layout.simple_spinner_item, subActivityList);
//            subSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            selectSubActivity.setAdapter(subSpinnerAdapter);
//
//            selectMainActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    String spinnerItem = selectMainActivity.getSelectedItem().toString();
//                    if (spinnerItem.matches(getResources().getString(R.string.selectActivity))) {
//                        mainActivity.setText("");
//                    } else {
//                        mainActivity.setText(spinnerItem);
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//                    mainActivity.setText("");
//                }
//            });
//
//            selectSubActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    String spinnerItem = selectSubActivity.getSelectedItem().toString();
//                    if (spinnerItem.matches(getResources().getString(R.string.selectActivity))) {
//                        subActivity.setText("");
//                    } else {
//                        subActivity.setText(spinnerItem);
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//
//
//            selectColor.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //openColorPicker();
//                    openColorPicker2();
//                }
//            });
//            System.out.println("------------------------------------SubActivityId: "+subActivityId+"---------------------------------------------------------");
//            saveTemplate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //Saves EditText to strings for use in if-statement
//                    mainActivityName = mainActivity.getText().toString();
//                    subActivityName = subActivity.getText().toString();
//
//                    if (mainActivityName.matches("") || subActivityName.matches("")) {
//                        Toast.makeText(getApplicationContext(), "Enter Main Activity & Sub Activity name!", Toast.LENGTH_LONG).show();
//                    } else {
//                        try {
//                            db.mainActivityDao().insertAll(new MainActivity(mainActivity.getText().toString()));
//                        } catch (Exception e) {
//                            Log.d(TAG, "This main activity does already exist: " + mainActivity.getText().toString());
//                        }
//                        //???
//                        //subActivityId = db.subActivityDao().getSpecificId(mainActivity.getText().toString(), subActivity.getText().toString());
//                        float tempTimeFrom = Float.parseFloat(timeFrom.getText().toString());
//                        timeFormat.format(tempTimeFrom);
//                        float tempTimeTo = Float.parseFloat(timeTo.getText().toString());
//                        timeFormat.format(tempTimeTo);
//                        if (subActivityId > 0) {
//                            Toast.makeText(getApplicationContext(), "This activity already exists", Toast.LENGTH_LONG).show();
//                            try {
////                                float tempTimeFrom = Float.parseFloat(timeFrom.getText().toString());
////                                timeFormat.format(tempTimeFrom);
////                                float tempTimeTo = Float.parseFloat(timeTo.getText().toString());
////                                timeFormat.format(tempTimeTo);
//                                db.mainActivityDao().insertAll(new MainActivity(mainActivity.getText().toString()));
//                                db.subActivityDao().update(new SubActivity(mainActivity.getText().toString(), subActivity.getText().toString(), activityColor,
//                                        tempTimeFrom, tempTimeTo, date.getText().toString(), taskName.getText().toString(), taskText.getText().toString()));
//                            } catch (Exception e) {
//                                db.subActivityDao().update(new SubActivity(mainActivity.getText().toString(), subActivity.getText().toString(), activityColor,
//                                        tempTimeFrom, tempTimeTo, date.getText().toString(), taskName.getText().toString(), taskText.getText().toString()));
//                                Log.d(TAG, "This main activity does already exist: " + mainActivity.getText().toString());
//                            }
//                        } else {
//
//                            String tempDate = date.getText().toString();
//
//                            Date currentDate = Calendar.getInstance().getTime();
//                            String pattern = getResources().getString(R.string.dataFormat);
//                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//                            String testdate = simpleDateFormat.format(currentDate);
//
//                            try {
//                                Date strToDate = new SimpleDateFormat(pattern).parse(tempDate);
//                                Log.d(TAG, "Date!!!!_________ " + strToDate);
//                                System.out.println("Auto cur date from android Calender: " + currentDate + "\tCalender date converted to string: " + tempDate + "\tString converted to date:" + strToDate);
//
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//
//                            // String currentDate = DateFormat.getDateInstance().format(helo);
//
//                            db.subActivityDao().insertAll(new SubActivity(mainActivity.getText().toString(), subActivity.getText().toString(), activityColor,
//                                    tempTimeFrom, tempTimeTo, date.getText().toString(), taskName.getText().toString(), taskText.getText().toString()));
//                            //CreateNewTemplate view -> ViewTemplates view (saves to database)
//                            startActivity(new Intent(CreateNewTemplate.this, TemplateManager.class));
//                        }
//                    }
//                }
//            });
////        }else {
////            populateFields(subActivityId);
////            saveTemplate.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    String tempMainActivity = mainActivity.getText().toString();
////
////                    float tempTimeFrom = Float.parseFloat(timeFrom.getText().toString());
////                    timeFormat.format(tempTimeFrom);
////
////                    float tempTimeTo = Float.parseFloat(timeTo.getText().toString());
////                    timeFormat.format(tempTimeTo);
////
////                    String tempDate = date.getText().toString();
////
////                    if(tempMainActivity.matches(mainActivityName)){
////                        db.subActivityDao().update(new SubActivity(tempMainActivity, subActivity.getText().toString(), activityColor,
////                                tempTimeFrom, tempTimeTo, tempDate, taskName.getText().toString(), taskText.getText().toString()));
////
////                        startActivity(new Intent(CreateNewTemplate.this, TemplateManager.class));
////                    }else{
////                        db.mainActivityDao().insertAll(new MainActivity(tempMainActivity));
////                        db.subActivityDao().update(new SubActivity(tempMainActivity, subActivity.getText().toString(), activityColor,
////                                tempTimeFrom, tempTimeTo, tempDate, taskName.getText().toString(), taskText.getText().toString()));
////                        startActivity(new Intent(CreateNewTemplate.this, TemplateManager.class));
////                    }
////                }
////            });
//////            float tempTimeFrom = Float.parseFloat(timeFrom.getText().toString());
//////            timeFormat.format(tempTimeFrom);
//////            float tempTimeTo = Float.parseFloat(timeTo.getText().toString());
//////            timeFormat.format(tempTimeTo);
//////            String tempDate = date.getText().toString();
//////
//////            db.subActivityDao().update(new SubActivity(mainActivity.getText().toString(), subActivity.getText().toString(), activityColor,
//////                    tempTimeFrom, tempTimeTo, tempDate, taskName.getText().toString(), taskText.getText().toString()));
////        }
//            cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //CreateNewTemplate view -> ViewTemplates view (without saving)
//                    startActivity(new Intent(CreateNewTemplate.this, TemplateManager.class));
//                }
//            });
//
//    }
//
//    private void populateFields(int subActivityId) {
//        saveTemplate.setText(R.string.saveUpdate);
//
//        mainActivity.setText(db.subActivityDao().getMainActivityNameById(subActivityId));
//        subActivity.setText(db.subActivityDao().getSubActivityNameById(subActivityId));
//
//        activityColor = db.subActivityDao().getSubActivityColorById(subActivityId);
//        selectColor.setBackgroundTintList(ColorStateList.valueOf(activityColor));
//        selectColor.setRippleColor(activityColor);
//
//        try{
//            float temp = Float.parseFloat(timeFormat.format(db.subActivityDao().getTimeFromById(subActivityId)));
//            timeFrom.setText(String.valueOf(temp));
//
//        }catch (Exception e){
//            timeFrom.setText(String.valueOf(db.subActivityDao().getTimeFromById(subActivityId)));
//        }
//
//        try{
//            float temp = Float.parseFloat(timeFormat.format(db.subActivityDao().getTimeToById(subActivityId)));
//            timeTo.setText(String.valueOf(temp));
//
//        }catch (Exception e){
//            timeTo.setText(String.valueOf(db.subActivityDao().getTimeToById(subActivityId)));
//        }
////        timeFrom.setText(String.valueOf(db.subActivityDao().getTimeFromById(subActivityId)));
////        timeTo.setText(String.valueOf(db.subActivityDao().getTimeToById(subActivityId)));
//
//        date.setText(db.subActivityDao().getDateById(subActivityId));
//
//        taskName.setText(db.subActivityDao().getTaskNameById(subActivityId));
//        taskText.setText((db.subActivityDao().getTaskTextById(subActivityId)));
//
////        oldMainActivityName = db.subActivityDao().getMainActivityNameById(subActivityId);
////        mainActivity.setText(oldMainActivityName);
////
////        oldSubActivityName = db.subActivityDao().getSubActivityNameById(subActivityId);
////        subActivity.setText(oldSubActivityName);
////
////        activityColor = db.subActivityDao().getSubActivityColorById(subActivityId);
////        selectColor.setBackgroundTintList(ColorStateList.valueOf(activityColor));
////        selectColor.setRippleColor(activityColor);
////
////        oldTimeFrom = db.subActivityDao().getTimeFromById(subActivityId);
////        timeFrom.setText(String.valueOf(oldTimeFrom));
////
////        oldTimeTo = db.subActivityDao().getTimeToById(subActivityId);
////        timeTo.setText(String.valueOf(oldTimeTo));
////
////        oldDate = db.subActivityDao().getDateById(subActivityId);
////        date.setText(oldDate);
////
////        oldTaskName = db.subActivityDao().getTaskNameById(subActivityId);
////        taskName.setText(oldTaskName);
////
////        oldTaskText = db.subActivityDao().getTaskTextById(subActivityId);
////        taskText.setText(oldTaskText);
//
////        System.out.println("-------------------------------------------\n MainActivity name: "+oldMainActivityName+
////                "\nSubActivitName: "+subActivityName+
////                "\nColor: "+activityColor+
////                "\ntimeFrom: "+oldTimeFrom+
////                "\ntimeTo: "+oldTimeTo+
////                "\ndate: "+oldDate+
////                "\nTaskName: "+oldTaskName+
////                "\nTaskText: "+oldTaskText);
//
//    }
//
//    //Color picker function for AmbilWarna color picker
//    public void openColorPicker() {
//        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, activityColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
//            @Override
//            public void onCancel(AmbilWarnaDialog dialog) { }
//
//            @Override
//            public void onOk(AmbilWarnaDialog dialog, int color) {
//                activityColor = color;
//                selectColor.setBackgroundTintList(ColorStateList.valueOf(activityColor));
//                selectColor.setRippleColor(activityColor);
//            }
//        });
//        colorPicker.show();
//    }
//
//    //material ui color picker
//    private void openColorPicker2() {
//        String title = "";
//        ColorChooserDialog dialog = new ColorChooserDialog(this);
//        dialog.setTitle(title);
//        dialog.setColorListener(new ColorListener() {
//            @Override
//            public void OnColorClick(View v, int color) {
//                activityColor = color;
//                selectColor.setBackgroundTintList(ColorStateList.valueOf(activityColor));
//                selectColor.setRippleColor(activityColor);
//            }
//        });
//        dialog.show();
//    }
//
//    // TODO: 05/12/2019 try this color picker: https://www.youtube.com/watch?v=MZ1GMHzAVPU
//
//    // TODO: 05/12/2019 try this color picker: https://www.youtube.com/watch?v=GlR7wqWEomU
//
//}
