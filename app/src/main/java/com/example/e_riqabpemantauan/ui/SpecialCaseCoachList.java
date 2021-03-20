package com.example.e_riqabpemantauan.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.adapter.SpecialCaseCoachListAdapter;
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

public class SpecialCaseCoachList extends AppCompatActivity {

    private SpecialCaseCoachListAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<SpecialCaseModel> coachDetails = new ArrayList<>();

    private String storeToken;
    private SpecialCaseModel specialCase;
    ConstraintLayout wardenLayout;
    TextView wardenN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_case_coach_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Kes Khas");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        specialCase = new SpecialCaseModel();

        wardenLayout = findViewById(R.id.wardenNameLayout);
        wardenN = findViewById(R.id.wardenName);

        setupMenuRecyclerView();
        fetchDetails();

        wardenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SpecialCase.class);
                intent.putExtra("warden", specialCase.getWardenName());
                startActivity(intent);

            }
        });
    }

    // Method for RecyclerView
    private void setupMenuRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SpecialCaseCoachListAdapter(  this, coachDetails);
        recyclerView.setAdapter(adapter);
    }
    // End Method for RecyclerView

    // Method for GET the name of Warden
    private void fetchDetails() {

        StringRequest request = new StringRequest(Request.Method.GET, RestAPI.GetWardenName, detailListener, errorListener) {
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

        try {
            JSONObject responseObj = new JSONObject(response);
            JSONObject jsonResponse = responseObj.optJSONObject("data");

            String name = jsonResponse.optString("warden_name");

            JSONArray array = jsonResponse.optJSONArray("pelatih");
            int length = array.length();

            for(int i = 0; i < length; i++) {
                JSONObject jsonObject = array.optJSONObject(i);
                int pid = jsonObject.optInt("id");
                String pname = jsonObject.optString("name");

                specialCase = new SpecialCaseModel();
                specialCase.setWardenName(name);
                specialCase.setId(pid);
                specialCase.setCoachName(pname);
                coachDetails.add(specialCase);
                adapter.notifyItemInserted(i);
            }

            wardenN.setText(name.toUpperCase());

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    };

    Response.ErrorListener errorListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
    };
    // End for GET the name of Warden

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(SpecialCaseCoachList.this, HomePage.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
