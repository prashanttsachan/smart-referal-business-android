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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class Confirmpassword extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirmpassword, container, false);
        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        EditText newpass = (EditText) view.findViewById(R.id.newpasswordConfirmpasswordEditText);
        EditText confirmpass = (EditText) view.findViewById(R.id.confirmConfirmPasswordEditText);
        TextView alertbox = (TextView) view.findViewById(R.id.alertBoxConfirmPasswordTextView);
        Button submit = (Button) view.findViewById(R.id.confirmpasswordButtonNewPassword);
        Bundle args = getArguments();
        String email = args.getString("email");
        String otptext = args.getString("otp");
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newpass.getText().toString().equals("")){
                    alertbox.setText("Fill the new password column!");
                }else if(confirmpass.getText().toString().equals("")){
                    alertbox.setText("Fill the confirm password column!");
                }else if(!(newpass.getText().toString().equals(confirmpass.getText().toString()))){
                    alertbox.setText("Password doesn't match!");
                }else {

                    view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    alertbox.setText("");
                    String url = "https://srbn.herokuapp.com/auth/reset-password";
                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("email", email);
                        jsonObject.put("otp",otptext);
                        jsonObject.put("password",newpass.getText().toString());
                        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                Intent i = new Intent(getActivity(),MainActivity.class);
                                Toast.makeText(getActivity(),"Successfully Password reset!",Toast.LENGTH_SHORT).show();
                                startActivity(i);
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
                                    alertbox.setText(message);
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