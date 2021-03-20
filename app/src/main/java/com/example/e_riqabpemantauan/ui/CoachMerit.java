package com.example.e_riqabpemantauan.ui;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.adapter.CoachMeritAdapter;
import com.example.e_riqabpemantauan.model.CoachMeritModel;
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

public class CoachMerit extends AppCompatActivity {

    private static final String TAG = "";
    //private Button search_btn;
    private RecyclerView recyclerView;
    private CoachMeritAdapter adapter;
    private List<CoachMeritModel> lists;
    private CoachMeritModel coachMeritList;
    private String storeToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_merit);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Markah Merit");

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

//        searchView = (SearchView) findViewById(R.id.search_field);
//        search_btn = (Button) findViewById(R.id.search_button);

        lists = new ArrayList<>();
        setupMenuRecyclerView();
        fetchDetails();
    }

    private void setupMenuRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new CoachMeritAdapter(this, lists);
        recyclerView.setAdapter(adapter);
    }

    private void fetchDetails() {

        StringRequest request = new StringRequest(Request.Method.GET, RestAPI.ViewCoachDetails, detailLoadListener, errorLoadListener) {
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
        try{
            JSONObject responseObj = new JSONObject(response);
            JSONArray array = responseObj.optJSONArray("data");
            int length = array.length();

            for(int i = 0; i < length; i++) {
                JSONObject meritObj = array.optJSONObject(i);
                int id = meritObj.optInt("id");
                String name = meritObj.optString("name");
                int totalMerit = meritObj.optInt("merit");

                coachMeritList = new CoachMeritModel();
                coachMeritList.setId(id);
                coachMeritList.setName(name);
                coachMeritList.setTotalMerit(totalMerit);
                lists.add(coachMeritList);
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

    // Method for SearchView
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem  = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.RIGHT));
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint(getString(R.string.search));
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                newText.toLowerCase();
                List<CoachMeritModel> modelList = new ArrayList<>();
                for(CoachMeritModel newModel : lists) {
                    String search_data = newModel.getName().toLowerCase();
                    if(search_data.contains(newText)) {
                        modelList.add(newModel);
                    }
                }
                adapter.setSearchOperation(modelList);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    // End Method for SearchView

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent = new Intent(CoachMerit.this, HomePage.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchActivity1() {
        Intent intent = new Intent(this, CoachMeritDetails.class);
        startActivity(intent);
    }
}