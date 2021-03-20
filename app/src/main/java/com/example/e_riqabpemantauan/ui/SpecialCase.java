package com.example.e_riqabpemantauan.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.model.ImageModel;
import com.example.e_riqabpemantauan.model.SpecialCaseModel;
import com.example.e_riqabpemantauan.utils.ImageEncode;
import com.example.e_riqabpemantauan.utils.MultipartRequest;
import com.example.e_riqabpemantauan.utils.RestAPI;
import com.example.e_riqabpemantauan.utils.SharedPrefsManager;
import com.example.e_riqabpemantauan.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SpecialCase extends AppCompatActivity {

    private Button submit;
    private ImageView prview;
    private TextView wardenName, dateDisplay, imageName, timeDisplay;
    private EditText report;
    private String mReport;
    private String mImage;
    private String date;
    private String time;
    private int coachId;
    private String wName;

    private String[] caseType ={"Kematian", "Lari dari RPM", "Lain-lain"};
    private Spinner case_type;
    private EditText locate;
    private String mlocation;

    private ConstraintLayout camera;

    private String storeToken;
    private SpecialCaseModel specialCase;
    private ImageModel image;

    private static final int CAMERA_PERMIT = 100;
    private final int CAMERA = 105;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_case);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("Kes Khas");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPrefsManager info = new SharedPrefsManager(this);
        storeToken = info.getToken();

        coachId = getIntent().getIntExtra("id", 0);
        wName = getIntent().getStringExtra("warden");

//        camera_btn = (ImageView) findViewById(R.id.camera_button);
//        camera_txt = (TextView) findViewById(R.id.camera_text);
        camera = (ConstraintLayout) findViewById(R.id.camera);
//        prview = (ImageView) findViewById(R.id.preview);
        wardenName = (TextView) findViewById(R.id.textViewName);
        dateDisplay = (TextView) findViewById(R.id.textViewDate);
        imageName = (TextView) findViewById(R.id.imageName);
        imageName.setVisibility(TextView.INVISIBLE);
        report = (EditText) findViewById(R.id.details);
        submit = (Button) findViewById(R.id.submit);
        timeDisplay = (TextView) findViewById(R.id.textViewTime);
        case_type = (Spinner) findViewById(R.id.caseType);
        locate = (EditText) findViewById(R.id.location);

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Date takeTime = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        time = dateFormat.format(takeTime);

        wardenName.setText(wName.toUpperCase());
        dateDisplay.setText(date);
        timeDisplay.setText(time);

        specialCase = new SpecialCaseModel();
        image = new ImageModel();
        specialCase.setDate(date);
        specialCase.setTime(time);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        // Spinner Dropdown for CaseType
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this,R.layout.support_simple_spinner_dropdown_item, caseType);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        case_type.setAdapter(adapter);

        case_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String locate = SpecialCase.this.case_type.getItemAtPosition(SpecialCase.this.case_type.getSelectedItemPosition()).toString();
                specialCase.setCaseType(locate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        // End Spinner for CaseType

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReport = report.getText().toString().trim();
                mlocation = locate.getText().toString().trim();
                mImage = imageName.getText().toString().trim();
                if(mReport.isEmpty()) {
                    report.setError("Field Cannot be Empty");

                    AlertDialog.Builder builder = new AlertDialog.Builder(SpecialCase.this);
                    builder.setTitle("Missing Fields");
                    builder.setMessage("1- Image is Required\n2- Special Case Field cannot be Empty");
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

                } else if(mlocation.isEmpty()) {
                    locate.setError("Field Cannot be Empty");

                    AlertDialog.Builder builder = new AlertDialog.Builder(SpecialCase.this);
                    builder.setTitle("Missing Fields");
                    builder.setMessage("1- Image is Required\n2- Location Field cannot be Empty");
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
                }
                else if(!mImage.equals("case_" + date + ".jpg")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SpecialCase.this);
                    builder.setTitle("Missing Fields");
                    builder.setMessage("1- Image is Required");
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
                }
                else {
                    SubmitData();
                }
            }
        });
    }

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

    // Method for POST data fields
    private void SubmitData() {

        MultipartRequest request = new MultipartRequest(Request.Method.POST, RestAPI.PostWardenSpecialCase, detailDataListener, errorDataListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + storeToken);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                Log.i("PARAM", "pelatih_id: " + String.valueOf(coachId));
                Log.i("PARAM", "case_type: " + specialCase.getCaseType());
                Log.i("PARAM", "date: " + specialCase.getDate());
                Log.i("PARAM", "time: " + specialCase.getTime().substring(0,5));
                Log.i("PARAM", "location: " + mlocation);
                Log.i("PARAM", "report: " + mReport);

                params.put("pelatih_id", String.valueOf(coachId));
                params.put("case_type", specialCase.getCaseType());
                params.put("date", specialCase.getDate());
                params.put("time", specialCase.getTime().substring(0,5));
                params.put("location", mlocation);
                params.put("report", mReport);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> paramsData = new HashMap<>();

                Log.i("PARAM", "image: " + image.getName() + " " + image.getBytes());

                paramsData.put("kesPicture", new DataPart(image.getName(), image.getBytes(), "image/jpeg"));

                return paramsData;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    public Response.Listener<NetworkResponse> detailDataListener = response ->
    {
        Log.i("VOLLEY", "Response" + response.toString());
        Toast.makeText(getApplicationContext(), "Successfully Submitted ", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SpecialCase.this, SpecialCaseList.class);
        startActivity(intent);

    };

    public Response.ErrorListener errorDataListener = error -> {
        Log.e("VOLLEY", "" + error.getMessage());
    };
    // End Method for POST data fields

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}