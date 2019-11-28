package com.hackerman.dcalender.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hackerman.dcalender.R;
import com.hackerman.dcalender.database.entity.Template;

import java.util.List;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    List<Template> templates;

    public TemplateAdapter(List<Template> templates) {
        this.templates = templates;
    }

    @NonNull
    @Override
    public TemplateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateAdapter.ViewHolder holder, int position) {
        holder.mainActivity.setText(templates.get(position).getMainActivity());
        holder.subActivity.setText(templates.get(position).getSubActivity());
        ;
    }

    @Override
    public int getItemCount() {
        return templates.size();
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
