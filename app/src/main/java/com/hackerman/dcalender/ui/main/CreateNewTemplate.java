package com.hackerman.dcalender.ui.main;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackerman.dcalender.R;
import com.hackerman.dcalender.database.AppDatabase;
import com.hackerman.dcalender.database.entity.MainActivity;
import com.hackerman.dcalender.database.entity.SubActivity;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;

import yuku.ambilwarna.AmbilWarnaDialog;

public class CreateNewTemplate extends AppCompatActivity {

    private static final String TAG = "CreateNewTemplate";

    EditText mainActivity;
    EditText subActivity;
    //color
    ConstraintLayout layout;
    int activityColor;
    FloatingActionButton selectColor;
    //buttons
    TextView saveTemplate;
    TextView cancel;
    //Variables for db if-statements
    int subActivityId;
    String mainActivityName;
    String subActivityName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_template);

        mainActivity = findViewById(R.id.mainActivityEditView);
        subActivity = findViewById(R.id.subActivityEditView);
        //color
        layout = findViewById(R.id.coordinatorLayout);
        activityColor = ContextCompat.getColor(this, R.color.activityBackground);
        selectColor = findViewById(R.id.selectColorFab);

        saveTemplate = findViewById(R.id.saveBtn);
        cancel = findViewById(R.id.cancelBtn);


        // TODO: 05/12/2019 RoomDatabase migration support
        // TODO: 05/12/2019 Look for solution to avoid using UI MainThread for database queries
        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries() //Allows database to read & writ on main UI thread. This is a terrible idea DO NOT DO THIS!!!
                .build();

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
                        // TODO: 04/12/2019 refine toast msg
                        Toast.makeText(getApplicationContext(), "This activity already exists", Toast.LENGTH_LONG).show();
                        //Log.d(TAG, "This activity already exists");
                    }
                    else {
                        db.subActivityDao().insertAll(new SubActivity(mainActivity.getText().toString(), subActivity.getText().toString(), activityColor ));
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
                //Test to see that color picker works
                //layout.setBackgroundColor(activityColor);
                selectColor.setBackgroundTintList(ColorStateList.valueOf(activityColor));
                selectColor.setRippleColor(activityColor);
                //R.color.activityBackground = activityColor;
            }
        });
        colorPicker.show();
    }

    //material ui color picker
    private void openColorPicker2() {
        String title = "title";
        ColorChooserDialog dialog = new ColorChooserDialog(this);
        dialog.setTitle(title);
        dialog.setColorListener(new ColorListener() {
            @Override
            public void OnColorClick(View v, int color) {
                activityColor = color;
                //Test to see that color picker works
                //layout.setBackgroundColor(activityColor);
                selectColor.setBackgroundTintList(ColorStateList.valueOf(activityColor));
                selectColor.setRippleColor(activityColor);
            }
        });
        //customize the dialog however you want
        dialog.show();
    }

    // TODO: 05/12/2019 try this color picker: https://www.youtube.com/watch?v=MZ1GMHzAVPU  

    // TODO: 05/12/2019 try this color picker: https://www.youtube.com/watch?v=GlR7wqWEomU

}
