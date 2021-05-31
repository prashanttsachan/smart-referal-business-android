package com.example.ylifebsb;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class Register extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);
        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        EditText sponser = (EditText) view.findViewById(R.id.sponserIDRegisterEditText);
        EditText firstname = (EditText) view.findViewById(R.id.firstnameRegisterEdittext);
        EditText lastname = (EditText) view.findViewById(R.id.lastnameEdittextRegister);
        EditText email = (EditText) view.findViewById(R.id.emailRegisterEdittext);
        EditText mobile = (EditText) view.findViewById(R.id.mobileRegisterEdittext);
        EditText password = (EditText) view.findViewById(R.id.passwordRegisterEditText);
        EditText confirmpassword = (EditText) view.findViewById(R.id.confirmpasswordRegisterEdittext);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        TextView alertbox = (TextView) view.findViewById(R.id.alertBoxRegisterTextview);

        Button register = (Button) view.findViewById(R.id.registerBtnRegister);

        try{
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sponser.getText().toString().equals("") || firstname.getText().toString().equals("") || lastname.getText().toString().equals("") || email.getText().toString().equals("") || mobile.getText().toString().equals("") || password.getText().toString().equals("") || confirmpassword.getText().toString().equals("")) {
                    alertbox.setText("Fill all the columns!");
                }else  if (!(password.getText().toString().equals(confirmpassword.getText().toString())))
                    alertbox.setText("Password doesn't match!");
                else {
                    view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    alertbox.setText("");
                    String url = "https://srbn.herokuapp.com/auth/register";
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("email", email.getText().toString());
                        jsonObject.put("mobile", mobile.getText().toString());
                        jsonObject.put("firstname", firstname.getText().toString());
                        jsonObject.put("lastname", lastname.getText().toString());
                        jsonObject.put("password", password.getText().toString());
                        jsonObject.put("sponsor", sponser.getText().toString());

                            Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    String message = new String();
                                    try {
                                        message = response.getString("message");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                    if (message.equals("A user already exists with this email/mobile.")) {
                                        alertbox.setText(message);
                                    } else {
                                        Intent i = new Intent(getActivity(), MainActivity.class);
                                        Toast.makeText(getActivity(), "Successfully Registered!", Toast.LENGTH_SHORT).show();
                                        startActivity(i);
                                    }
                                }
                            };

                            Response.ErrorListener errorListener = new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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
                                    }else{
                                    try {
                                        responseBody = new String(error.networkResponse.data, "utf-8");
                                        JSONObject data = new JSONObject(responseBody);
                                        String mes = (String) data.get("message");
                                        alertbox.setText(mes);
                                    } catch (UnsupportedEncodingException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                    }
                                    Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
                                }
                            };
                            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject, successListener, errorListener);
                            mRequestQueue.add(request);
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "JSON exception", Toast.LENGTH_SHORT).show();
                        }

                }
            }

        });}
        catch (Exception e){
            Toast.makeText(getActivity(),"RETRY! "+e,Toast.LENGTH_SHORT).show();
        }
        return view;
    }

}