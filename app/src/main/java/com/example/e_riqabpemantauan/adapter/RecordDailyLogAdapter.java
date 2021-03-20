package com.example.e_riqabpemantauan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.model.RecordDailyLogModel;

import java.util.List;

public class RecordDailyLogAdapter extends RecyclerView.Adapter<RecordDailyLogAdapter.RecordsViewHolder> {

    private Context context;
    private List<RecordDailyLogModel> recordDailyLogModels;
    private RecordDailyLogModel list;

    public RecordDailyLogAdapter(Context context, List<RecordDailyLogModel> recordDailyLogModels) {
        this.context = context;
        this.recordDailyLogModels = recordDailyLogModels;
    }

    @NonNull
    @Override
    public RecordDailyLogAdapter.RecordsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.record_daily_log, parent, false);
        return new RecordsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordDailyLogAdapter.RecordsViewHolder holder, int position) {
        list = recordDailyLogModels.get(position);

        holder.time.setText(list.getTime());
        holder.activity.setText(list.getActivity());
    }

    @Override
    public int getItemCount() {
        return recordDailyLogModels == null ? 0 : recordDailyLogModels.size();
    }

    public class RecordsViewHolder extends RecyclerView.ViewHolder {

        TextView time, activity;

        public RecordsViewHolder(@NonNull View itemView) {
            super(itemView);

            time = itemView.findViewById(R.id.time);
            activity = itemView.findViewById(R.id.activity);
        }
    }
}
