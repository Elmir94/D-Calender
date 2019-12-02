package com.hackerman.dcalender.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackerman.dcalender.R;
import com.hackerman.dcalender.database.entity.SubActivity;
import com.hackerman.dcalender.database.entity.MainActivity;

import java.util.HashMap;
import java.util.List;

public class MyExpandableAdapter extends BaseExpandableListAdapter {

    private List<MainActivity> parent_items;
    private HashMap<String,List<SubActivity>> child_items;
    private Context ctx;
    private boolean groupEditeActivated;

    private MyListManipulate myListManipulate;


    public interface MyListManipulate{
        void ManipulateAction(int type, int group, int child);
    }


    public MyExpandableAdapter(Context ctx, List<MainActivity> parent_items, HashMap<String, List<SubActivity>> child_items,boolean groupEditeActivated,MyListManipulate actionCallback) {
        this.ctx = ctx;
        this.parent_items = parent_items;
        this.child_items = child_items;
        this.groupEditeActivated=groupEditeActivated;
        myListManipulate=actionCallback;
        System.out.println("Adapter Called");

    }

    @Override
    public int getGroupCount() {
        System.out.println("getGroupCount Called");
        return parent_items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        System.out.println("getChildrenCount Called :"+this.child_items.get(this.parent_items.get(groupPosition).getMainActivityName()).size());
        return this.child_items.get(this.parent_items.get(groupPosition).getMainActivityName()).size();
    }

    @Override
    public MainActivity getGroup(int groupPosition) {
        return this.parent_items.get(groupPosition);
    }

    @Override
    public SubActivity getChild(int groupPosition, int childPosition) {
        return this.child_items.get(this.parent_items.get(groupPosition).getMainActivityName()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent ) {
        System.out.println("getGroupView Called");
        String Title=(String )getGroup(groupPosition).getMainActivityName();
        if(convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.patent_cat,null);
        }
        TextView tv=(TextView) convertView.findViewById(R.id.parent_cat_title);
        ImageView DeleteImg=(ImageView)convertView.findViewById(R.id.groupdelete);
        ImageView UpdateImg=(ImageView)convertView.findViewById(R.id.groupupdate);
        final Context ctx=this.ctx;
        final int grp=groupPosition;

        if(groupEditeActivated){
            DeleteImg.setVisibility(View.VISIBLE);
            UpdateImg.setVisibility(View.VISIBLE);
        }else{
            DeleteImg.setVisibility(View.VISIBLE);
            UpdateImg.setVisibility(View.VISIBLE);
        }
        tv.setText(Title);

        UpdateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListManipulate.ManipulateAction(1,grp,0);;

            }
        });

        DeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListManipulate.ManipulateAction(3,grp,0);

            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        System.out.println("getChildView Called");
        String Title=(String )getChild(groupPosition,childPosition).getSubActivityName();
        if(convertView==null){
            LayoutInflater layoutInflater=(LayoutInflater)this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.child_cat,null);
        }
        TextView tv=(TextView) convertView.findViewById(R.id.child_cat_title);
        tv.setText(Title);

        final Context ctx=this.ctx;
        final int grp=groupPosition;
        final int child=childPosition;
        //ImageView AddNewChild=(ImageView) convertView.findViewById(R.id.AddNewChild);
        ImageView DeleteImg=(ImageView)convertView.findViewById(R.id.childdelete);
        ImageView UpdateImg=(ImageView)convertView.findViewById(R.id.childupdate);
//        if(childPosition==(getChildrenCount(groupPosition)-1)){
//            AddNewChild.setVisibility(View.VISIBLE);
//        }else{
//            AddNewChild.setVisibility(View.GONE);
//        }

        UpdateImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListManipulate.ManipulateAction(2,grp,child);
            }
        });

        DeleteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myListManipulate.ManipulateAction(4,grp,child);

            }
        });

//        AddNewChild.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myListManipulate.ManipulateAction(5,grp,child);
//            }
//        });


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
