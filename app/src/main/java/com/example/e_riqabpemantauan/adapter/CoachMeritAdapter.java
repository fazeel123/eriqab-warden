package com.example.e_riqabpemantauan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.model.CoachMeritModel;
import com.example.e_riqabpemantauan.ui.CoachMeritDetails;

import java.util.ArrayList;
import java.util.List;

public class CoachMeritAdapter extends RecyclerView.Adapter<CoachMeritAdapter.CoachViewHolder> {

    private Context context;
    private List<CoachMeritModel> coachMerit;
    private CoachMeritModel list;

    public CoachMeritAdapter(Context context, List<CoachMeritModel> coachMerit) {
        this.context = context;
        this.coachMerit = coachMerit;
    }

    @NonNull
    @Override
    public CoachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coach_merit, parent, false);
        return new CoachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoachMeritAdapter.CoachViewHolder holder, int position) {
        list = coachMerit.get(position);
        int var = list.getId();

        holder.title.setText(list.getName().toUpperCase());
        holder.merit.setText(Integer.toString(list.getTotalMerit()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CoachMeritDetails.class);
                intent.putExtra("id", var);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coachMerit == null ? 0 : coachMerit.size();
    }


    public class CoachViewHolder extends RecyclerView.ViewHolder {

        TextView title, merit;

        public CoachViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            merit = itemView.findViewById(R.id.totalMerit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent detailIntent = new Intent(v.getContext(), CoachMeritDetails.class);
                    detailIntent.putExtra("id", 1 + position);
                    v.getContext().startActivity(detailIntent);
                }
            });
        }
    }

    public void setSearchOperation(List<CoachMeritModel> newList) {
        coachMerit = new ArrayList<>();
        coachMerit.addAll(newList);
        notifyDataSetChanged();
    }

}
