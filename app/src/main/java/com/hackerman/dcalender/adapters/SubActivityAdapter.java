package com.hackerman.dcalender.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackerman.dcalender.R;
import com.hackerman.dcalender.database.entity.SubActivity;

import java.util.List;

public class SubActivityAdapter extends RecyclerView.Adapter<SubActivityAdapter.ViewHolder> {
    List<SubActivity> subActivityes;

    public SubActivityAdapter(List<SubActivity> subActivityes) {
        this.subActivityes = subActivityes;
    }

    @NonNull
    @Override
    public SubActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_activity_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubActivityAdapter.ViewHolder holder, int position) {
        holder.mainActivity.setText(subActivityes.get(position).getMainActivityName());
        holder.subActivity.setText(subActivityes.get(position).getSubActivityName());
    }

    @Override
    public int getItemCount() {
        return subActivityes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mainActivity;
        public TextView subActivity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mainActivity = itemView.findViewById(R.id.mainActivityTextView);
            this.subActivity =  itemView.findViewById(R.id.subActivityTextView);
        }
    }
}
