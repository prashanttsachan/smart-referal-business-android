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


public class OtpVerification extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_otp_verification, container, false);
        TextView countdown = (TextView) view.findViewById(R.id.countdownTextview);
        Button resendcode  = (Button) view.findViewById(R.id.resendOtpBtn);
        Button verify = (Button) view.findViewById(R.id.verifyOtpBtn);
        resendcode.setVisibility(View.GONE);
        Fragment fragment = new Confirmpassword();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction t = fragmentManager.beginTransaction();
        EditText otp = (EditText) view.findViewById(R.id.editTextOTP);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().length()>=4)
                t.replace(R.id.framelayout,fragment).addToBackStack(null).commit();

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




