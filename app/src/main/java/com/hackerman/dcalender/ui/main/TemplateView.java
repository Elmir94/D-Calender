package com.hackerman.dcalender.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hackerman.dcalender.R;
import com.hackerman.dcalender.adapters.MainActivityAdapter;
import com.hackerman.dcalender.adapters.MyExpandableAdapter;
import com.hackerman.dcalender.adapters.SubActivityAdapter;
import com.hackerman.dcalender.adapters.TemplateAdapter;
import com.hackerman.dcalender.database.AppDatabase;
import com.hackerman.dcalender.database.entity.MainActivity;
import com.hackerman.dcalender.database.entity.SubActivity;
import com.hackerman.dcalender.database.entity.Template;
import com.hackerman.dcalender.adapters.MyExpandableAdapter;
import java.util.HashMap;
import java.util.List;



public class TemplateView extends AppCompatActivity implements MyExpandableAdapter.MyListManipulate {

    private static final String TAG = "TemplateView";

    FloatingActionButton AddNewMainActivity;
    FloatingActionButton EditMainActivity;

    ExpandableListView expandableListView;
    MyExpandableAdapter myExpandableAdapter;
    AlertDialog AddParentDialog;
    AlertDialog AddChildDialog;
    AppDatabase db;
    HashMap<String,List<SubActivity>> MyData;
    List<MainActivity> AllCategory;

    boolean MainActivityActionStatus;
    AlertDialog EditParentDialog;
    AlertDialog EditChildtDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_view);

        expandableListView=(ExpandableListView)findViewById(R.id.expandablelist);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        expandableListView.setIndicatorBounds( GetPixelFromDips(0),  GetPixelFromDips(50));


        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "production")
                .allowMainThreadQueries() //Allows database to read & writ on main UI thread. This is a terrible idea DO NOT DO THIS!!!
                .build();

        MainActivityActionStatus=false;
        PopulateData();
        AddNewMainActivity=findViewById(R.id.fab_add);
        EditMainActivity=findViewById(R.id.fab_edit);



