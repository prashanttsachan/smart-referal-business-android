package com.example.ylifebsb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.R.color.white;

public class homelayout extends AppCompatActivity  {
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    FragmentManager fm = getSupportFragmentManager();
    Button upgrade;
    int i = 0 ;

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
        else{
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            View hview = nav.getHeaderView(0);
            TextView name = (TextView) hview.findViewById(R.id.headernameTextView);
            TextView email = (TextView) hview.findViewById(R.id.headeremailTextview);
            TextView walletbalance = (TextView) findViewById(R.id.walletBalanceTextView);
            upgrade = findViewById(R.id.upgradeAccountBtn);


            //API call to get data and show it on the screen
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
                        boolean upgraded = user.getBoolean("upgraded");
                        walletbalance.setText(user.getString("wallet")+" INR");
                        name.setText(user.getString("firstname")+" "+user.getString("lastname"));
                        email.setText(user.getString("email"));
                        TextView usernameHomeScreen = (TextView) findViewById(R.id.userNameHomeTextView);
                        usernameHomeScreen.setText(user.getString("firstname")+" "+user.getString("lastname"));

                        if(!(upgraded)){
                            upgrade.setVisibility(View.VISIBLE);
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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
                    }
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
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
        }
        super.onStart();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homelayout);
        DBHelper db = new DBHelper(this);
        Cursor c = db.getdata();
        c.moveToNext();
        upgrade = findViewById(R.id.upgradeAccountBtn);
        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                UpgradeAccount();
            }
        });
        nav =(NavigationView) findViewById(R.id.headermenu);
        View hview = nav.getHeaderView(0);
        CircleImageView circleimg = (CircleImageView) hview.findViewById(R.id.userimagecircularview);
        circleimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),userProfile.class);
                startActivity(i);
            }
        });
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        toggle =  new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:{
                        toolbar.setTitle("YLIFE-BSB");
                        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i)
                            getSupportFragmentManager().popBackStack();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.menu_wallet:{
                        toolbar.setTitle("Wallet");
                        fm.beginTransaction().replace(R.id.homeFrameLayout, new mywallet()).addToBackStack(null).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.menu_transaction:{
                        toolbar.setTitle("Transaction");
                        fm.beginTransaction().replace(R.id.homeFrameLayout, new Tansaction()).addToBackStack(null).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.menu_aadhar:{
                        toolbar.setTitle("Aadhar-KYC");
                        fm.beginTransaction().replace(R.id.homeFrameLayout, new aadhar()).addToBackStack(null).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.menu_pan:{
                        toolbar.setTitle("PAN-KYC");
                        fm.beginTransaction().replace(R.id.homeFrameLayout, new pancardkyc()).addToBackStack(null).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.menu_bank:{
                        toolbar.setTitle("Bank Information");
                        fm.beginTransaction().replace(R.id.homeFrameLayout, new Accountinfo()).addToBackStack(null).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.menu_logout:{
                        db.logout();
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;}
                    case R.id.menu_level:{
                        toolbar.setTitle("Level");
                        fm.beginTransaction().replace(R.id.homeFrameLayout, new Levels()).addToBackStack(null).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;}
                    case R.id.menu_transfertobank:{
                        toolbar.setTitle("Transfer To Bank");
                        fm.beginTransaction().replace(R.id.homeFrameLayout, new transfettobank()).addToBackStack(null).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;}
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(i>0){
            fm.popBackStack();
            i=0;
        }else
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
        if(fm.getBackStackEntryCount()>0) {
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i)
                fm.popBackStack();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("YLIFE-BSB");
        }else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    public void UpgradeAccount(){
        StringRequest request = new StringRequest(Request.Method.POST,"https://srbn.herokuapp.com/user/upgrade-account", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Intent i = new Intent(getApplicationContext(),homelayout.class);
                Toast.makeText(getApplicationContext(),"Successfully Upgraded!",Toast.LENGTH_SHORT).show();
                startActivity(i);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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
                }else {
                    message = "Failed To Upgrade !";
                }
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
            }
        }) {
            //This is for Headers
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                DBHelper db = new DBHelper(getApplicationContext());
                Cursor c = db.getdata();
                c.moveToNext();
                String token = c.getString(c.getColumnIndex("token"));
                headers.put("Authorization","Bearer "+ token);
                return headers;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

}