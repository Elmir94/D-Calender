package com.hackerman.dcalender.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackerman.dcalender.R;
import com.hackerman.dcalender.adapters.MainActivityAdapter;
import com.hackerman.dcalender.adapters.SubActivityAdapter;
import com.hackerman.dcalender.adapters.TemplateAdapter;
import com.hackerman.dcalender.database.AppDatabase;
import com.hackerman.dcalender.database.entity.MainActivity;
import com.hackerman.dcalender.database.entity.SubActivity;
import com.hackerman.dcalender.database.entity.Template;

import java.util.List;


public class TemplateView extends AppCompatActivity {

    private static final String TAG = "TemplateView";

    //This is for template RCV
    RecyclerView templateRecyclerView;
    RecyclerView.Adapter templateAdapter;

    //This is for mainActivity RCV
    RecyclerView mainActivityRecyclerView;
    RecyclerView.Adapter mainActivitylateAdapter;

    //This is for subActivity RCV
    RecyclerView subActivityRecyclerView;
    RecyclerView.Adapter subActivityAdapter;

    FloatingActionButton createNewTemplate;

//    ArrayList<Template> templates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_view);

        templateRecyclerView = findViewById(R.id.templateRcv);
        mainActivityRecyclerView = findViewById(R.id.mainActivityRcv);
        subActivityRecyclerView = findViewById(R.id.subActivityRcv);

//        templates = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++){
//            Template template = new Template("Rensy #" +i, "Unknown");
//            templates.add(template);
//        }

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries() //Allows database to read & writ on main UI thread. This is a terrible idea DO NOT DO THIS!!!
                .build();

        List<Template> templates = db.templateDao().getAlltemplates();
        templateRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        templateAdapter = new TemplateAdapter(templates);
        templateRecyclerView.setAdapter(templateAdapter);

        List<MainActivity> mainActivityes = db.mainActivityDao().getAllmainActivities();
        mainActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainActivitylateAdapter = new MainActivityAdapter(mainActivityes);
        mainActivityRecyclerView.setAdapter(mainActivitylateAdapter);

        List<SubActivity> subActivityes = db.subActivityDao().getAllsubActivityis();
        subActivityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        subActivityAdapter = new SubActivityAdapter(subActivityes);
        subActivityRecyclerView.setAdapter(subActivityAdapter);

        createNewTemplate = findViewById(R.id.NewTemplateFab);
        createNewTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TemplateView.this, CreateNewTemplate.class));
            }
        });

    }
}
