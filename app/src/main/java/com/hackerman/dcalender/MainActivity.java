package com.hackerman.dcalender;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
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
    private int schedule_5min = 15; //Offset
    private int schedule_hour = schedule_5min * 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        verticalScroll = (ScrollViewHandler) findViewById(R.id.verticalScroll);

        mainLayout = (LinearLayout) findViewById(R.id.schedule);
        LinearLayout day1 = (LinearLayout) findViewById(R.id.daySchedule1);
        LinearLayout day2 = (LinearLayout) findViewById(R.id.daySchedule2);
        //Listen to touch on objects
        for(int i = 0; i < day1.getChildCount(); ++i) {
            View nextChild = day1.getChildAt(i);
            nextChild.setOnTouchListener(onTouchListener());
        }
        for(int i = 0; i < day2.getChildCount(); ++i) {
            View nextChild = day2.getChildAt(i);
            nextChild.setOnTouchListener(onTouchListener());
        }

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
        LinearLayout day1View = (LinearLayout) findViewById(R.id.daySchedule1);

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
        LinearLayout d1Schedule = (LinearLayout) findViewById(R.id.daySchedule1);
        LinearLayout d2Schedule = (LinearLayout) findViewById(R.id.daySchedule2);

        View topMargin = new View(this);
        topMargin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, schedule_hour - 30));
        timeStampsView.addView(topMargin);

        View addTopMarginLines = new View(this);
        addTopMarginLines.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, schedule_hour));
        timeStampLinesView.addView(addTopMarginLines);


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
            //params.setMargins(10,0,0,schedule_hour - 5);
            line.setLayoutParams(params);
            line.setBackgroundColor(Color.DKGRAY);
            timeStampLinesView.addView(line);

            View d1line = new View(this);
            LinearLayout.LayoutParams d1params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5));
            //params.setMargins(10,0,0,schedule_hour - 5);
            d1line.setLayoutParams(d1params);
            d1line.setBackgroundColor(Color.DKGRAY);
            d1Schedule.addView(d1line);

            View d2line = new View(this);
            LinearLayout.LayoutParams d2params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5));
            //params.setMargins(10,0,0,schedule_hour - 5);
            d2line.setLayoutParams(d2params);
            d2line.setBackgroundColor(Color.DKGRAY);
            d2Schedule.addView(d2line);

            int yPos = (schedule_hour * i) - (5 * i);
            line.animate()
                    .y(yPos)
                    .setDuration(0)
                    .start();

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

        //Button Task = new Button(this);
        //LinearLayout.LayoutParams parameters = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,3*schedule_hour));
        //Task.setLayoutParams(parameters);
        //Task.setBackgroundResource(R.drawable.gradienttaskbg);

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
                        }

                        verticalScroll.setEnableScrolling(true);
                        break;

                    case MotionEvent.ACTION_MOVE: //Moving while holding down

                        int snapMetrics = schedule_hour / 4; // 15min
                        int setPos = ((y + yDelta) / snapMetrics) * snapMetrics;

                        if (setPos >= schedule_hour && setPos <= schedule_hour * 25 - view.getHeight()) {
                            moved = true;
                            view.animate()
                                    .y(setPos)
                                    .setDuration(0)
                                    .start();
                        }


                        //view.setLayoutParams(layoutParams);
                        break;
                }

                mainLayout.invalidate();
                return false;
            }
        };
    }



}

