package com.example.ylifebsb;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Application;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import static android.graphics.Color.*;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onStart() {
        DBHelper db = new DBHelper(this);
        Cursor c = db.getdata();
        if(c.getCount()!=0) {
            c.moveToNext();
            String token = c.getString(c.getColumnIndex("token"));
            if (!(token.equals("") || token.equals(null))) {
                Intent i = new Intent(getApplicationContext(), homelayout.class);
                startActivity(i);
            }
        }
        super.onStart();
    }

    FragmentManager fm = getSupportFragmentManager();

    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        Button signin = (Button)  findViewById(R.id.signInBtn);
        EditText email = (EditText) findViewById(R.id.editTextMobileNumber);
        EditText password = (EditText) findViewById(R.id.editTextPassword);
        TextView alertbox = (TextView) findViewById(R.id.alertBox);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);



        mRequestQueue = Volley.newRequestQueue(this);
        DBHelper db = new DBHelper(this);
        try{
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("")) {
                    alertbox.setText("Enter email id!");
                } else if (password.getText().toString().equals(""))
                    alertbox.setText("Enter Password!");
                else {
                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);

                    alertbox.setText("");
                    String url = "https://srbn.herokuapp.com/auth/login";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", email.getText().toString());
                        jsonObject.put("password", password.getText().toString());
                        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                String message = null;
                                try {
                                    message = response.getString("message");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (message.equals("User not exists.")) {
                                    alertbox.setText("User not exists.");
                                } else if (message.equals("Password doesn't match.")) {
                                    alertbox.setText("Password doesn't match.");
                                } else {
                                    JSONObject data = new JSONObject();
                                    try {
                                        data = response.getJSONObject("data");
                                        String token = (String) data.getString("access_token");
                                        JSONObject user = new JSONObject();
                                        user = data.getJSONObject("user");
                                        String name = user.getString("firstname") + " " + user.getString("lastname");
                                        String email = user.getString("email");
                                        db.insert(token, name, email);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    Intent i = new Intent(getApplicationContext(), homelayout.class);
                                    Toast.makeText(getApplicationContext(), "Successfully logged In", Toast.LENGTH_SHORT).show();
                                    startActivity(i);
                                }
                            }
                        };

                        Response.ErrorListener errorListener = new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {

                                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                String responseBody = null;
                                String message = null;
                                if (error instanceof NetworkError) {
                                    message = "Cannot connect to Internet...Please check your connection!";
                                } else if (error instanceof ServerError) {
                                    message = "The server could not be found. Please try again after some time!!";
                                } else if (error instanceof AuthFailureError) {
                                    message = "Cannot connect to Internet...Please check your connection!";
                                } else if (error instanceof ParseError) {
                                    message = "Parsing error! Please try again after some time!!";
                                } else if (error instanceof NoConnectionError) {
                                    message = "Cannot connect to Internet...Please check your connection!";
                                } else if (error instanceof TimeoutError) {
                                    message = "Connection TimeOut! Please check your internet connection.";
                                }else {
                                    message = "Failed!";
                                }
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            }
                        };
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, successListener, errorListener);
                        mRequestQueue.add(request);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "JSON exception", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });}
        catch (Exception e ){
            Toast.makeText(getApplicationContext(),"Retry! "+e,Toast.LENGTH_SHORT).show();
        }
        Button forgotpassword = (Button) findViewById(R.id.forgotPasswordBtn);
        Button register =  (Button) findViewById(R.id.registerBtn);


        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.framelayout, new Forgetpassword()).addToBackStack(null).commit();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.framelayout, new Register()).addToBackStack(null).commit();
            }
        });
    }
    @Override
    public void onBackPressed(){
        if(fm.getBackStackEntryCount()>0) {
            fm.popBackStack();
        }else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }

    }

}