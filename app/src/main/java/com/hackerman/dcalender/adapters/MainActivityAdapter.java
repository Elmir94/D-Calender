package com.hackerman.dcalender.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackerman.dcalender.R;
import com.hackerman.dcalender.database.entity.MainActivity;

import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    List<MainActivity> mainActivityes;

    public MainActivityAdapter(List<MainActivity> mainActivityes) {
        this.mainActivityes = mainActivityes;
    }

    @NonNull
    @Override
    public MainActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_activity_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityAdapter.ViewHolder holder, int position) {
        holder.mainActivity.setText(mainActivityes.get(position).getMainActivityName());
    }

    @Override
    public int getItemCount() {
        return mainActivityes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mainActivity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mainActivity = itemView.findViewById(R.id.mainActivityTextView);
        }
    }
}
