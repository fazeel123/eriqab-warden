package com.example.e_riqabpemantauan.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.adapter.RecordDailyLogDetailsAdapter;
import com.example.e_riqabpemantauan.model.RecordDailyLogDetailsModel;
import com.example.e_riqabpemantauan.utils.RestAPI;
import com.example.e_riqabpemantauan.utils.SharedPrefsManager;
import com.example.e_riqabpemantauan.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecordDailyLogDetails extends AppCompatActivity {

    private static final String TAG = "";
    private RecyclerView recyclerView;
    private RecordDailyLogDetailsAdapter adapter;
    private List<RecordDailyLogDetailsModel> lists = new ArrayList<>();
    private RecordDailyLogDetailsModel recordDetailsList;
    private String storeToken;
    private String time;
    private String date;
    private Button submit;
    private CheckBox check;
    private TextView timeBox;
    int LAUNCH_SECOND_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_daily_log_details);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Log Harian");

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        recordDetailsList = new RecordDailyLogDetailsModel();

        time = getIntent().getStringExtra("time");

        recordDetailsList.setTimeActivity(time);
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        recordDetailsList.setDateActivity(date);

        submit = (Button) findViewById(R.id.submit);
        check = (CheckBox) findViewById(R.id.checkBox);
        timeBox = (TextView) findViewById(R.id.time);


        timeBox.setText(time);

//        lists = new ArrayList<>();
        setupMenuRecyclerView();
        fetchDetails();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateActivity();
            }
        });
    }

    private void setupMenuRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecordDailyLogDetailsAdapter(this, lists);
        recyclerView.setAdapter(adapter);
    }

    // Method for GET Activity List
    private void fetchDetails() {

        StringRequest request = new StringRequest(Request.Method.GET, RestAPI.PreListActivities, detailListener, errorListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + storeToken);
                return headers;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailListener = response ->
    {
        Log.i("VOLLEY", "response" + response);

        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray array = responseObj.optJSONArray("data");
            int length = array.length();

            for(int i = 0; i < length; i++) {

                JSONObject valueObj = array.getJSONObject(i);
//                String gtime = valueObj.optString(time.substring(0, 8));
                JSONArray innerArray = valueObj.optJSONArray(time.substring(0, 8));
                for(int j = 0; j < innerArray.length(); j++) {

                    JSONObject jsonObject = innerArray.getJSONObject(j);
                    String mtime = jsonObject.optString("time");
                    String mtitle = jsonObject.optString("title");

//                    Log.i("PARAM", "Get Time: " + recordDetailsList.getTime());
//                    Log.i("PARAM", "Get Title: " + recordDetailsList.getTitle());

//                    Toast.makeText(getApplicationContext(), "Main Time: " + mtime + " Activity: " + mtitle , Toast.LENGTH_LONG).show();

                    recordDetailsList = new RecordDailyLogDetailsModel();
                    recordDetailsList.setTime(mtime);
                    recordDetailsList.setTitle(mtitle.toUpperCase());
                    lists.add(recordDetailsList);
                    adapter.notifyItemInserted(i);
                }
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
    // End Method for Activity List

    // Method for Create Activity
//    private void CreateActivity() {
//
//        JSONObject object = createParamsToJSON();
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, RestAPI.CreateActivity, object, detailActivityListener, errorActivityListener) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer " + storeToken);
//                return params;
//            }
//        };
//        VolleySingleton.getInstance(this).addToRequestQueue(jsonRequest);
//    }
//
//    public Response.Listener<JSONObject> detailActivityListener = response ->
//    {
//        Log.i("VOLLEY", "response" + response.toString());
//        Intent intent = new Intent(RecordDailyLogDetails.this, RecordDailyLog.class);
//        startActivity(intent);
//    };
//
//    Response.ErrorListener errorActivityListener = error -> {
//        Log.e("VOLLEY", "" + error.getMessage());
//        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
//        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
//    };
//    // End Method for Create Activity
//
//    private JSONObject createParamsToJSON()
//    {
//        JSONArray activityOne = new JSONArray();
//
//        for(int i = 0; i < lists.size(); i++)
//        {
//            RecordDailyLogDetailsModel model = lists.get(i);
//            if(model.getActivityName() != null) {
//                activityOne.put(model.getActivityName());
//            }
//        }
//        JSONObject object = new JSONObject();
//        try
//        {
//            Log.i("VOLLEY", "date: " + date);
//            Log.i("VOLLEY", "shift: " + String.valueOf(2));
//            Log.i("VOLLEY", "time: " + time.substring(0, 5));
//            Log.i("VOLLEY", "activity: " + activityOne);
//
//            object.put("date", date);
//            object.put("shift", "2");
//            object.put("time", time.substring(0, 5));
//            object.put("activity[]", activityOne);
//
//        } catch (JSONException e)
//        {
//            e.printStackTrace();
//        }
//
//        Log.i("VOLLEY", "CreateActivity: object: " + object.toString());
//
//        return object;
//    }

    private void CreateActivity() {

        StringRequest request = new StringRequest(Request.Method.POST, RestAPI.CreateActivity, detailCreateListener, errorCreateListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
//                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + storeToken);
                return headers;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                JSONArray activityOne = new JSONArray();

                for(int i = 0; i < lists.size(); i++)
                {
                    RecordDailyLogDetailsModel model = lists.get(i);
                    if(model.getActivityName() != null) {
                        activityOne.put(model.getActivityName());
                    }
                }

                Log.i("VOLLEY", "date: " + date);
                Log.i("VOLLEY", "shift: " + "2");
                Log.i("VOLLEY", "time: " + time.substring(0, 5));
                Log.i("VOLLEY", "activity[]: " + activityOne);

                String temp = String.valueOf(activityOne);
                String temp_two = temp.replaceAll("\\[\"", "").replaceAll("\",\"", ", ").replaceAll("\\\"]", "");

                params.put("date", String.valueOf(date));
                params.put("shift", "2");
                params.put("time", time.substring(0, 5));
                params.put("activity[]", String.valueOf(temp_two));
//                params.put("activity[]", "Activity 6");

                Log.i("VOLLEY", "PARAMS 1: " + params.get("date"));
                Log.i("VOLLEY", "PARAMS 2: " + params.get("shift"));
                Log.i("VOLLEY", "PARAMS 3: " + params.get("time"));
                Log.i("VOLLEY", "PARAMS 4: " + params.get("activity[]"));

                return params;

            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailCreateListener = response -> {
        Log.i("VOLLEY", "response" + response.toString());

        Toast.makeText(getApplicationContext(), "Successfully Created.", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(RecordDailyLogDetails.this, RecordDailyLog.class);
        finish();
    };

    private Response.ErrorListener errorCreateListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + Arrays.toString(error.getStackTrace()));

        AlertDialog.Builder builder = new AlertDialog.Builder(RecordDailyLogDetails.this);
        builder.setTitle("Please Select an Activity");
        builder.setCancelable(true);
        builder.setNegativeButton(
                "Back",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
//        Toast.makeText(getApplicationContext(), String.valueOf(error.getMessage()), Toast.LENGTH_LONG).show();
    };

    public void checkButton(int position, String activityName) {

        lists.get(position).setActivityName(activityName);
        Log.i("CHECK", "MAIN CHECK: " + lists.get(position).getActivityName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent = new Intent(RecordDailyLogDetails.this, RecordDailyLog.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}