//        AddNewMainActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                OpenAddParentDialog();
//
//
//            }
//        });
        EditMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivityActionStatus){
                    MainActivityActionStatus=false;
                    EditMainActivity.setRippleColor(Color.parseColor("#ffffff"));

                }else{
                    MainActivityActionStatus=true;
                    EditMainActivity.setRippleColor(Color.parseColor("#e2e2e2"));
                }
                PopulateData();
            }
        });

        AddNewMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TemplateView.this, CreateNewTemplate.class));
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(MyData.get(AllCategory.get(groupPosition).getMainActivityName()).size()==0){
                    Toast.makeText(getApplicationContext(),"This Main Activity doesn't have any items , Long click on it to add the first item ",Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick( AdapterView<?> parent, View view, int position, long id) {

                long packedPosition = expandableListView.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);


                /*  if group item clicked */
                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    OpenAddChildDialog(AllCategory.get(groupPosition));
                }

                return false;
            }
        });



    }

    public void OpenAddParentDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.add_category_dialog, null);

        final TextView NewCattv=(TextView) mView.findViewById(R.id.new_cat_title);
        Button Ok=(Button) mView.findViewById(R.id.DialogOk);
        Button Cancel=(Button) mView.findViewById(R.id.DialogCancel);
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NewCattv.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Main Activity Title Can't be Empty",Toast.LENGTH_LONG).show();

                }else{
                    try{
                        db.mainActivityDao().insertAll(new MainActivity(NewCattv.getText().toString()));
                        Toast.makeText(getApplicationContext(),"New Main Activity added Successfully",Toast.LENGTH_LONG).show();
                        AddParentDialog.dismiss();
                        AddParentDialog.cancel();
                        PopulateData();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Failed to Add",Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddParentDialog.dismiss();
                AddParentDialog.cancel();
            }
        });

        mBuilder.setView(mView);
        AddParentDialog = mBuilder.create();
        AddParentDialog.show();
    }

    public void OpenAddChildDialog(MainActivity cat){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.add_category_dialog, null);
        TextView DialogTitle=(TextView)mView.findViewById(R.id.dialog_title);
        final TextView NewCattv=(TextView) mView.findViewById(R.id.new_cat_title);
        final int CatId=cat.getId();
        Button Ok=(Button) mView.findViewById(R.id.DialogOk);
        Button Cancel=(Button) mView.findViewById(R.id.DialogCancel);
        DialogTitle.setText("Adding Sub Activity Item to "+cat.getMainActivityName()+" ");
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NewCattv.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Item Title Can't be Empty",Toast.LENGTH_LONG).show();

                }else{
                    try{
                        db.subActivityDao().insertAll(new SubActivity(CatId,NewCattv.getText().toString()));
                        Toast.makeText(getApplicationContext(),"New Sub Activity Item added Successfully",Toast.LENGTH_LONG).show();
                        AddChildDialog.dismiss();
                        AddChildDialog.cancel();
                        PopulateData();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Failed to Add",Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddChildDialog.dismiss();
                AddChildDialog.cancel();
            }
        });

        mBuilder.setView(mView);
        AddChildDialog = mBuilder.create();
        AddChildDialog.show();

    }

    public void OpenEditParentDialog( MainActivity cat){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.add_category_dialog, null);
        TextView DialogTitle=(TextView)mView.findViewById(R.id.dialog_title);
        final TextView NewCattv=(TextView) mView.findViewById(R.id.new_cat_title);
        Button Ok=(Button) mView.findViewById(R.id.DialogOk);
        Button Cancel=(Button) mView.findViewById(R.id.DialogCancel);
        DialogTitle.setText("Editing Main Activity");
        NewCattv.setText(cat.getMainActivityName().toString());
        final MainActivity MainActivityToUpdate=cat;
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NewCattv.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Main Activity Name Can't be Empty",Toast.LENGTH_LONG).show();

                }else{
                    try{
                        MainActivityToUpdate.setMainActivityName(NewCattv.getText().toString());
                        db.mainActivityDao().update(MainActivityToUpdate);
                        Toast.makeText(getApplicationContext(),"Main Activity Edited Successfully",Toast.LENGTH_LONG).show();
                        EditParentDialog.dismiss();
                        EditParentDialog.cancel();
                        PopulateData();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Failed to Edit",Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditParentDialog.dismiss();
                EditParentDialog.cancel();
            }
        });

        mBuilder.setView(mView);
        EditParentDialog = mBuilder.create();
        EditParentDialog.show();
    }

    public void OpenEditChildDialog( SubActivity sub){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.add_category_dialog, null);
        TextView DialogTitle=(TextView)mView.findViewById(R.id.dialog_title);
        final TextView NewCattv=(TextView) mView.findViewById(R.id.new_cat_title);
        Button Ok=(Button) mView.findViewById(R.id.DialogOk);
        Button Cancel=(Button) mView.findViewById(R.id.DialogCancel);
        DialogTitle.setText("Editing Sub Activity");
        NewCattv.setText(sub.getSubActivityName().toString());
        final SubActivity SubActivityToUpdate=sub;
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NewCattv.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Sub Activity Name Can't be Empty",Toast.LENGTH_LONG).show();

                }else{
                    try{
                        SubActivityToUpdate.setSubActivityName(NewCattv.getText().toString());
                        db.subActivityDao().update(SubActivityToUpdate);
                        Toast.makeText(getApplicationContext(),"Sub Activity Edited Successfully",Toast.LENGTH_LONG).show();
                        EditChildtDialog.dismiss();
                        EditChildtDialog.cancel();
                        PopulateData();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Failed to Edit",Toast.LENGTH_LONG).show();

                    }
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditChildtDialog.dismiss();
                EditChildtDialog.cancel();
            }
        });

        mBuilder.setView(mView);
        EditChildtDialog = mBuilder.create();
        EditChildtDialog.show();
    }


    private  void PopulateData(){

        MyData=GetSubCatHash();
        AllCategory=db.mainActivityDao().getAllmainActivities();
        myExpandableAdapter=new MyExpandableAdapter(this,AllCategory,MyData,MainActivityActionStatus,this);
        expandableListView.setAdapter(myExpandableAdapter);
        myExpandableAdapter.notifyDataSetChanged();
        if(MyData.isEmpty()){
            Toast.makeText(getApplicationContext(),"Database Is Empty",Toast.LENGTH_LONG).show();

        }

    }

    public HashMap<String,List<SubActivity>> GetSubCatHash(){
        HashMap<String,List<SubActivity>> MyDataHashMap= new HashMap<String, List<SubActivity>>();;
        List<MainActivity> MyCategries=db.mainActivityDao().getAllmainActivities();
        for (MainActivity cat:MyCategries){
            List<SubActivity> child_items=db.subActivityDao().GetAllRelatedSubActivities(cat.getId());
            MyDataHashMap.put(cat.getMainActivityName(),child_items);

        }
        return MyDataHashMap;
    }


    @Override
    public void ManipulateAction(int type, int group, int child) {
        /*
        this method is to determine the action that should be done on the Expandable list
        type: the type of action
             1:edit MainActivity
             2:edit SubActivity
             3:Delete MainActivity
             4:Delete Subactivity
             5:Add new SubActivity to a MainActivity
         group: the group item's position which was clicked
         child: the child item's position which was clicked
                if the action was performed on group item only , the child will be always Zero 0, that happens when action type is 1 or 3

         */

        switch (type){
            case 1:

                OpenEditParentDialog(AllCategory.get(group));

                break;

            case 2:

                OpenEditChildDialog(MyData.get(AllCategory.get(group).getMainActivityName()).get(child));

                break;
            case 3:

                try {
                    db.mainActivityDao().delete(AllCategory.get(group));
                    PopulateData();

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
                break;

            case 4 :

                try {
                    db.subActivityDao().delete(MyData.get(AllCategory.get(group).getMainActivityName()).get(child));
                    PopulateData();

                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
                break;

            case 5:

                OpenAddChildDialog(AllCategory.get(group));

                break;

        }
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

}
