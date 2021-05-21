package com.example.ylifebsb;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.TextView;
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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class aadhar extends Fragment {

    int SELECT_PHOTO = 1;
    Uri uri;
    int i=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_aadhar, container, false);

        view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        Button next = (Button) view.findViewById(R.id.nextKycAdharBtn);

        EditText aadhar = (EditText) view.findViewById(R.id.AadharNoEditText);
        ImageView front = (ImageView) view.findViewById(R.id.frontAadharImageView);
        ImageView back = (ImageView) view.findViewById(R.id.backAadharImageView);
        TextView alertbox = (TextView) view.findViewById(R.id.kycAadharAlertBox);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aadhar.getText().toString().equals("")){
                    alertbox.setText("Aadhar number is not there!");
                }
                else if(front.getDrawable()==null || back.getDrawable()==null){
                    alertbox.setText("Please upload the Image");
                }
                else{

                    view.findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    SendImage();
                }
            }
        });


        Button choosefront = (Button) view.findViewById(R.id.chooseFrontImageAadhaarBtn);
        Button chooseback = (Button) view.findViewById(R.id.chooseBackImageAadhaarBtn);
        choosefront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=1;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });
        chooseback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i=2;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);

            }
        });


        return view;
    }




    Bitmap bitmap1;
    Bitmap bitmap2;
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() !=null){
            uri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                Bitmap lastBitmap = null;
                if(i==1){
                    ImageView frontimg = (ImageView) getView().findViewById(R.id.frontAadharImageView);
                    bitmap1=bitmap;
                    frontimg.setImageBitmap(bitmap);
                    frontimg.setVisibility(View.VISIBLE);
                }else{
                    ImageView backimg = (ImageView) getView().findViewById(R.id.backAadharImageView);
                    backimg.setImageBitmap(bitmap);
                    bitmap2=bitmap;
                    backimg.setVisibility(View.VISIBLE);
                }
                lastBitmap = bitmap;


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






    private void SendImage() {
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "https://srbn.herokuapp.com/user/aadhar-details",
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
                        Toast.makeText(getContext(), "Failed to upload, Retry!", Toast.LENGTH_LONG).show();

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
                long imagename = System.currentTimeMillis();
                params.put("aadharFront", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap1)));
                params.put("aadharBack", new DataPart(imagename+"2"+ ".png", getFileDataFromDrawable(bitmap2)));
                return params;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<String, String>();
                EditText aadharno = (EditText) getView().findViewById(R.id.AadharNoEditText);
                String aadhar = aadharno.getText().toString();
                params.put("aadhar",aadhar);
                return params;
            }


        };

        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);

    }
}