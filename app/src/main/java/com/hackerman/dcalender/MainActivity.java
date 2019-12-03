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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mainLayout;
    private int yDelta;
    ScrollViewHandler verticalScroll;
    private boolean moved = false;

    //Values for scheduleviews
    private final int schedule_5min = 15;                   //Offset
    private final int schedule_hour = schedule_5min * 12;
    private int schedule_snap_grid = 4;                     //2=30min, 4=15min, 6=10min, 12=5min (Hour divider)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        schedule_snap_grid = schedule_hour / schedule_snap_grid;

        verticalScroll = (ScrollViewHandler) findViewById(R.id.verticalScroll);

        initTouchListeners();
        setupTimestamps();
        //setupTasks();

        ScrollView mScrollView = (ScrollView) findViewById(R.id.verticalScroll);
        mScrollView.post(new Runnable() {
            public void run() {
                ScrollView mScrollView = (ScrollView) findViewById(R.id.verticalScroll);
                mScrollView.scrollTo(0, 8*schedule_hour);
            }
        });
        //Add tasks

    }


    public void onTaskClick(View view)
    {
        Snackbar clickMessage = Snackbar.make(view, "Redirected to deep task view", Snackbar.LENGTH_SHORT);
        clickMessage.show();
    }

    public void addTask (View view, int yPos) {
        yPos = (yPos / schedule_snap_grid) * schedule_snap_grid;
        Button Task = new Button(this);
        LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, schedule_hour));
        Task.setLayoutParams(parameters);
        Task.setBackgroundResource(R.drawable.gradienttaskbg);
        Task.setText("Task");
        FrameLayout layout = (FrameLayout) findViewById(view.getId());
        layout.addView(Task);

        Task.animate()
                .y(yPos)
                .setDuration(0)
                .start();

        Task.setOnTouchListener(onTouchListener());


    }

    public void openSidebar(View view)
    {
        Snackbar clickMessage = Snackbar.make(view, "Opening sidebar", Snackbar.LENGTH_SHORT);
        clickMessage.show();
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
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5));
            params.setMargins(10,0,0,0);
            line.setLayoutParams(params);
            line.setBackgroundColor(Color.DKGRAY);
            timeStampLinesView.addView(line);

            View d1line = new View(this);
            LinearLayout.LayoutParams d1params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5));
            //params.setMargins(10,0,0,schedule_hour - 5);
            d1line.setLayoutParams(d1params);
            d1line.setBackgroundColor(Color.LTGRAY);
            d1Schedule.addView(d1line);

            View d2line = new View(this);
            LinearLayout.LayoutParams d2params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5));
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

    private void setupTasks() {
        //Fetch from database




        //android:id="@+id/button9"
        //android:layout_width="match_parent"
        //android:layout_height="wrap_content"
        //android:layout_marginTop="100px"
        //android:height="225px"
        //android:background="@drawable/gradienttaskbg"
        //android:onClick="onTaskClick"
        //android:text="Training" />
    }

    private OnTouchListener onTouchListener() {
        return new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int y = (int) event.getRawY();

                //When Schedule
                if (view.getId() == R.id.daySchedule1 || view.getId() == R.id.daySchedule2) {

                    if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        addTask (view, (int) event.getY());
                    }
                }

                //When Task
                else {
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {

                        case MotionEvent.ACTION_DOWN:

                            moved = false;
                            yDelta = (int) view.getY() - y;

                            verticalScroll.setEnableScrolling(false);
                            break;

                        case MotionEvent.ACTION_UP: //Release touch
                            //Snackbar clickMessage = Snackbar.make(view, "startPos: " + startPos + "  view.getY(): "+ view.getY(), Snackbar.LENGTH_SHORT);
                            //clickMessage.show();

                            if (!moved) {
                                onTaskClick(view);
                            } else {
                                view.animate()
                                        .x(0)
                                        .setDuration(0)
                                        .start();
                            }

                            verticalScroll.setEnableScrolling(true);
                            break;

                        case MotionEvent.ACTION_MOVE: //Moving while holding down

                            int setPos = ((y + yDelta) / schedule_snap_grid) * schedule_snap_grid;
                            moved = true;

                            if (setPos < schedule_hour) {
                                setPos = schedule_hour;
                            } else if (setPos >= schedule_hour * 25 - view.getHeight()) {
                                setPos = schedule_hour * 25 - view.getHeight();
                            }

                            view.animate()
                                    .x(10)
                                    .y(setPos)
                                    .setDuration(0)
                                    .start();


                            //view.setLayoutParams(layoutParams);
                            break;
                    }
                }

                mainLayout.invalidate();
                return true;
            }
        };
    }


}

