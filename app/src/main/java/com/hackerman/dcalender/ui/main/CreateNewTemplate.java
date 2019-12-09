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
import com.hackerman.dcalender.database.AppDatabase;
import com.hackerman.dcalender.database.entity.MainActivity;
import com.hackerman.dcalender.database.entity.SubActivity;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

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

    //create task
    FloatingActionButton createTask;
    TextView saveTemplate;
    TextView cancel;

    //taskText

    EditText taskText;

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

        createTask = findViewById(R.id.createTaskFab);

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

        createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getThing
                Toast.makeText(getApplicationContext(), "thingi will com never", Toast.LENGTH_LONG).show();
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
                        db.subActivityDao().insertAll(new SubActivity(mainActivity.getText().toString(), subActivity.getText().toString(), activityColor, taskText.getText().toString() ));
                        //CreateNewTemplate view -> ViewTemplates view (saves to database)
                        startActivity(new Intent(CreateNewTemplate.this, TemplateView.class));
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

    // TODO: 05/12/2019 try this color picker: https://www.youtube.com/watch?v=MZ1GMHzAVPU  

    // TODO: 05/12/2019 try this color picker: https://www.youtube.com/watch?v=GlR7wqWEomU

}
