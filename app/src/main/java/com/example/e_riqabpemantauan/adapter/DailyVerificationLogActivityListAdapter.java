package com.example.e_riqabpemantauan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.model.DailyVerificationLogActivityListModel;

import java.util.List;

public class DailyVerificationLogActivityListAdapter extends RecyclerView.Adapter<DailyVerificationLogActivityListAdapter.DailyLogsViewHolder> {

    private Context context;
    private List<DailyVerificationLogActivityListModel> dailyVerificationLogActivityList;
    private DailyVerificationLogActivityListModel list;

    public DailyVerificationLogActivityListAdapter(Context context, List<DailyVerificationLogActivityListModel> dailyVerificationLogActivityList) {
        this.context = context;
        this.dailyVerificationLogActivityList = dailyVerificationLogActivityList;
    }

    @NonNull
    @Override
    public DailyVerificationLogActivityListAdapter.DailyLogsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_verification_log_activity_list, parent, false);
        return new DailyLogsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyVerificationLogActivityListAdapter.DailyLogsViewHolder holder, int position) {
        list = dailyVerificationLogActivityList.get(position);

        holder.mtime.setText(list.getTime());
        holder.activity1.setText(list.getActivity());
    }

    @Override
    public int getItemCount() {
        return dailyVerificationLogActivityList == null ? 0 : dailyVerificationLogActivityList.size();
    }

    public class DailyLogsViewHolder extends RecyclerView.ViewHolder {

        TextView mtime, activity1;

        public DailyLogsViewHolder(@NonNull View itemView) {
            super(itemView);

            mtime = itemView.findViewById(R.id.time);
            activity1 = itemView.findViewById(R.id.activity);
        }
    }
}
