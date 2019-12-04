package com.hackerman.dcalender.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.hackerman.dcalender.R;
import com.hackerman.dcalender.database.AppDatabase;
import com.hackerman.dcalender.database.entity.MainActivity;
import com.hackerman.dcalender.database.entity.SubActivity;
import com.hackerman.dcalender.database.entity.Template;

import java.util.List;

public class CreateNewTemplate extends AppCompatActivity {

    private static final String TAG = "CreateNewTemplate";

    EditText mainActivity;
    EditText subActivity;
    Button saveTemplate;
    List<MainActivity> MyCategries;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_template);

        mainActivity = findViewById(R.id.mainActivityEditView);
        subActivity = findViewById(R.id.subActivityEditView);
        saveTemplate = findViewById(R.id.saveTemplateBtn);

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries() //Allows database to read & writ on main UI thread. This is a terrible idea DO NOT DO THIS!!!
                .build();

      MyCategries=db.mainActivityDao().getAllmainActivities();

        saveTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //this saves a information to the old database version still needed for displaying activitis in RC-view
                //db.templateDao().insertAll(new Template(mainActivity.getText().toString(),subActivity.getText().toString()));

                //New database model
                try{
                    db.mainActivityDao().insertAll(new MainActivity(mainActivity.getText().toString()));
                }catch (Exception e) {
                    Log.d(TAG, "This main activity does already exist: " + mainActivity.getText().toString());
                    //System.out.println(String.format("This main activity does already exist: %s", mainActivity.getText().toString()));
                }



                //db.mainActivityDao().insertAll(new MainActivity(mainActivity.getText().toString()));
                db.subActivityDao().insertAll(new SubActivity(mainActivity.getText().toString(),subActivity.getText().toString()));
                //This is to go from CreateUser view -> Main view
                startActivity(new Intent(CreateNewTemplate.this, TemplateView.class));
            }
        });

    }
}
// TODO: 2019-12-02 nfkdgndskngksknsgdnksdgkngsdknkn 