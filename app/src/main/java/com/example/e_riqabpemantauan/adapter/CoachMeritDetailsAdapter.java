package com.example.e_riqabpemantauan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.model.CoachMeritDetailsModel;

import java.util.List;

public class CoachMeritDetailsAdapter extends RecyclerView.Adapter<CoachMeritDetailsAdapter.CoachViewHolder> {

    private Context context;
    private List<CoachMeritDetailsModel> coachMeritDetails;
    private CoachMeritDetailsModel list;

    public CoachMeritDetailsAdapter(Context context, List<CoachMeritDetailsModel> coachMeritDetails) {
        this.context = context;
        this.coachMeritDetails = coachMeritDetails;
    }

    @NonNull
    @Override
    public CoachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coach_merit_details, parent, false);
        return new CoachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoachMeritDetailsAdapter.CoachViewHolder holder, int position) {
        list = coachMeritDetails.get(position);

        holder.time.setText(list.getDate());
        holder.details.setText(list.getNote());
        if(list.getType().equals("Amal Baik")) {
            holder.totalMerit.setTextColor(Color.parseColor("#24B14B"));
            holder.totalMerit.setText("+" + list.getMerit());
        } else if(list.getType().equals("Kesalahan")) {
            holder.totalMerit.setTextColor(Color.RED);
            holder.totalMerit.setText("-" + list.getMerit());
        }

    }

    @Override
    public int getItemCount() {
        return coachMeritDetails == null ? 0 : coachMeritDetails.size();
    }

    public class CoachViewHolder extends RecyclerView.ViewHolder {

        TextView time, details, totalMerit;

        public CoachViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            details = itemView.findViewById(R.id.activity);
            totalMerit = itemView.findViewById(R.id.merit);
        }
    }
}


