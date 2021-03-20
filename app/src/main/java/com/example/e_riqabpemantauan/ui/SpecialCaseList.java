package com.example.e_riqabpemantauan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.adapter.SpecialCaseListAdapter;
import com.example.e_riqabpemantauan.model.SpecialCaseModel;
import com.example.e_riqabpemantauan.utils.RestAPI;
import com.example.e_riqabpemantauan.utils.SharedPrefsManager;
import com.example.e_riqabpemantauan.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpecialCaseList extends AppCompatActivity {

    private String storeToken;
    private SpecialCaseModel specialCase;
    private SpecialCaseListAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<SpecialCaseModel> specialCaseModels = new ArrayList<>();

    private Button done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_case_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Kes Khas");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        done = findViewById(R.id.done);

        setupMenuRecyclerView();
        fetchDetails();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SpecialCaseCoachList.class);
                startActivity(intent);
            }
        });
    }

    private void setupMenuRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SpecialCaseListAdapter(  this, specialCaseModels);
        recyclerView.setAdapter(adapter);
        //adapter.setOnItemClickListener(ClassDetails.this);
    }

    private void fetchDetails() {

        StringRequest request = new StringRequest(Request.Method.GET, RestAPI.CaseList, detailListener, errorListener) {
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
                JSONObject caseObj = array.optJSONObject(i);
                String id = caseObj.optString("ref_id");
                JSONObject coach = caseObj.optJSONObject("pelatih");
                String coachId = coach.optString("id");
                String coachName = coach.optString("name");
                JSONObject warden = caseObj.optJSONObject("user");
                String wardenId = warden.optString("id");
                String wardenName = warden.optString("name");
                String caseType = caseObj.optString("case_type");
                String date = caseObj.optString("date");
                String time = caseObj.optString("time");
                String location = caseObj.optString("location");
                String report = caseObj.optString("report");
                String picture = caseObj.optString("kesPicture");

                specialCase = new SpecialCaseModel();
                specialCase.setRefId(id);
                specialCase.setmCoachId(coachId);
                specialCase.setmCoachName(coachName);
                specialCase.setmWardenId(wardenId);
                specialCase.setmWardenName(wardenName);
                specialCase.setmCaseType(caseType);
                specialCase.setmDate(date);
                specialCase.setmTime(time);
                specialCase.setmLocation(location);
                specialCase.setmReport(report);
                specialCase.setmCasePicture(picture);
                specialCaseModels.add(specialCase);
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

        if (id == android.R.id.home) {
            Intent intent = new Intent(SpecialCaseList.this, SpecialCaseCoachList.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
