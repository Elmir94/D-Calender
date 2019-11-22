package com.hackerman.dcalender;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);


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
        for( int i = 0; i < timeStamps.length; i++ )
        {
            TextView textView = new TextView(this);
            textView.setText(timeStamps[i]);
            textView.setGravity(Gravity.RIGHT);
            textView.setHeight(150);
            timeStampsView.addView(textView);
        }



    }


}

