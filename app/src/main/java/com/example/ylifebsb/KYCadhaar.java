package com.example.ylifebsb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class KYCadhaar extends AppCompatActivity {
    @Override
    protected void onStart() {
        DBHelper db = new DBHelper(this);
        Cursor c = db.getdata();
        c.moveToNext();
        String token = c.getString(c.getColumnIndex("token"));
        if((token.equals("") || token.equals(null))){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
        super.onStart();
    }


    int SELECT_PHOTO = 1;
    Uri uri;
    int i=0;
    DBHelper db = new DBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_k_y_cadhaar);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        Button next = (Button) findViewById(R.id.nextKycAdharBtn);

        EditText aadhar = (EditText) findViewById(R.id.AadharNoEditText);
        ImageView front = (ImageView) findViewById(R.id.frontAadharImageView);
        ImageView back = (ImageView) findViewById(R.id.backAadharImageView);
        TextView alertbox = (TextView) findViewById(R.id.kycAadharAlertBox);
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

                    findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                    SendImage();
                }
            }
        });
        Button choosefront = (Button) findViewById(R.id.chooseFrontImageAadhaarBtn);
        Button chooseback = (Button) findViewById(R.id.chooseBackImageAadhaarBtn);
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

    }
    Bitmap bitmap1;
    Bitmap bitmap2;
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() !=null){
            uri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Bitmap lastBitmap = null;
                if(i==1){
                    ImageView frontimg = (ImageView) findViewById(R.id.frontAadharImageView);
                    bitmap1=bitmap;
                    frontimg.setImageBitmap(bitmap);
                    frontimg.setVisibility(View.VISIBLE);
                }else{
                    ImageView backimg = (ImageView) findViewById(R.id.backAadharImageView);
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
                       findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        try {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            getSupportFragmentManager().beginTransaction().replace(R.id.kycFramelayout, new pancardkyc()).addToBackStack(null).commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                        Toast.makeText(KYCadhaar.this, "Failed", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                Cursor c = db.getdata();
                c.moveToFirst();
                String token = c.getString(c.getColumnIndex("token"));
                headers.put("Authorization", token);
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
                EditText aadharno = (EditText) findViewById(R.id.AadharNoEditText);
                String aadhar = aadharno.getText().toString();
                params.put("aadhar",aadhar);
                return params;
            }


        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);

    }

    public String getToken(){
        Cursor c = db.getdata();
        c.moveToFirst();
        String token = c.getString(c.getColumnIndex("token"));
        return token;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),homelayout.class);
        startActivity(i);

        super.onBackPressed();
    }
}