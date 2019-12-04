package com.hackerman.dcalender.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.hackerman.dcalender.R;
import com.hackerman.dcalender.database.AppDatabase;
import com.hackerman.dcalender.database.entity.MainActivity;
import com.hackerman.dcalender.database.entity.SubActivity;

import java.util.Collections;
import java.util.List;

public class CreateNewTemplate extends AppCompatActivity {

    private static final String TAG = "CreateNewTemplate";

    EditText mainActivity;
    EditText subActivity;
    TextView saveTemplate;
    TextView cancel;
    int subActivityId;
    String mainActivityName;
    String subActivityName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_template);

        mainActivity = findViewById(R.id.mainActivityEditView);
        subActivity = findViewById(R.id.subActivityEditView);
        saveTemplate = findViewById(R.id.saveBtn);
        cancel = findViewById(R.id.cancelBtn);



        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries() //Allows database to read & writ on main UI thread. This is a terrible idea DO NOT DO THIS!!!
                .build();

        saveTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivityName = mainActivity.getText().toString();
                subActivityName = subActivity.getText().toString();

                if(mainActivityName.matches("") && subActivityName.matches("") ) {
                    Toast.makeText(getApplicationContext(), "Enter Main Activity & Sub Activity name!", Toast.LENGTH_LONG).show();
                }
                else{
                    try {
                        db.mainActivityDao().insertAll(new MainActivity(mainActivity.getText().toString()));
                    }
                    catch (Exception e) {
                        Log.d(TAG, "This main activity does already exist: " + mainActivity.getText().toString());
                        //System.out.println(String.format("This main activity does already exist: %s", mainActivity.getText().toString()));
                    }

                    subActivityId = db.subActivityDao().getSpecificId(mainActivity.getText().toString(), subActivity.getText().toString());

                    if (subActivityId > 0) {
                        // TODO: 04/12/2019 refine toast msg
                        Toast.makeText(getApplicationContext(), "This activity already exists", Toast.LENGTH_LONG).show();
                        //Log.d(TAG, "This activity already exists");
                    }
                    else {
                        db.subActivityDao().insertAll(new SubActivity(mainActivity.getText().toString(), subActivity.getText().toString()));
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
}
// TODO: 2019-12-02 nfkdgndskngksknsgdnksdgkngsdknkn 