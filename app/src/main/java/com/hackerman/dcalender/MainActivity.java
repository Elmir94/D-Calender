package com.hackerman.dcalender;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mainLayout;

    //Values for scheduleviews
    private final int schedule_5min = 15;                       //Offset
    private final int schedule_hour = schedule_5min * 12;
    private int schedule_snap_grid = 4;                         //2=30min, 4=15min, 6=10min, 12=5min (Hour divider)

    //Touchhandling
    private int yDelta;
    ScrollViewHandler verticalScroll;
    private boolean moved = false;
    private boolean resize = false;
    private boolean delete = false;
    private int viewHeight = 0;
    private boolean[][] occupiedSpace = new boolean[2][25*schedule_hour];    //For collision check, [day][yPos]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        schedule_snap_grid = schedule_hour / schedule_snap_grid;
        for (int i = 0; i < occupiedSpace[0].length; i++) {
            occupiedSpace[0][i] = false;
            occupiedSpace[1][i] = false;
        }

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
        //Add tasks

    }


    public void onTaskClick(View view)
    {
        Snackbar clickMessage = Snackbar.make(view, "Redirected to deep task view", Snackbar.LENGTH_SHORT);
        clickMessage.show();
    }

    public void openSidebar(View view)
    {
        Snackbar clickMessage = Snackbar.make(view, "Opening sidebar", Snackbar.LENGTH_SHORT);
        clickMessage.show();
    }

    private void addTask (View view, int yPos) {
        yPos = (yPos / schedule_snap_grid) * schedule_snap_grid;
        Button Task = new Button(this);
        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, schedule_hour);
        boolean canSpawn = true;

        int height = schedule_hour;
        while (checkCollision(yPos,yPos + height, getDayIndex(view.getId()))) {
            height -= schedule_snap_grid;
            parameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height);
        }

        Task.setLayoutParams(parameters);
        Task.setBackgroundResource(R.drawable.gradienttaskbg);
        Task.setText("Task");
        Task.setTextColor(Color.WHITE);
        FrameLayout layout = (FrameLayout) findViewById(view.getId());
        layout.addView(Task);

        Task.animate()
                .y(yPos)
                .setDuration(0)
                .start();

        Task.setOnTouchListener(onTouchListener());


        occupySpace(yPos, yPos + parameters.height, getDayIndex(view.getId()));
    }

    private void occupySpace (int from, int to, int dayIndex) {
        for (int i = from; i < to; i++) {
            occupiedSpace[dayIndex][i] = true;
        }
    }

    private void emptySpace (int from, int to, int dayIndex) {
        for (int i = from; i < to; i++) {
            occupiedSpace[dayIndex][i] = false;
        }
    }

    private boolean checkCollision(int from, int to, int dayIndex) {

        if ( from > 0 && from < occupiedSpace.length && to > 0 && to < occupiedSpace.length) {

            for (int i = from + 1; i < to - 1; i+=schedule_snap_grid) {
                if (occupiedSpace[dayIndex][i]) {
                    return true;
                }
            }
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
                        addTask (view, (int) event.getY());
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
                                occupySpace((int)view.getY(), (int)view.getY() + view.getHeight(), dayIndex);
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

                                if (setPos < schedule_hour) { //Out of bounds
                                    setPos = schedule_hour;
                                } else if (setPos >= schedule_hour * 25 - view.getHeight()) {
                                    setPos = schedule_hour * 25 - view.getHeight();
                                }

                                if (resize) {

                                    int newHeight = viewHeight + (setPos - (int) view.getY());
                                    if (newHeight < schedule_snap_grid) {
                                        newHeight = schedule_snap_grid;
                                    }
                                    if (!checkCollision((int)event.getY(), setPos, dayIndex)) {
                                        ViewGroup.LayoutParams parameters = view.getLayoutParams();
                                        parameters.height = newHeight;
                                        view.setLayoutParams(parameters);
                                    }

                                } else {
                                    if (!checkCollision(setPos, setPos +view.getHeight(), dayIndex)) {
                                        view.animate()
                                                .x(20)
                                                .y(setPos)
                                                .setDuration(0)
                                                .start();
                                    }
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

