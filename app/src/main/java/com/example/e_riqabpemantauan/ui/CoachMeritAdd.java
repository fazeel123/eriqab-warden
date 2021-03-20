package com.example.e_riqabpemantauan.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.model.CoachMeritAddModel;
import com.example.e_riqabpemantauan.model.CoachMeritDetailsModel;
import com.example.e_riqabpemantauan.utils.RestAPI;
import com.example.e_riqabpemantauan.utils.SharedPrefsManager;
import com.example.e_riqabpemantauan.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoachMeritAdd  extends AppCompatActivity {

    private static final String TAG = "";
    private String storeToken;
    private int meritId;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    public EditText date, details, merit;
    private Button submit;
    private CoachMeritAddModel coachMeritAddModel;
    private CoachMeritDetailsModel coachMeritDetailList;
    private List<CoachMeritDetailsModel> lists;
    private String mDetails, markMerit;
    private TextView coach, total_merit;
    public Spinner type;
    private String[] typeList ={"Amal Baik", "Kesalahan"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_merit_add);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Markah Merit");

        coachMeritAddModel = new CoachMeritAddModel();

        coach = (TextView) findViewById(R.id.coach);
        total_merit = (TextView) findViewById(R.id.totalMerit);
        type = (Spinner) findViewById(R.id.type);
        date = (EditText) findViewById(R.id.datepicker);
        details = (EditText) findViewById(R.id.details);
        merit = (EditText) findViewById(R.id.merit);
        submit = (Button) findViewById(R.id.submit);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        meritId = getIntent().getIntExtra("id", 0);
        Log.i("Intent", "ID: " + meritId);
//        Toast.makeText(getApplicationContext(), String.valueOf(meritId), Toast.LENGTH_LONG).show();

        // Date Dialog for Tarikh Field
        date.setInputType(InputType.TYPE_NULL);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendr = Calendar.getInstance();
                int day = calendr.get(Calendar.DAY_OF_MONTH);
                int month = calendr.get(Calendar.MONTH);
                int year = calendr.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(CoachMeritAdd.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        // End Dialog for Tarikh Field

       // Spinner Dropdown for Type
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this,R.layout.support_simple_spinner_dropdown_item, typeList);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        type.setAdapter(adapter);

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String value = String.valueOf(CoachMeritAdd.this.type.getSelectedItem().toString());
                coachMeritAddModel.setType(value);
                Log.i("MERIT", "Merit Value: " + value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
       // End Spinner for Type

        lists = new ArrayList<>();
        fetchDetails();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetails = details.getText().toString().trim();
                markMerit = merit.getText().toString().trim();
                coachMeritAddModel.setDate(date.getText().toString().trim());

                if(coachMeritAddModel.getDate().isEmpty() && mDetails.isEmpty() && markMerit.isEmpty()) {
                    date.setError("Field is required!");
                    details.setError("Field is required");
                    merit.setError("Field is required");
                } else if(coachMeritAddModel.getDate().isEmpty())  {
                    date.setError("Field is required!");
                } else if(mDetails.isEmpty()) {
                    details.setError("Field is required");
                } else if(markMerit.isEmpty()) {
                    merit.setError("Field is required");
                } else {
                    sendMerit();
                }
            }
        });
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
            JSONObject jsonObject = responseObj.optJSONObject("data");

            int id = jsonObject.optInt("id");
            String name = jsonObject.optString("name");
            int totalMerit = jsonObject.optInt("jumlah_merit");

            int length = jsonObject.length();

            for (int i = 0; i < length; i++) {

                String date = jsonObject.optString("tarikh");
                String note = jsonObject.optString("catatan");
                String type = jsonObject.optString("jenis");
                String merit = jsonObject.optString("merit");

                coachMeritDetailList = new CoachMeritDetailsModel();
                coachMeritDetailList.setId(id);
                coachMeritDetailList.setName(name);
                coachMeritDetailList.setTotalMerit(totalMerit);
                lists.add(coachMeritDetailList);

                coach.setText(coachMeritDetailList.getName());
                total_merit.setText(Integer.toString(coachMeritDetailList.getTotalMerit()));
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


    private void sendMerit() {

        String URL = RestAPI.AddMeritDetails + meritId;

        StringRequest request = new StringRequest(Request.Method.POST, URL, detailPostListener, errorPostListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Content-Type", "application/json; charset=UTF-8 ");
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                Log.i("PARAM", "Date: " + date);
                Log.i("PARAM", "Note: " + mDetails);
                Log.i("PARAM", "Merit: " + markMerit);
                Log.i("PARAM", "Type: " + coachMeritAddModel.getType());

                Map<String, String> params = new HashMap<String, String>();
                params.put("tarikh", String.valueOf(coachMeritAddModel.getDate()));
                params.put("butiran", String.valueOf(mDetails));
                params.put("merit", String.valueOf(markMerit));
                params.put("jenis", String.valueOf(coachMeritAddModel.getType()));
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<String> detailPostListener = response ->
    {
        Log.i("VOLLEY", "response" + response);
        Intent intent = new Intent(getApplicationContext(), CoachMerit.class);
        startActivity(intent);
    };

    Response.ErrorListener errorPostListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent = new Intent(CoachMeritAdd.this, CoachMeritDetails.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
