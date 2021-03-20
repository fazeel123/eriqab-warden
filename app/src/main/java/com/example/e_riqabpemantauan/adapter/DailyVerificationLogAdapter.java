package com.example.e_riqabpemantauan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_riqabpemantauan.ui.DailyVerificationLogActivityList;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.model.DailyVerificationLogModel;

import java.util.List;

public class DailyVerificationLogAdapter extends RecyclerView.Adapter<DailyVerificationLogAdapter.DailyVerificationLogViewHolder> {

    private Context context;
    private List<DailyVerificationLogModel> lists;
    private DailyVerificationLogModel list;

    public DailyVerificationLogAdapter(Context context, List<DailyVerificationLogModel>list) {
        this.context = context;
        this.lists = list;
    }

    @NonNull
    @Override
    public DailyVerificationLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.daily_verification_log, parent, false);
        return new DailyVerificationLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyVerificationLogViewHolder holder, int position) {

        list = lists.get(position);
        int var = list.getId();

        holder.textViewDate.setText(list.getDate());
        holder.textViewShift.setText(list.getShift());

        if(list.getWardenOneId() == "null") {
            holder.img1.setImageResource(R.drawable.ic_pause_circle_filled);
        } else if(list.getWardenOneId() == "0") {
            holder.img1.setImageResource(R.drawable.ic_cancel);
        } else {
            holder.img1.setImageResource(R.drawable.ic_check_circle);
        }

        if(list.getGetWardenTwoId() == "null") {
            holder.img2.setImageResource(R.drawable.ic_pause_circle_filled);
        } else if(list.getGetWardenTwoId() == "0") {
            holder.img2.setImageResource(R.drawable.ic_cancel);
        } else {
            holder.img2.setImageResource(R.drawable.ic_check_circle);
        }

        if(list.getHourseRulerId() == "null") {
            holder.img3.setImageResource(R.drawable.ic_pause_circle_filled);
        } else if(list.getHourseRulerId() == "0") {
            holder.img3.setImageResource(R.drawable.ic_cancel);
        } else {
            holder.img3.setImageResource(R.drawable.ic_check_circle);
        }

        if(list.getExecutiveId() == "null") {
            holder.img4.setImageResource(R.drawable.ic_pause_circle_filled);
        } else if(list.getExecutiveId() == "0") {
            holder.img4.setImageResource(R.drawable.ic_cancel);
        } else {
            holder.img4.setImageResource(R.drawable.ic_check_circle);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DailyVerificationLogActivityList.class);
                intent.putExtra("id", var);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists == null ? 0 : lists.size();
    }

    public class DailyVerificationLogViewHolder extends RecyclerView.ViewHolder {

        TextView textViewShift, textViewDate;
        ImageView img1, img2, img3, img4;

        public DailyVerificationLogViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDate = itemView.findViewById(R.id.time);
            textViewShift = itemView.findViewById(R.id.shift);
            img1 = itemView.findViewById(R.id.imageView1);
            img2 = itemView.findViewById(R.id.imageView2);
            img3 = itemView.findViewById(R.id.imageView3);
            img4 = itemView.findViewById(R.id.imageView4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position =getAdapterPosition();
                    Intent detailIntent = new Intent(v.getContext(), DailyVerificationLogActivityList.class);
                    detailIntent.putExtra("id", position + 1);
                    v.getContext().startActivity(detailIntent);
                }
            });
        }
    }

}
