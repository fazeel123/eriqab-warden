package com.example.e_riqabpemantauan.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.utils.RestAPI;
import com.example.e_riqabpemantauan.utils.SharedPrefsManager;
import com.example.e_riqabpemantauan.utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class DailyVerificationLogActivityDisapprove extends AppCompatActivity {

    private String storeToken;
    private int logId;
    private TextView name, date, shift;
    private EditText comment;
    private Button submit;
    private String mComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_verification_log_disapprove);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Log Harian");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        submit = (Button) findViewById(R.id.submit);
        comment = (EditText) findViewById(R.id.details);
//        name = (TextView) findViewById(R.id.name);
        date = (TextView) findViewById(R.id.time);
        shift = (TextView) findViewById(R.id.shift);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        logId = getIntent().getIntExtra("id", 0);
//        Toast.makeText(getApplicationContext(), String.valueOf(logId), Toast.LENGTH_LONG).show();
//        mComment = comment.getText().toString().trim();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mComment = comment.getText().toString().trim();
                if(mComment.isEmpty()) {
                    comment.setError("Field Cannot be Empty");
                } else {
                    disapproveDetails();
                }
            }
        });
    }

    private void disapproveDetails() {

        Log.i("PARAM", "log_id: " + logId);
        Log.i("PARAM", "comment: " + mComment);

        String URL = RestAPI.DailyVerificationLogDisapprove + logId;

        StringRequest request = new StringRequest(Request.Method.POST, URL, detailDisapproveListener, errorDisapproveListener) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("log_id", String.valueOf(logId));
                params.put("comment", comment.getText().toString().trim());
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

    public Response.Listener<String> detailDisapproveListener = response ->
    {
        Log.i("VOLLEY", "response" + response);

        Intent intent = new Intent(this, DailyVerificationLog.class);
        startActivity(intent);
    };

    Response.ErrorListener errorDisapproveListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());

        AlertDialog.Builder builder = new AlertDialog.Builder(DailyVerificationLogActivityDisapprove.this);
        builder.setTitle("Unauthorized to Disapprove");
        builder.setMessage("Only Executive and HouseRuler is authorized to do it.");
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
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            Intent intent = new Intent(DailyVerificationLogActivityDisapprove.this, DailyVerificationLogActivityList.class);
            intent.putExtra("id", logId);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
