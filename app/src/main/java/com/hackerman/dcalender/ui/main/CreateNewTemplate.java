package com.hackerman.dcalender.ui.main;

import android.content.Intent;
import android.os.Bundle;
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

public class CreateNewTemplate extends AppCompatActivity {

    private static final String TAG = "CreateNewTemplate";

    EditText mainActivity;
    EditText subActivity;
    Button saveTemplate;

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

        saveTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG,"onClick firstName: "+firstName.getText().toString());
                /*User user = new User(firstName.getText().toString(),lastName.getText().toString(),email.getText().toString());
                db.userDao().insertAll(user);*/

                //this saves a information to the old database version still needed for displaying activitis in RC-view
                db.templateDao().insertAll(new Template(mainActivity.getText().toString(),subActivity.getText().toString()));

                //New detabase model
                db.mainActivityDao().insertAll(new MainActivity(mainActivity.getText().toString()));
                db.subActivityDao().insertAll(new SubActivity(mainActivity.getText().toString(),subActivity.getText().toString()));
                //This is to go from CreateUser view -> Main view
                startActivity(new Intent(CreateNewTemplate.this, TemplateView.class));
            }
        });

    }
}
// TODO: 2019-12-02 nfkdgndskngksknsgdnksdgkngsdknkn 