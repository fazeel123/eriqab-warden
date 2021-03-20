package com.example.e_riqabpemantauan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.model.SpecialCaseModel;
import com.example.e_riqabpemantauan.ui.SpecialCase;

import java.util.List;

public class SpecialCaseCoachListAdapter extends RecyclerView.Adapter<SpecialCaseCoachListAdapter.SpecialCaseViewHolder> {

    private Context context;
    private List<SpecialCaseModel> specialCaseModels;
    private SpecialCaseModel list;

    public SpecialCaseCoachListAdapter(Context context, List<SpecialCaseModel> specialCaseModels) {
        this.context = context;
        this.specialCaseModels = specialCaseModels;
    }

    @NonNull
    @Override
    public SpecialCaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.special_case_coach_list, parent, false);
        return new SpecialCaseCoachListAdapter.SpecialCaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialCaseViewHolder holder, int position) {
        list = specialCaseModels.get(position);
        int var = list.getId();
        String wName = list.getCoachName();

        holder.coachName.setText(list.getCoachName().toUpperCase());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SpecialCase.class);
                intent.putExtra("id", var);
                intent.putExtra("warden", wName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return specialCaseModels == null ? 0 : specialCaseModels.size();
    }


    public class SpecialCaseViewHolder extends RecyclerView.ViewHolder {

        TextView coachName;

        public SpecialCaseViewHolder(@NonNull View itemView) {
            super(itemView);

            coachName = itemView.findViewById(R.id.CoachName);
        }
    }
}
