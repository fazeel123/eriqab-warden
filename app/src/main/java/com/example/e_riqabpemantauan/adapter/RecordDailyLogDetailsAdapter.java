package com.example.e_riqabpemantauan.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.model.RecordDailyLogDetailsModel;
import com.example.e_riqabpemantauan.model.RecordDailyLogModel;
import com.example.e_riqabpemantauan.ui.RecordDailyLogDetails;
import com.example.e_riqabpemantauan.ui.RecordDailyLogList;

import java.util.List;

public class RecordDailyLogDetailsAdapter extends RecyclerView.Adapter<RecordDailyLogDetailsAdapter.RecordsDetailsViewHolder> {

    private Context context;
    private List<RecordDailyLogDetailsModel> recordDetailsModel;
//    private RecordDailyLogDetailsModel list;

    public RecordDailyLogDetailsAdapter(Context context, List<RecordDailyLogDetailsModel> recordDetailsModels) {
        this.context = context;
        this.recordDetailsModel = recordDetailsModels;
    }

    @NonNull
    @Override
    public RecordDailyLogDetailsAdapter.RecordsDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.record_daily_log_details, parent, false);
        return new RecordsDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsDetailsViewHolder holder, int position) {

        RecordDailyLogDetailsModel list = recordDetailsModel.get(position);

        Log.i("PARAM", "Get Time: " + list.getTime());
        Log.i("PARAM", "Get Title: " + list.getTitle());

        holder.checkBox.setText(list.getTitle());

        holder.checkBox.setChecked(list.isCheckActivity());
        holder.checkBox.setTag(position);

        holder.checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(buttonView.isChecked()) {
                buttonView.setChecked(true);
                list.setCheckActivity(true);
                list.setActivityName(list.getTitle());

                RecordDailyLogDetails logDetails = (RecordDailyLogDetails) context;
                logDetails.checkButton(position, list.getActivityName());
                Log.i("CHECK", "ADAPTER CHECK: " + list.getActivityName());

            } else {
                buttonView.setChecked(false);
                list.setCheckActivity(false);
                list.setActivityName(list.getTitle());
                RecordDailyLogDetails logDetails = (RecordDailyLogDetails) context;
                logDetails.checkButton(position, list.getActivityName());
                Log.i("CHECK", "ADAPTER CHECK: " + list.getActivityName());
            }
        }));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecordDailyLogList.class);
//                intent.putExtra("id", var);
                intent.putExtra("time", list.getTime());
                intent.putExtra("activity", list.getTitle());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordDetailsModel == null ? 0 : recordDetailsModel.size();
    }

    public static class RecordsDetailsViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public RecordsDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
