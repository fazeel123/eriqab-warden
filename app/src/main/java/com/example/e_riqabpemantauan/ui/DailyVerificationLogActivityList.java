package com.example.e_riqabpemantauan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.adapter.DailyVerificationLogActivityListAdapter;
import com.example.e_riqabpemantauan.model.DailyVerificationLogActivityListModel;
import com.example.e_riqabpemantauan.utils.RestAPI;
import com.example.e_riqabpemantauan.utils.SharedPrefsManager;
import com.example.e_riqabpemantauan.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyVerificationLogActivityList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DailyVerificationLogActivityListAdapter adapter;
    private List<DailyVerificationLogActivityListModel> lists;
    private DailyVerificationLogActivityListModel activityList;
    private String storeToken;
    private int logId;

    private Button submit;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_verification_log_activity_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Log Harian");

        submit = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        logId = getIntent().getIntExtra("id", 0);
//        Toast.makeText(getApplicationContext(), String.valueOf(logId), Toast.LENGTH_LONG).show();

        lists = new ArrayList<>();
        setupMenuRecyclerView();
        fetchDetails();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approveDetails();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCancel();
            }
        });
    }

    private void setupMenuRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DailyVerificationLogActivityListAdapter(this, lists);
        recyclerView.setAdapter(adapter);
    }

    private void fetchDetails() {

        String URL = RestAPI.DailyVerificationLogActiviyList + logId;

        StringRequest request = new StringRequest(Request.Method.GET, URL, detailLoadListener, errorLoadListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json; charset=UTF-8 ");
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailLoadListener = response ->
    {
        Log.i("VOLLEY", "response" + response);
        try{
            JSONObject responseObj = new JSONObject(response);
            JSONArray array = responseObj.optJSONArray("data");
            int length = array.length();

            for(int i = 0; i < length; i++) {
                JSONObject activityObj = array.optJSONObject(i);
                int id = activityObj.optInt("id");
                String time = activityObj.optString("time");
                String act = activityObj.optString("activity");
                String note = activityObj.optString("note");
                String image = activityObj.optString("picture");

                String temp = act.replaceAll("\\[\"", "").replaceAll("\",\"", ", ").replaceAll("\\\"]", "");

                activityList = new DailyVerificationLogActivityListModel();
                activityList.setId(id);
                activityList.setTime(time.substring(0, 5));
                activityList.setActivity(temp);
                activityList.setNote(note);
                activityList.setImage(image);
                lists.add(activityList);
                adapter.notifyItemInserted(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    Response.ErrorListener errorLoadListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
    };

    private void approveDetails() {

        Log.i("PARAM", "log_id: " + logId);

        String URL = RestAPI.DailyVerificationLogApprove + logId;

        StringRequest request = new StringRequest(Request.Method.POST, URL, detailApproveListener, errorApproveListener) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("log_id", String.valueOf(logId));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailApproveListener = response ->
    {
        Log.i("VOLLEY", "response" + response);

        Intent intent = new Intent(DailyVerificationLogActivityList.this, DailyVerificationLog.class);
        intent.putExtra("id", logId);
        startActivity(intent);

    };

    Response.ErrorListener errorApproveListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent = new Intent(DailyVerificationLogActivityList.this, DailyVerificationLog.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchCancel() {
        Intent intent = new Intent(this, DailyVerificationLogActivityDisapprove.class);
        intent.putExtra("id", logId);
        startActivity(intent);
    }
}