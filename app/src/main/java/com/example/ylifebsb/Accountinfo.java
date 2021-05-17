package com.example.ylifebsb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class Accountinfo extends Fragment {
    int SELECT_PHOTO = 1;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accountinfo, container, false);
        Button choose = (Button) view.findViewById(R.id.chooseImageAccountBtn);
        Button submit = (Button) view.findViewById(R.id.submitKycInfoBtn);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() !=null){
            uri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ImageView passimg = (ImageView) getView().findViewById(R.id.passbookImageView);
                passimg.setImageBitmap(bitmap);
                passimg.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }


    private void update() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://srbn.herokuapp.com/user/bank-details",
                new Response.Listener<NetworkResponse>(){
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            Toast.makeText(getActivity().getApplicationContext(),"Successfull",Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getContext(),wallet.class);
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                String token = ((KYCadhaar)getContext()).getToken();
                headers.put("Authorization", token);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
                EditText Accountholder =(EditText) getView().findViewById(R.id.accountholdername);
                EditText Accountno = (EditText) getView().findViewById(R.id.accountnumber);
                EditText IFSC =(EditText) getView().findViewById(R.id.IFSC);
                params.put("account",Accountholder.getText().toString());
                params.put("ifsc",Accountno.getText().toString());
                params.put("name",IFSC.getText().toString());
                return params;
            }


        };

        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);

    }
}