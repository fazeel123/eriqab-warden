package com.example.e_riqabpemantauan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.adapter.CoachMeritDetailsAdapter;
import com.example.e_riqabpemantauan.model.CoachMeritDetailsModel;
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

public class CoachMeritDetails extends AppCompatActivity {

    private static final String TAG = "";
    private CoachMeritDetailsModel coachMeritDetailList;
    private String storeToken;
    private int meritId;
    public TextView coach, totlMerit;
    public Button add;
    private RecyclerView recyclerView;
    private CoachMeritDetailsAdapter adapter;
    private List<CoachMeritDetailsModel> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_merit_details);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Markah Merit");

        coach = (TextView) findViewById(R.id.coach);
        totlMerit = (TextView)findViewById(R.id.totalMerit);
        add = (Button) findViewById(R.id.add);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        meritId = getIntent().getIntExtra("id", 1);
        Log.i("Intent", "ID: " + meritId);
//        Toast.makeText(getApplicationContext(), String.valueOf(meritId), Toast.LENGTH_LONG).show();

        lists = new ArrayList<>();
        setupMenuRecyclerView();
        fetchDetails();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeritDetails();
            }
        });
    }

    private void setupMenuRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CoachMeritDetailsAdapter(this, lists);
        recyclerView.setAdapter(adapter);
    }

    private void fetchDetails() {

        String URL = RestAPI.ViewCoachDetailsId + meritId;

        StringRequest request = new StringRequest(Request.Method.GET, URL, detailLoadListener, errorLoadListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/json; charset=UTF-8 ");
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailLoadListener = response ->
    {
        Log.i("VOLLEY", "response" + response);
        try {
            JSONObject responseObj = new JSONObject(response);
            JSONObject jsonResponse = responseObj.optJSONObject("data");

            int id = jsonResponse.optInt("id");
            String name = jsonResponse.optString("name");
            int totalMerit = jsonResponse.optInt("jumlah_merit");

            JSONArray array = jsonResponse.optJSONArray("merit_list");
            int length = array.length();

            for (int i = 0; i < length; i++) {
                JSONObject meritObj = array.optJSONObject(i);
                int mId = meritObj.optInt("id");
                String date = meritObj.optString("tarikh");
                String note = meritObj.optString("catatan");
                String type = meritObj.optString("jenis");
                String merit = meritObj.optString("merit");

                coachMeritDetailList = new CoachMeritDetailsModel();
                coachMeritDetailList.setId(id);
                coachMeritDetailList.setName(name);
                coachMeritDetailList.setTotalMerit(totalMerit);
                coachMeritDetailList.setmId(mId);
                coachMeritDetailList.setDate(date.substring(0, 10));
                coachMeritDetailList.setNote(note);
                coachMeritDetailList.setType(type);
                coachMeritDetailList.setMerit(merit);
                lists.add(coachMeritDetailList);
                adapter.notifyItemInserted(i);

                coach.setText(coachMeritDetailList.getName());
                totlMerit.setText(Integer.toString(coachMeritDetailList.getTotalMerit()));

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

    private void addMeritDetails() {
        Intent intent = new Intent(this, CoachMeritAdd.class);
        intent.putExtra("id", meritId);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent = new Intent(CoachMeritDetails.this, CoachMerit.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}