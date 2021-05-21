package com.example.ylifebsb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class Forgetpassword extends Fragment {


    private FragmentManager supportFragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forgetpassword, container, false);
        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        Button sendotp = (Button) view.findViewById(R.id.SendotpBtn);
        EditText email = (EditText) view.findViewById(R.id.editForgetMobileNumber);
        TextView alert = (TextView) view.findViewById(R.id.alertBoxMobileNumber);
        Fragment fragmentTwo;
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        Fragment fragment = new OtpVerification();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction t = fragmentManager.beginTransaction();
        sendotp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("")) {
                    alert.setText("Enter email address!");
                } else {
                    view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    alert.setText("");
                    String url = "https://srbn.herokuapp.com/auth/forget-password";
                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("email", email.getText().toString());
                        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                String otp = new String();

                                try {
                                    String message = response.getString("message");
                                    otp = message.replaceAll("[^0-9]","");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                final Bundle bundle = new Bundle();
                                bundle.putString("email", email.getText().toString());
                                bundle.putString("otp",otp);
                                fragment.setArguments(bundle);

                                t.replace(R.id.framelayout, fragment).addToBackStack(null).commit();


                            }
                        };

                        Response.ErrorListener errorListener = new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                String responseBody = null;
                                try {
                                    responseBody = new String(error.networkResponse.data, "utf-8");

                                    JSONObject data = new JSONObject(responseBody);
                                    String message = (String) data.get("message");
                                    alert.setText(message);
                                } catch (UnsupportedEncodingException | JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, successListener, errorListener);
                        mRequestQueue.add(request);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return view;
    }

}
