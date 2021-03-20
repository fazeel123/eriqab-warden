package com.example.e_riqabpemantauan.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.model.ImageModel;
import com.example.e_riqabpemantauan.model.RecordDailyLogListModel;
import com.example.e_riqabpemantauan.utils.ImageEncode;
import com.example.e_riqabpemantauan.utils.MultipartRequest;
import com.example.e_riqabpemantauan.utils.RestAPI;
import com.example.e_riqabpemantauan.utils.SharedPrefsManager;
import com.example.e_riqabpemantauan.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RecordDailyLogList extends AppCompatActivity {

    private static final String TAG = "";
    private String storeToken;
    private int Id;
    private String date, getImageName;
    private String activityName, timeValue;
    private ImageModel image;
    private RecordDailyLogListModel recordListModel;

    private TextView mTime, mActivity, imageName;;
    private Button submit;
    private EditText notes;
    private String mNotes;
    private CheckBox accept, other;
    private CardView comment;
    private ConstraintLayout camera;

    private static final int CAMERA_PERMIT = 100;
    private final int CAMERA = 105;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_daily_log_list);
        Log.d(TAG, "onCreate: called.");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Log Harian");

        mTime = (TextView) findViewById(R.id.time);
        mActivity = (TextView) findViewById(R.id.activity);
        submit = (Button) findViewById(R.id.submit);
        notes = (EditText) findViewById(R.id.notes);
        accept = (CheckBox) findViewById(R.id.agree);
        other = (CheckBox) findViewById(R.id.other);
        comment = (CardView) findViewById(R.id.note);
        imageName = (TextView) findViewById(R.id.imageName);
        camera = (ConstraintLayout) findViewById(R.id.camera);

        imageName.setVisibility(TextView.INVISIBLE);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        Id = getIntent().getIntExtra("id", 0);
        activityName = getIntent().getStringExtra("activity");
        timeValue = getIntent().getStringExtra("time");
//        Toast.makeText(getApplicationContext(), String.valueOf(Id), Toast.LENGTH_LONG).show();

        image = new ImageModel();
        recordListModel = new RecordDailyLogListModel();

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        fetchDetails();

        mTime.setText(String.valueOf(timeValue));
        mActivity.setText(String.valueOf(activityName));

        mNotes = notes.getText().toString().trim();

        other.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if(buttonView.isChecked()) {

            } else {
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNotes = notes.getText().toString().trim();
                if (mNotes.isEmpty()) {
                    notes.setError("Field Cannot be Empty");
                } else {
                    mNotes = notes.getText().toString().trim();
                    approveDetails();
                }
            }
        });
    }

    private void fetchDetails() {

        String URL = RestAPI.DailyDetailsId + Id;

        StringRequest request = new StringRequest(Request.Method.GET, URL, detailListener, errorListener) {

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
            JSONObject array = responseObj.optJSONObject("data");

            int id = array.optInt("id");
            String time = array.optString("time");
            String activity = array.optString("activity");
            String notes = array.optString("notes");
            String picture = array.optString("picture");

            recordListModel.setId(id);
            recordListModel.setTime(time);
            recordListModel.setActivity(activity);
            recordListModel.setNotes(notes);
            recordListModel.setPicture(picture);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    };

    Response.ErrorListener errorListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
    };

    private void dispatchTakePictureIntent() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
        {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMIT);
            }
            else
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA);
            }
        }
        else
        {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA) {

            if (resultCode == Activity.RESULT_OK)
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                image.setName("case_" + date + ".jpg");
                image.setBytes(ImageEncode.getByteArray(photo));

                imageName.setText(image.getName());
                imageName.setVisibility(TextView.VISIBLE);
                //prview.setImageBitmap(photo);
            }

        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "You haven't take an Image", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMIT)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA);
            }
            else
            {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void approveDetails() {

        getImageName = imageName.getText().toString().trim();

        String URL = RestAPI.UpdateDailyDetails + Id;

        if(image.getName() == null) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, detailNoteListener, errorNoteListener) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + storeToken);
                    return params;
                }

                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    Log.i("PARAM", "Notes: " + mNotes);

                    params.put("notes", mNotes);
                    return params;
                }
            };
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

        } else {

            MultipartRequest request = new MultipartRequest(Request.Method.POST, URL, detailApproveListener, errorApproveListener) {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("Content-Type", "application/form-data");
                    params.put("Authorization", "Bearer " + storeToken);
                    return params;
                }

                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    Log.i("PARAM", "Notes: " + mNotes);

                    params.put("notes", mNotes);
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> paramsData = new HashMap<>();

                    Log.i("PARAM", "image: " + image.getName() + " " + image.getBytes());

                    paramsData.put("kesPicture", new DataPart(image.getName(), image.getBytes(),"image/jpeg"));

                    return paramsData;
                }
            };
            VolleySingleton.getInstance(this).addToRequestQueue(request);
        }
    }

    private Response.Listener<String> detailNoteListener = response -> {
        Log.i("VOLLEY", "response" + response);

        Toast.makeText(getApplicationContext(), "Successfully Submitted", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, RecordDailyLog.class);
        startActivity(intent);
    };

    private Response.ErrorListener errorNoteListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
        Log.d("VOLLEY", "Failed with error msg:\t" + error.getMessage());
        Log.d("VOLLEY", "Error StackTrace: \t" + error.getStackTrace());
    };

    public Response.Listener<NetworkResponse> detailApproveListener = response ->
    {
        Log.i("VOLLEY", "response" + response);

        Toast.makeText(getApplicationContext(), "Successfully Submitted", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, RecordDailyLog.class);
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
            Intent intent = new Intent(RecordDailyLogList.this, RecordDailyLogDetails.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
