package com.example.e_riqabpemantauan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.model.SpecialCaseModel;

import java.util.List;

public class SpecialCaseListAdapter extends RecyclerView.Adapter<SpecialCaseListAdapter.SpecialCaseListViewHolder> {

    private Context context;
    private List<SpecialCaseModel> specialCaseModels;
    private SpecialCaseModel list;

    public SpecialCaseListAdapter(Context context, List<SpecialCaseModel> specialCaseModels) {
        this.context = context;
        this.specialCaseModels = specialCaseModels;
    }

    @NonNull
    @Override
    public SpecialCaseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.special_case_list, parent, false);
        return new SpecialCaseListAdapter.SpecialCaseListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialCaseListViewHolder holder, int position) {

        list = specialCaseModels.get(position);

        holder.coachN.setText(list.getmCoachName());
        holder.wardenN.setText(list.getmWardenName());
        holder.caseType.setText(list.getmCaseType());
        holder.date.setText(list.getmDate());
        holder.time.setText(list.getmTime());
        holder.location.setText(list.getmLocation());
    }

    @Override
    public int getItemCount() {
        return specialCaseModels == null ? 0 : specialCaseModels.size();
    }


    public class SpecialCaseListViewHolder extends RecyclerView.ViewHolder {

        TextView coachN, wardenN, caseType, date, time, location;

        public SpecialCaseListViewHolder(@NonNull View itemView) {
            super(itemView);

            coachN = itemView.findViewById(R.id.coachName);
            wardenN = itemView.findViewById(R.id.wardenName);
            caseType = itemView.findViewById(R.id.caseType);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            location = itemView.findViewById(R.id.location);
        }
    }
}
