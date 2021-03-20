package com.example.e_riqabpemantauan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.adapter.RecordDailyLogAdapter;
import com.example.e_riqabpemantauan.model.RecordDailyLogDetailsModel;
import com.example.e_riqabpemantauan.model.RecordDailyLogModel;
import com.example.e_riqabpemantauan.utils.RestAPI;
import com.example.e_riqabpemantauan.utils.SharedPrefsManager;
import com.example.e_riqabpemantauan.utils.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecordDailyLog extends AppCompatActivity {

    private static final String TAG = "";
    private String storeToken;
    private RecordDailyLogModel recordDailyLogModel;
    private RecyclerView recyclerView;
    private RecordDailyLogAdapter adapter;
    private List<RecordDailyLogModel> lists;
    private Spinner time;
    private String[] timeList = {"01:00:00 am", "02:00:00 am", "03:00:00 am", "04:00:00 am", "05:00:00 am", "06:00:00 am", "07:00:00 am",
                                 "08:00:00 am", "09:00:00 am", "10:00:00 am", "11:00:00 am", "12:00:00 pm", "13:00:00 pm", "14:00:00 pm",
                                 "15:00:00 pm", "16:00:00 pm", "17:00:00 pm", "18:00:00 pm", "19:00:00 pm", "20:00:00 pm", "21:00:00 pm",
                                 "22:00:00 pm", "23:00:00 pm", "24:00:00 am"};

    private RecordDailyLogDetailsModel model;
    private TextView personName;

    private int hours = 24;
    private String temp[];

    int LAUNCH_SECOND_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_daily_log);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Log Harian");

        time = (Spinner) findViewById(R.id.spinner);
        personName = (TextView) findViewById(R.id.personName);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        model = new RecordDailyLogDetailsModel();
        lists = new ArrayList<>();
        setupMenuRecyclerView();
        fetchPersonDetails();
        fetchDetails();

        // Spinner Dropdown for Location
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this,R.layout.support_simple_spinner_dropdown_item, timeList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        time.setAdapter(adapter);

        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String locate = RecordDailyLog.this.time.getItemAtPosition(RecordDailyLog.this.time.getSelectedItemPosition()).toString();
                model.setAdd_time(locate);
                //Toast.makeText(getApplicationContext(), locate, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newActivitySlots();
            }
        });
    }

    private void setupMenuRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecordDailyLogAdapter(this, lists);
        recyclerView.setAdapter(adapter);
    }

    // Method for GET the name of Warden
    private void fetchPersonDetails() {

        StringRequest request = new StringRequest(Request.Method.GET, RestAPI.GetWardenName, detailPersonListener, errorPersonListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailPersonListener = response ->
    {
        Log.i("VOLLEY", "response" + response);
        try {
            JSONObject responseObj = new JSONObject(response);
            JSONObject jsonResponse = responseObj.optJSONObject("data");

            String name = jsonResponse.optString("warden_name");

            recordDailyLogModel = new RecordDailyLogModel();
            recordDailyLogModel.getPersonName();

            personName.setText(name.toUpperCase());

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    };

    Response.ErrorListener errorPersonListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
    };
    // End for GET the name of Warden

    private void fetchDetails() {

        StringRequest request = new StringRequest(Request.Method.POST, RestAPI.GetDailyLogDetails, detailListener, errorListener) {

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("date", date);
                params.put("shift", String.valueOf(2));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailListener = response ->
    {
        Log.i("VOLLEY", "response" + response);
        try{
            JSONObject responseObj = new JSONObject(response);
            JSONArray array = responseObj.optJSONArray("data");
            int length = array.length();

            for(int i = 0; i < length; i++) {
                JSONObject logObj = array.optJSONObject(i);
                int id = logObj.optInt("id");
                String time = logObj.optString("time");
                String activity = logObj.optString("activity");

                String temp = activity.replaceAll("\\[\"", "").replaceAll("\",\"", ", ").replaceAll("\\\"]", "");

                recordDailyLogModel = new RecordDailyLogModel();
                recordDailyLogModel.setId(id);
                recordDailyLogModel.setTime(time.substring(0, 5));
                recordDailyLogModel.setActivity(temp.substring(0, temp.length() - 0));
//                recordDailyLogModel.setActivity(activity);
                lists.add(recordDailyLogModel);
                adapter.notifyItemInserted(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    Response.ErrorListener errorListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
    };

    private void newActivitySlots() {
        Intent intent = new Intent(this, RecordDailyLogDetails.class);
        intent.putExtra("time", model.getAdd_time().toString());
        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent = new Intent(RecordDailyLog.this, HomePage.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
