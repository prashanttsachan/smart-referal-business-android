package com.example.ylifebsb;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class userProfile extends AppCompatActivity {
    int SELECT_PHOTO = 1;
    Uri uri;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Button choose = (Button) findViewById(R.id.chooseImageBtn);
        img = (ImageView) findViewById(R.id.profile_image);

        //database initialisation
        DBHelper db = new DBHelper(this);
        Cursor c = db.getdata();
        c.moveToFirst();
        String token = c.getString(c.getColumnIndex("token"));
        Button changepassword = (Button) findViewById(R.id.changePasswordBtn);

        //changepassword Activity call on changepassword button is pressed
        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent changepass = new Intent(getApplicationContext(),changePassword.class);
                startActivity(changepass);
            }
        });



        //Api request to get data from the server
        StringRequest request = new StringRequest(Request.Method.GET,"https://srbn.herokuapp.com/auth/checkauth", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                //Data is shown in the profile throw api
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject data = new JSONObject();
                    data = jsonObject.getJSONObject("data");
                    JSONObject user = new JSONObject();
                    user = data.getJSONObject("user");
                    EditText name = (EditText) findViewById(R.id.userNameProfileEditText);
                    EditText phoneno = (EditText) findViewById(R.id.userMobileNoProfileEdittext);
                    EditText mail  = (EditText) findViewById(R.id.userMailProfileEdittext);
                    name.setText(user.getString("firstname")+" "+user.getString("lastname"));
                    phoneno.setText(user.getString("mobile"));
                    mail.setText(user.getString("email"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
            }
        }) {
            //This is for Headers
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer "+ token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);




        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() !=null){
        uri = data.getData();
        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            img.setImageBitmap(bitmap);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),homelayout.class);
        startActivity(i);

        super.onBackPressed();
    }
}