package com.hackerman.dcalender.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackerman.dcalender.R;
import com.hackerman.dcalender.database.AppDatabase;
import com.hackerman.dcalender.database.Template;

import java.util.ArrayList;
import java.util.List;


public class TemplateView extends AppCompatActivity {

    private static final String TAG = "TemplateView";

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    FloatingActionButton createNewTemplate;

//    ArrayList<Template> templates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_view);

        recyclerView = findViewById(R.id.templateViewRc);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TemplateAdapter(templates);
        recyclerView.setAdapter(adapter);

        createNewTemplate =findViewById(R.id.NewTemplateFab);
        createNewTemplate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TemplateView.this, CreateNewTemplate.class));
            }
        });

    }
}
