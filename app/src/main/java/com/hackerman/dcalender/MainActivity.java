package com.hackerman.dcalender;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.hackerman.dcalender.database.AppDatabase;
import com.hackerman.dcalender.database.AppDatabase;
import com.hackerman.dcalender.database.entity.SubActivity;
import com.hackerman.dcalender.ui.main.DeepTaskView;
import com.hackerman.dcalender.ui.main.TemplateManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mainLayout;

    //Values for scheduleviews
    public final int schedule_5min = 15;                       //Offset
    public final int schedule_hour = schedule_5min * 12;
    public int schedule_snap_grid = 4;                         //2=30min, 4=15min, 6=10min, 12=5min (Hour divider)
    Calendar calendar = Calendar.getInstance();
    DrawerLayout dl;
    ArrayList<Date> dates1 = new ArrayList<Date>();
    ArrayList<Date> dates2 = new ArrayList<Date>();
    int recyclerViewPos;
    List<Task> tasks1 = new ArrayList<Task>();
    List<Task> tasks2 = new ArrayList<Task>();

    AppDatabase db;

    //Touchhandling
    private int yDelta;
    ScrollViewHandler verticalScroll;
    private boolean moved = false;
    private boolean resize = false;
    private boolean delete = false;
    private int viewHeight = 0;
    private boolean[][] occupiedSpace = new boolean[2][(25*schedule_snap_grid)+ 1];    //For collision check, [day][yPos]
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        //database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries() //Allows database to read & writ on main UI thread. This is a terrible idea DO NOT DO THIS!!!
                .build();

        mContext = this;

        schedule_snap_grid = schedule_hour / schedule_snap_grid;
        for (int i = 0; i < occupiedSpace[0].length; i++) {
            occupiedSpace[0][i] = false;
            occupiedSpace[1][i] = false;
        }

        setupDates((Date) calendar.getTime());
        verticalScroll = (ScrollViewHandler) findViewById(R.id.verticalScroll);
        setupTimestamps();
        setupTasks();
        ScrollView mScrollView = (ScrollView) findViewById(R.id.verticalScroll);
        mScrollView.post(new Runnable() {
            public void run() {
                ScrollView mScrollView = (ScrollView) findViewById(R.id.verticalScroll);
                mScrollView.scrollTo(0, 7 * schedule_hour);
            }
        });

        createDrawer();
        //Add tasks

        System.out.println("____________________________Date________________: "+calendar.getTime());

    }

    private void createDrawer() {

        ActionBarDrawerToggle t;
        com.google.android.material.navigation.NavigationView nv;

        dl = (DrawerLayout)findViewById(R.id.NavBar);
        t = new ActionBarDrawerToggle(this, dl,R.string.dayView2Date, R.string.dayView1Weekday);

        nv = (com.google.android.material.navigation.NavigationView)findViewById(R.id.nv);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                System.out.println("Here\n");
                int item = menuItem.getItemId();
                switch (item){
                    //First item in list
                    case R.id.TaskManager:

                        TemplateManager man = new TemplateManager();
                        Intent i = new Intent(mContext, man.getClass());
                        startActivity(i);

                        System.out.println("Template Manager\n");
                        break;

                    //Second item in list   ... etc
                    case R.id.settings:
                        System.out.println("Settings\n");
                        break;


                }
                return false;
            }
        });
    }


    public void onTaskClick(View view)
    {
        Snackbar clickMessage = Snackbar.make(view, "Redirected to deep task view", Snackbar.LENGTH_SHORT);
        clickMessage.show();
        Intent intent = new Intent(this, DeepTaskView.class);

        intent.putExtra("taskID", view.getId());
        startActivity(intent);
    }

    public void openSidebar(View view)
    {
        DrawerLayout navview = (DrawerLayout) findViewById(R.id.NavBar);
        navview.openDrawer(Gravity.LEFT);
    }

    private void setupDates(Date middleDate) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.datesContainer);
        recyclerView.setLayoutManager(layoutManager);
        dates1.clear();
        dates2.clear();
        calendar.setTime(middleDate);
        calendar.add(Calendar.DATE, -10);
        for (int i = 0; i < 12; i++) {
            dates1.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
            dates2.add(calendar.getTime());
            calendar.add(Calendar.DATE, 1);
        }

        final RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, dates1, dates2);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(5);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Toast.makeText(MainActivity.this, "Scrolled, dx:"+dx+", dy:"+dy, Toast.LENGTH_SHORT).show();
                int position = recyclerView.computeHorizontalScrollOffset();
                int offset = recyclerView.computeHorizontalScrollExtent();
                if (position % offset == 0) {
                    saveTasksToMemory(recyclerViewPos);
                    recyclerViewPos = position / offset;
                    getTasksFromMemory(recyclerViewPos);
                }
            }
        });
        getTasksFromMemory(recyclerViewPos);
    }

    private void saveTasksToMemory(int saveToPos) { //TODO
        FrameLayout daySchedule1 = (FrameLayout) findViewById(R.id.daySchedule1);
        FrameLayout daySchedule2 = (FrameLayout) findViewById(R.id.daySchedule2);

        //TODO: Save to database dates here

        emptySpace(schedule_hour, schedule_hour*25, 0);

        for (int i = 0; i < tasks1.size(); i++) {
            Task temp = tasks1.get(i);

            /*
            db.taskDao().insertAll(new DBTask(
                    temp.templateID,
                    temp.name,
                    temp.hexColor,
                    temp.date,
                    temp.timeFrom,
                    temp.timeTo));

             */
            daySchedule1.removeView((View) findViewById(tasks1.get(i).id));
        }

        emptySpace(schedule_hour, schedule_hour*25, 1);
        for (int i = 0; i < tasks2.size(); i++) {
            Task temp = tasks2.get(i);

            /*
            db.taskDao().insertAll(new DBTask(
                    temp.templateID,
                    temp.name,
                    temp.hexColor,
                    temp.date,
                    temp.timeFrom,
                    temp.timeTo));

             */

            daySchedule2.removeView((View) findViewById(tasks2.get(i).id));
        }

        tasks1.clear();
        tasks2.clear();
    }

    private void getTasksFromMemory(int getFromPos) { //TODO

        SimpleDateFormat dateFormat = new SimpleDateFormat(R.string.dataFormat);
        String dbDateString1 = dateFormat.format(dates1.get(recyclerViewPos));
        String dbDateString2 = dateFormat.format(dates2.get(recyclerViewPos));

        //List<com.hackerman.dcalender.database.entity.SubActivity> date1Entires = db.subActivityDao().getAllSubActivitiesOnDate(dbDateString1);
        //List<com.hackerman.dcalender.database.entity.SubActivity> date2Entires = db.subActivityDao().getAllSubActivitiesOnDate(dbDateString2);

        for (int i = 0; i < dbTasks1.size(); i++) {
            loadTask((FrameLayout) findViewById(R.id.daySchedule1), dbTasks1.get(i).convertToScheduleTask());

            Task print = dbTasks1.get(i).convertToScheduleTask();
            Snackbar clickMessage = Snackbar.make(mainLayout,"Id:"+ db.taskDao().getIdOnTimeFrom(), Snackbar.LENGTH_LONG);
            clickMessage.show();
            /*Snackbar clickMessage = Snackbar.make(mainLayout,"Name: "+print.name+", ID: "+print.id+
                    ", HexColor: "+print.hexColor+", Date:"+print.date+", TemplateID: "+print.templateID+
                    ", From:"+print.timeFrom+", To: "+print.timeTo, Snackbar.LENGTH_LONG);
            clickMessage.show(); */
        }

        for (int i = 0; i < dbTasks2.size(); i++) {
            loadTask((FrameLayout) findViewById(R.id.daySchedule2), dbTasks2.get(i).convertToScheduleTask());

            Task print = dbTasks1.get(i).convertToScheduleTask();
            Snackbar clickMessage = Snackbar.make(mainLayout,"Name: "+print.name+", ID: "+print.id+
                    ", HexColor: "+print.hexColor+", Date:"+print.date+", TemplateID: "+print.templateID+
                    ", From:"+print.timeFrom+", To: "+print.timeTo, Snackbar.LENGTH_LONG);
            clickMessage.show();
        }
        //db.taskDao().insertAll(new com.hackerman.dcalender.database.entity.Task());
    }

    private void loadTask (View view, Task task) { //TODO, Add to date ArrayLists
        int yPos = (int)(task.timeFrom * schedule_hour);
        Button button = new Button(this);
        int height = (int)(task.timeTo - task.timeFrom)*schedule_hour;

        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        button.setLayoutParams(parameters);
        button.setId(task.id);
        button.setBackgroundColor(Color.parseColor(task.hexColor));
        button.setText(task.name);
        button.setTextColor(Color.WHITE);
        FrameLayout layout = (FrameLayout) findViewById(view.getId());
        layout.addView(button);

        if(view.getId() == R.id.daySchedule1) {
            tasks1.add(task);
        } else {
            tasks2.add(task);
        }

        button.animate()
                .y(0-height)
                .setDuration(0)
                .start();

        button.animate()
                .y(yPos)
                .setDuration(500)
                .start();

        button.setOnTouchListener(onTouchListener());

        occupySpace(yPos, yPos + parameters.height, getDayIndex(view.getId()));

    }

    private void addTask (View view, int yPos) { //TODO, Add to date ArrayLists
        yPos = (yPos / schedule_snap_grid) * schedule_snap_grid;
        Date day = new Date();
        if (view.getId() == R.id.daySchedule1) {
            day = dates1.get(recyclerViewPos);
        } else {
            day = dates2.get(recyclerViewPos);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(R.string.dataFormat);
        String dbDateString = dateFormat.format(day);

        Task task = new Task(
                "New Task",
                dbDateString,
                (float)yPos/schedule_hour,
                ((float)yPos/schedule_hour) + 1,
                "#808080");

        if (view.getId() == R.id.daySchedule1) {
            tasks1.add(task);
        } else {
            tasks2.add(task);
        }


        db.subActivityDao().insertAll(new SubActivity(
                "None",
                "None",
                "New Task",
                "#808080",
                task.timeFrom,
                task.timeTo,
                task.date,));

        String mainActivityName, String subActivityName, int activityColor, float timeFrom, float timeTo, String date, String taskName, String taskText



        Button button = new Button(this);
        int height = (int)(task.timeTo - task.timeFrom)*schedule_hour;

        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        button.setLayoutParams(parameters);
        button.setId(task.id);
        button.setBackgroundColor(Color.parseColor(task.hexColor));
        button.setText(task.name);
        button.setTextColor(Color.WHITE);
        FrameLayout layout = (FrameLayout) findViewById(view.getId());
        layout.addView(button);

        button.animate()
                .y(yPos)
                .setDuration(0)
                .start();

        button.setOnTouchListener(onTouchListener());

        occupySpace(yPos, yPos + parameters.height, getDayIndex(view.getId()));
    }

    private void occupySpace (int from, int to, int dayIndex) {
        int fromIndex = from/schedule_snap_grid;
        int toIndex = to/schedule_snap_grid;

        for (int i = fromIndex; i < toIndex; i++) {
            occupiedSpace[dayIndex][i] = true;
        }
    }

    private void emptySpace (int from, int to, int dayIndex) {
        int fromIndex = from/schedule_snap_grid;
        int toIndex = to/schedule_snap_grid;

        for (int i = fromIndex; i < toIndex; i++) {
            occupiedSpace[dayIndex][i] = false;
        }
    }

    private boolean checkCollision(int from, int to, int dayIndex) {
        int fromIndex = from/schedule_snap_grid;
        int toIndex = to/schedule_snap_grid;

        if (!checkOutOfBounds(from, to)) {

            for (int i = fromIndex; i < toIndex; i++) {

                if (occupiedSpace[dayIndex][i]) {
                    return true;
                }
            }
        } else {
            return true;
        }

        return false;
    }

    private boolean checkOutOfBounds (int top, int bottom) {
        int topIndex = top/schedule_snap_grid;
        int bottomIndex = bottom/schedule_snap_grid;
        int maxTopPos = schedule_hour/schedule_snap_grid;
        int maxBottomPos = (schedule_hour/schedule_snap_grid) * 25;

        //Snackbar clickMessage = Snackbar.make(mainLayout, "TopIndex: "+topIndex+", bottomIndex: "+bottomIndex+", maxTopPos: "+maxTopPos+", maxBottomPos: "+maxBottomPos, Snackbar.LENGTH_SHORT);
        //clickMessage.show();

        if (maxTopPos > topIndex || bottomIndex > maxBottomPos) {
            return true;
        }
        return false;
    }

    private void loadDaysToOccupiedSpace() { //TODO

        LinearLayout schedule = (LinearLayout) findViewById(R.id.schedule);

        FrameLayout day1 = (FrameLayout) findViewById(R.id.daySchedule1);
        FrameLayout day2 = (FrameLayout) findViewById(R.id.daySchedule2);
        for (int i = 0; i < day1.getChildCount(); i++) {
            if (day1.getChildAt(i) instanceof Button) {
                occupySpace((int)day1.getChildAt(i).getY(), (int)day1.getChildAt(i).getY() + day1.getChildAt(i).getHeight(), 0);
            }
        }
        for (int i = 0; i < day2.getChildCount(); i++) {
            if (day2.getChildAt(i) instanceof Button) {
                occupySpace((int)day2.getChildAt(i).getY(), (int)day2.getChildAt(i).getY() + day2.getChildAt(i).getHeight(), 1);
            }
        }
    }

    private void initTouchListeners () {
        mainLayout = (LinearLayout) findViewById(R.id.schedule);
        FrameLayout day1 = (FrameLayout) findViewById(R.id.daySchedule1);
        FrameLayout day2 = (FrameLayout) findViewById(R.id.daySchedule2);
        //Listen to touch on objects
        day1.setOnTouchListener(onTouchListener());
        day2.setOnTouchListener(onTouchListener());
        for(int i = 0; i < day1.getChildCount(); ++i) {
            View nextChild = day1.getChildAt(i);
            nextChild.setOnTouchListener(onTouchListener());
        }
        for(int i = 0; i < day2.getChildCount(); ++i) {
            View nextChild = day2.getChildAt(i);
            nextChild.setOnTouchListener(onTouchListener());
        }
    }

    private void setupTimestamps(){
        String[] timeStamps;
        timeStamps = new String[25];
        for (int i = 0; i < timeStamps.length; i++) {
            if (i < 10) {
                timeStamps[i] = "0" + i + ":00";
            }
            else {
                timeStamps[i] = i + ":00";
            }
        }

        LinearLayout timeStampsView = (LinearLayout) findViewById(R.id.timeStamps);
        LinearLayout timeStampLinesView = (LinearLayout) findViewById(R.id.timeStampLines);
        FrameLayout d1Schedule = (FrameLayout) findViewById(R.id.daySchedule1);
        FrameLayout d2Schedule = (FrameLayout) findViewById(R.id.daySchedule2);

        View topMargin = new View(this);
        topMargin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, schedule_hour - 30));
        timeStampsView.addView(topMargin);


        for( int i = 0; i < timeStamps.length; i++ )
        {
            //Stamps
            TextView textView = new TextView(this);
            textView.setText(timeStamps[i]);
            textView.setGravity(Gravity.TOP | Gravity.END);
            textView.setHeight(schedule_hour);
            textView.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            timeStampsView.addView(textView);

            //Lines
            View line = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5);
            params.setMargins(10,0,0,0);
            line.setLayoutParams(params);
            line.setBackgroundColor(Color.DKGRAY);
            timeStampLinesView.addView(line);

            View d1line = new View(this);
            LinearLayout.LayoutParams d1params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5);
            //params.setMargins(10,0,0,schedule_hour - 5);
            d1line.setLayoutParams(d1params);
            d1line.setBackgroundColor(Color.LTGRAY);
            d1Schedule.addView(d1line);

            View d2line = new View(this);
            LinearLayout.LayoutParams d2params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5);
            //params.setMargins(10,0,0,schedule_hour - 5);
            d2line.setLayoutParams(d2params);
            d2line.setBackgroundColor(Color.LTGRAY);
            d2Schedule.addView(d2line);

            int yPos = (schedule_hour * (i+1)) - (5 * i); //(i+1 for top margin)
            line.animate()
                    .y(yPos)
                    .setDuration(0)
                    .start();

            yPos = yPos + (i*5);

            d1line.animate()
                    .y(yPos)
                    .setDuration(0)
                    .start();

            d2line.animate()
                    .y(yPos)
                    .setDuration(0)
                    .start();


        }


        View addBottomMargin = new View(this);
        addBottomMargin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, schedule_hour));
        timeStampsView.addView(addBottomMargin);

    }

    private int getDayIndex(int dayId) {
        switch (dayId) {
            case R.id.daySchedule1:
                return 0;

            case R.id.daySchedule2:
                return 1;
        }
        return -1;
    }

    private void setupTasks() {
        //Fetch from database

        loadDaysToOccupiedSpace();
        initTouchListeners();
    }

    private OnTouchListener onTouchListener() {
        return new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int y = (int) event.getRawY();

                //When Schedule
                if (view instanceof FrameLayout) {

                    if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        if (!checkOutOfBounds((int) event.getY(), (int) event.getY())) {
                            addTask (view, (int) event.getY());
                        }
                    }
                }

                //When Task
                else if (view instanceof Button){
                    int dayIndex = getDayIndex(((FrameLayout) view.getParent()).getId());

                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:

                            moved = false;
                            resize = false;
                            delete = false;
                            yDelta = (int) view.getY() - y;

                            int height = view.getHeight();
                            emptySpace((int)view.getY(), (int)view.getY() + view.getHeight(), dayIndex);

                            //If bottom of task pressed
                            if ((int) event.getY() >= (height - (height * 0.30))) {
                                viewHeight = view.getHeight();
                                resize = true;
                            }

                            verticalScroll.setEnableScrolling(false);
                            break;

                        case MotionEvent.ACTION_UP: //Release touch

                            if (!moved) {
                                onTaskClick(view);
                            }
                            else if (delete) {
                                emptySpace((int)view.getY(), (int)view.getY() + view.getHeight(), dayIndex);
                                ((FrameLayout) view.getParent()).removeView(view);
                            }
                            else if (!resize) {
                                view.animate()
                                        .x(0)
                                        .setDuration(0)
                                        .start();
                                occupySpace((int)view.getY(), (int)view.getY() + view.getHeight(), dayIndex);
                            }
                            else {
                                occupySpace((int)view.getY(), (int)view.getY() + view.getHeight(), dayIndex);
                            }

                            verticalScroll.setEnableScrolling(true);
                            break;

                        case MotionEvent.ACTION_MOVE: //Moving while holding down

                            int setPos = ((y + yDelta) / schedule_snap_grid) * schedule_snap_grid;
                            moved = true;
                            delete = false;
                            int[] positions = new int[2];
                            view.getLocationOnScreen(positions);

                            //Delete on swipe left
                            if ((int) event.getRawX() - positions[0] < 0) {
                                delete = true;
                                view.setAlpha(0.5f);
                            }

                            else { //Don't delete
                                view.setAlpha(1);

                                if (resize) {

                                    int newHeight = viewHeight + (setPos - (int) view.getY());
                                    if (newHeight < schedule_snap_grid) {
                                        newHeight = schedule_snap_grid;
                                    }
                                    //Snackbar poop = Snackbar.make(view, (int)view.getY()+", "+((int)view.getY()+newHeight)+", 25*hour="+(25*schedule_hour), Snackbar.LENGTH_SHORT);
                                    //poop.show();
                                    if (!checkCollision((int)view.getY(), (int)view.getY() + newHeight, dayIndex)) {
                                        ViewGroup.LayoutParams parameters = view.getLayoutParams();
                                        parameters.height = newHeight;
                                        view.setLayoutParams(parameters);
                                        //emptySpace((int)view.getY(), (int)view.getY() + view.getHeight(), dayIndex);
                                    }
                                }
                                else if (!checkCollision(setPos, setPos +view.getHeight(), dayIndex)) {
                                    view.animate()
                                            .x(20)
                                            .y(setPos)
                                            .setDuration(0)
                                            .start();
                                }

                            }


                            //view.setLayoutParams(layoutParams);
                            break;
                    }
                }

                //mainLayout.invalidate();
                return true;
            }
        };
    }


}

