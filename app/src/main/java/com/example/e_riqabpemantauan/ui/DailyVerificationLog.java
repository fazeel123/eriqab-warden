package com.example.e_riqabpemantauan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.adapter.DailyVerificationLogAdapter;
import com.example.e_riqabpemantauan.model.DailyVerificationLogModel;
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

public class DailyVerificationLog extends AppCompatActivity {

    private static final String TAG = "";
    private String storeToken;
    private DailyVerificationLogModel dailyVerificationLogModel;
    private RecyclerView recyclerView;
    private DailyVerificationLogAdapter adapter;
    private List<DailyVerificationLogModel> lists;
    //private ArrayList<DailyVerificationLogModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_verification_log);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Pengesahan Log");

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        lists = new ArrayList<>();
        setupMenuRecyclerView();
        fetchDetails();

    }

    private void setupMenuRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new DailyVerificationLogAdapter(this, lists);
        recyclerView.setAdapter(adapter);
    }

    private void fetchDetails() {

        StringRequest request = new StringRequest(Request.Method.GET, RestAPI.DailyVerificationLog, detailListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/vnd.MAIS_ERIQAB.v1+json");
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
                String date = logObj.optString("date");
                String shift = logObj.optString("shift");
                String warden_One = logObj.optString("wardenOne");
                String warden_Two = logObj.optString("wardenTwo");
                String house_Ruler = logObj.optString("houseRuler");
                String executive = logObj.optString("executive");
                String comment = logObj.optString("comment");

                dailyVerificationLogModel = new DailyVerificationLogModel();
                dailyVerificationLogModel.setId(id);
                dailyVerificationLogModel.setDate(date);
                dailyVerificationLogModel.setShift(shift);
                dailyVerificationLogModel.setWardenOneId(warden_One);
                dailyVerificationLogModel.setGetWardenTwoId(warden_Two);
                dailyVerificationLogModel.setHourseRulerId(house_Ruler);
                dailyVerificationLogModel.setExecutiveId(executive);
                dailyVerificationLogModel.setComment(comment);
                lists.add(dailyVerificationLogModel);
                adapter.notifyItemInserted(i);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    Response.ErrorListener errorListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent = new Intent(DailyVerificationLog.this, HomePage.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}