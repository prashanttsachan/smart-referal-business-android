package com.example.ylifebsb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class changePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        DBHelper db = new DBHelper(this);
        Cursor c = db.getdata();
        c.moveToNext();
        String token = c.getString(c.getColumnIndex("token"));
        EditText oldpassword = (EditText) findViewById(R.id.oldpasswordchangePasswordEditText);
        EditText newpassword = (EditText) findViewById(R.id.newpasswordchangePasswordEditText);
        EditText confirmpassword = (EditText) findViewById(R.id.confirmNewPasswordChangePasswordEditText);
        Button change = (Button) findViewById(R.id.changepasswordBtnInchangePassword);
        TextView alertbox = (TextView) findViewById(R.id.alertBoxChangePasswordTextView);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertbox.setText("");
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                if(newpassword.getText().toString().equals(confirmpassword.getText().toString())){
                    StringRequest request = new StringRequest(Request.Method.POST,"https://srbn.herokuapp.com/user/change-password", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            Intent gotoprofile = new Intent(getApplicationContext(),userProfile.class);
                            gotoprofile.putExtra("token",token);
                            Toast.makeText(getApplicationContext(),"Password changed",Toast.LENGTH_SHORT).show();
                            startActivity(gotoprofile);

                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            alertbox.setText("Password is wrong");
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new Hashtable<String, String>();
                            params.put("password", oldpassword.getText().toString());
                            params.put("newPassword",newpassword.getText().toString());
                            return params;
                        }

                        @Override
                        //This is for Headers If You Needed
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Authorization", token);
                            return headers;
                        }

                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);



                }
                else{
                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    alertbox.setText("New password doesn't match.");
                }

            }
        });
    }
}