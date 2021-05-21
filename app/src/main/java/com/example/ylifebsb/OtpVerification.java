package com.example.ylifebsb;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class OtpVerification extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_otp_verification, container, false);
        TextView countdown = (TextView) view.findViewById(R.id.countdownTextview);
        Button resendcode  = (Button) view.findViewById(R.id.resendOtpBtn);
        Button verify = (Button) view.findViewById(R.id.verifyOtpBtn);
        TextView alertbox = (TextView) view.findViewById(R.id.alertBoxOTP);
        resendcode.setVisibility(View.GONE);
        Fragment fragment = new Confirmpassword();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction t = fragmentManager.beginTransaction();
        EditText otp = (EditText) view.findViewById(R.id.editTextOTP);
        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        Bundle args = getArguments();
        String email = args.getString("email");
        String otptext = args.getString("otp");
        otp.setText(otptext);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().equals("")){
                    alertbox.setText("Enter OTP!");
                }
                else {
                    view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    alertbox.setText("");
                    String url = "https://srbn.herokuapp.com/auth/forget-password-otp";
                    JSONObject jsonObject = new JSONObject();
                    try {

                        jsonObject.put("email", email);
                        jsonObject.put("otp",otp.getText().toString());
                        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                                final Bundle bundle = new Bundle();
                                bundle.putString("email", email);
                                bundle.putString("otp",otp.getText().toString());
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





        new CountDownTimer(300000, 1000) {

            public void onTick(long duration) {
                long Mmin = (duration / 1000) / 60;
                long Ssec = (duration / 1000) % 60;
                if (Ssec < 10) {
                    countdown.setText("" + Mmin + ":0" + Ssec);
                } else countdown.setText("" + Mmin + ":" + Ssec);
            }

            public void onFinish() {
                countdown.setText("00:00");
                resendcode.setVisibility(View.VISIBLE);
            }

        }.start();
        return view;
    }
}




