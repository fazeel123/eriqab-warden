package com.example.e_riqabpemantauan.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.example.e_riqabpemantauan.R;
import com.example.e_riqabpemantauan.model.UserModel;
import com.example.e_riqabpemantauan.utils.RestAPI;
import com.example.e_riqabpemantauan.utils.SharedPrefsManager;
import com.example.e_riqabpemantauan.utils.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private Button login;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        login = (Button) findViewById(R.id.loginButton);
        email = (EditText)  findViewById(R.id.id_pengguna);
        password = (EditText) findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String mPassword = password.getText().toString().trim();

                if (!mEmail.isEmpty() || !mPassword.isEmpty()) {
                    Login();
                } else {

                    email.setError("Please Insert Email");
                    password.setError("Please Insert Password");
                }
            }
        });
    }

    private void Login() {
        StringRequest request = new StringRequest(Request.Method.POST, RestAPI.LOGIN, loginListener, errorListener)
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/vnd.MAIS_ERIQAB.v1+json");
                return headers;
            }
        };
        VolleySingleton.getInstance(Login.this).addToRequestQueue(request);
    }

    private Response.Listener<String> loginListener = response ->
    {
        if(response.isEmpty())
        {
            Toast.makeText(this, "Response is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i("VOLLEY", "response: " + response);

        try
        {
            JSONObject responseObject   = new JSONObject(response);
            String access_token   = responseObject.optString("access_token");
            if(!access_token.isEmpty())
            {
                UserModel user = new UserModel();
                user.setAccess_token(access_token);
                SharedPrefsManager info = new SharedPrefsManager(this);
                info.signIn(user);
                Intent intent = new Intent(Login.this, HomePage.class);
//                Toast.makeText(this, "Successfully Logged In.", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "Failed to sign in", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    };

    private Response.ErrorListener errorListener = error ->
    {
//        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle("Invalid Login Credentials");
        builder.setMessage("Your Email or Password is incorrect!");
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

}
