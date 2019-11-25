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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);

        verticalScroll = (ScrollViewHandler) findViewById(R.id.verticalScroll);

        mainLayout = (LinearLayout) findViewById(R.id.daySchedule1);
        findViewById(R.id.button4).setOnTouchListener(onTouchListener());

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
        addBottomMargin.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,97));
        timeStampsView.addView(addBottomMargin);

    }


    private OnTouchListener onTouchListener() {
        return new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                final int y = (int) event.getRawY();

                switch (event.getAction() & MotionEvent.ACTION_MASK) {

                    case MotionEvent.ACTION_DOWN:
                        LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams)
                                view.getLayoutParams();

                        yDelta = y - lParams.topMargin;

                        verticalScroll.setEnableScrolling(false);
                        break;

                    case MotionEvent.ACTION_UP: //Release touch
                        Snackbar clickMessage = Snackbar.make(view, "ACTION_UP", Snackbar.LENGTH_SHORT);
                        clickMessage.show();
                        verticalScroll.setEnableScrolling(true);
                        break;

                    case MotionEvent.ACTION_MOVE: //Moving while holding down
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view
                                .getLayoutParams();
                        layoutParams.topMargin = y - yDelta;
                        layoutParams.bottomMargin = 0;
                        view.setLayoutParams(layoutParams);
                        break;
                }

                //mainLayout.invalidate();
                return false;
            }
        };
    }



}

