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


public class Forgetpassword extends Fragment {


    private FragmentManager supportFragmentManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forgetpassword, container, false);
        Button sendotp = (Button) view.findViewById(R.id.SendotpBtn);
        EditText mobileno = (EditText) view.findViewById(R.id.editForgetMobileNumber);
        TextView alert = (TextView) view.findViewById(R.id.alertBoxMobileNumber);
        Fragment fragmentTwo;
        Fragment fragment = new OtpVerification();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction t = fragmentManager.beginTransaction();
        sendotp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(mobileno.getText().toString().length()<10){
                    alert.setText("Invalid Mobile Number!");
                }
                else{
                     t.replace(R.id.framelayout,fragment).addToBackStack(null).commit();

                }
            }
        });
        return view;
    }

    public FragmentManager getSupportFragmentManager() {
        return supportFragmentManager;
    }
}