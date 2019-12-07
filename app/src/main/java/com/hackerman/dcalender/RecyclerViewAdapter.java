package com.hackerman.dcalender;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 2/12/2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<Date> dates1 = new ArrayList<>();
    private ArrayList<Date> dates2 = new ArrayList<>();
    private Context Context;

    public RecyclerViewAdapter(Context context, ArrayList<Date> dates1, ArrayList<Date> dates2) {
        this.dates1 = dates1;
        this.dates2 = dates2;
        this.Context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_container, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        holder.weekday1.setText(DateFormat.format("EEEE", dates1.get(position)));
        holder.date1.setText(DateFormat.format("dd MMMM", dates1.get(position)));
        holder.weekday2.setText(DateFormat.format("EEEE", dates2.get(position)));
        holder.date2.setText(DateFormat.format("dd MMMM", dates2.get(position)));
    }

    @Override
    public int getItemCount() {
        return dates1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView weekday1;
        TextView date1;
        TextView weekday2;
        TextView date2;

        public ViewHolder(View itemView) {
            super(itemView);
            weekday1 = itemView.findViewById(R.id.weekday1);
            date1 = itemView.findViewById(R.id.date1);
            weekday2 = itemView.findViewById(R.id.weekday2);
            date2 = itemView.findViewById(R.id.date2);
        }
    }
}