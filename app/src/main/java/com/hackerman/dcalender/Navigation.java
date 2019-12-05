package com.hackerman.dcalender;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Navigation extends AppCompatActivity  {

    private ViewGroup mainLayout;
    private int yDelta;
    ScrollViewHandler verticalScroll;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private DrawerLayout dl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);




        //Creates the custom drawer
        createDrawer();

        //Creates the custom calendar
        initializeCalendar();



        openDrawer();

    }
    public void openDrawer(){
        //mDrawerLayout.openDrawer(mDrawerLayout);
    }
    private void selectItemFromDrawer(int pos)
    {

    }


    private void initializeCalendar() {
        View v; // Creating an instance for View Object
        LayoutInflater inflater = (LayoutInflater) this.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.nav_header, null);

        CalendarView simpleCalendarView =  v.findViewById(R.id.simpleCalendarView); // get the reference of Cale// ndarView
        simpleCalendarView.setOnClickListener(new View.OnClickListener() {
                @Override
            public void onClick(View view) {
                System.out.println("Calendar clicked\n");
            }
        });



        simpleCalendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                System.out.println("Calender vieww!!");
                //Save to bundle, put bundle in activity, start activity

            }
        });
    }

    private void createDrawer()
    {

        ActionBarDrawerToggle t;
        com.google.android.material.navigation.NavigationView nv;

        dl = (DrawerLayout)findViewById(R.id.main);
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
                        System.out.println("First item\n");
                        break;

                    //Second item in list   ... etc
                    case R.id.settings:
                        System.out.println("Second item\n");
                        break;


                }
                return false;
            }
        });

        //Put this on the onClick for the menueList
        dl.openDrawer(Gravity.LEFT);

        dl.addDrawerListener(t);
        //dl.openDrawer(Gravity.LEFT);
        t.syncState();


    }

    /*
     * Called when a particular item from the navigation drawer
     * is selected.
     * */
   /* private void selectItemFromDrawer(int position) {
        Fragment fragment = new PreferencesFragment();

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();

        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).mTitle);

        // Close the drawer
        mDrawerLayout.closeDrawer(mDrawerPane);
    }*/
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

    class NavItem {
        String mTitle;
        int mIcon;

        public NavItem(String title, int icon) {
            mTitle = title;
            mIcon = icon;
        }
    }
    class DrawerListAdapter extends BaseAdapter {

        Context mContext;
        ArrayList<NavItem> mNavItems;

        public DrawerListAdapter(Context context, ArrayList<NavItem> navItems) {
            mContext = context;
            mNavItems = navItems;
        }

        @Override
        public int getCount() {
            return mNavItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mNavItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
        ListView mDrawerList;
        RelativeLayout mDrawerPane;
        private ActionBarDrawerToggle mDrawerToggle;
        private DrawerLayout mDrawerLayout;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.drawer_item, null);
            }
            else {
                view = convertView;
            }

            TextView titleView = (TextView) view.findViewById(R.id.title);
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);

            titleView.setText( mNavItems.get(position).mTitle );
            iconView.setImageResource(mNavItems.get(position).mIcon);

            return view;
        }
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

