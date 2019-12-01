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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mainLayout;
    private int yDelta;
    ScrollViewHandler verticalScroll;

    //Values for scheduleviews
    private int schedule_start = 100;
    private int schedule_5min = 10; //Offset
    private int schedule_hour = schedule_5min * 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        verticalScroll = (ScrollViewHandler) findViewById(R.id.verticalScroll);

        mainLayout = (LinearLayout) findViewById(R.id.schedule);
        LinearLayout day1 = (LinearLayout) findViewById(R.id.daySchedule1);
        LinearLayout day2 = (LinearLayout) findViewById(R.id.daySchedule2);
        for(int i = 0; i < day1.getChildCount(); ++i) {
            View nextChild = day1.getChildAt(i);
            nextChild.setOnTouchListener(onTouchListener());
        }
        for(int i = 0; i < day2.getChildCount(); ++i) {
            View nextChild = day2.getChildAt(i);
            nextChild.setOnTouchListener(onTouchListener());
        }

        setupTimestamps();

        ScrollView mScrollView = (ScrollView) findViewById(R.id.verticalScroll);
        mScrollView.post(new Runnable() {
            public void run() {
                ScrollView mScrollView = (ScrollView) findViewById(R.id.verticalScroll);
                mScrollView.scrollTo(0, 8*150);
            }
        });
        //Add tasks
        LinearLayout day1View = (LinearLayout) findViewById(R.id.daySchedule1);

        Button myButton = (Button) findViewById(R.id.button8);



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
        timeStamps = new String[24];
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

        View addMargin = new View(this);
        addMargin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,75));
        timeStampsView.addView(addMargin);

        View addMarginLines = new View(this);
        addMarginLines.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,75));
        timeStampLinesView.addView(addMarginLines);


        for( int i = 0; i < timeStamps.length; i++ )
        {
            TextView textView = new TextView(this);
            textView.setText(timeStamps[i]);
            textView.setGravity(Gravity.TOP | Gravity.END);
            textView.setHeight(150);
            textView.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            timeStampsView.addView(textView);

            View line = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,5));
            params.setMargins(10,25,0,120);
            line.setLayoutParams(params);
            line.setBackgroundColor(Color.DKGRAY);
            timeStampLinesView.addView(line);


        }

        View addBottomMargin = new View(this);
        addBottomMargin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,100));
        timeStampsView.addView(addBottomMargin);

    }


    private OnTouchListener onTouchListener() {
        return new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_BUTTON_PRESS:
                        Snackbar clickMess = Snackbar.make(view, "Clicked on deep task view", Snackbar.LENGTH_SHORT);
                        clickMess.show();

                    case MotionEvent.ACTION_DOWN:
                        LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams)
                                view.getLayoutParams();

                        yDelta = y - lParams.topMargin;

                        verticalScroll.setEnableScrolling(false);
                        break;

                    case MotionEvent.ACTION_UP: //Release touch
                        //Snackbar clickMessage = Snackbar.make(view, "Released touch", Snackbar.LENGTH_SHORT);
                        //clickMessage.show();

                        verticalScroll.setEnableScrolling(true);
                        break;

                    case MotionEvent.ACTION_MOVE: //Moving while holding down
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.topMargin = ((y - yDelta) / schedule_5min) * schedule_5min;                   //Snap to 5 min
                        layoutParams.bottomMargin = 0;

                        if ( y < schedule_start) {
                            Snackbar message = Snackbar.make(view, "Stop!", Snackbar.LENGTH_SHORT);
                            message.show();
                        }
                        else if (y + layoutParams.height > findViewById(R.id.timeStamps).getHeight()) {
                            layoutParams.topMargin = y - findViewById(R.id.timeStamps).getHeight();
                        }

                        view.setLayoutParams(layoutParams);
                        break;
                }

                mainLayout.invalidate();
                return false;
            }
        };
    }



}

