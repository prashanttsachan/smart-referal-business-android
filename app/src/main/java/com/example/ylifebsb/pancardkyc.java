package com.example.ylifebsb;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.spi.AbstractSelectionKey;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class pancardkyc extends Fragment {
    int SELECT_PHOTO = 1;
    Uri uri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_pancardkyc, container, false);
        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        Button choose = (Button) view.findViewById(R.id.chooseImagePanBtn);
        Button next = (Button) view.findViewById(R.id.nextKycPANBtn);

        Fragment fragment = new Accountinfo();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction t = fragmentManager.beginTransaction();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getView().findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                update();

            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
    Bitmap bitmap;
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() !=null){
            uri = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                ImageView panimage = (ImageView) getView().findViewById(R.id.pancardImageView);
                panimage.setImageBitmap(bitmap);
                panimage.setVisibility(View.VISIBLE);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void update() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://srbn.herokuapp.com/user/pan-details",
                new Response.Listener<NetworkResponse>(){
                    @Override
                    public void onResponse(NetworkResponse response) {
                        getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Successfully Uploaded!", Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getView().findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Failed Retry!", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                DBHelper db = new DBHelper(getActivity());
                Cursor c = db.getdata();
                c.moveToNext();
                String token = c.getString(c.getColumnIndex("token"));
                headers.put("Authorization","Bearer "+ token);
                return headers;
            }
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String imagename = "Pancardimg";
                params.put("pan", new DataPart(imagename+".png", getFileDataFromDrawable(bitmap)));

                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
                EditText panno = (EditText) getView().findViewById(R.id.panNoEditText);
                String pan = panno.getText().toString();
                params.put("panNumber",pan);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);

    }
}