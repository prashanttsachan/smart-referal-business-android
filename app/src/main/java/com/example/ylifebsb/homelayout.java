package com.example.ylifebsb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class homelayout extends AppCompatActivity {
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homelayout);
        DBHelper db = new DBHelper(this);
        Cursor c = db.getdata();
        c.moveToNext();
        nav =(NavigationView) findViewById(R.id.headermenu);
        View hview = nav.getHeaderView(0);
        TextView name = (TextView) hview.findViewById(R.id.headernameTextView);
        TextView email = (TextView) hview.findViewById(R.id.headeremailTextview);
        CircleImageView circleimg = (CircleImageView) hview.findViewById(R.id.userimagecircularview);
        circleimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),userProfile.class);
                startActivity(i);
            }
        });
        String username = c.getString(c.getColumnIndex("username"));
        String useremail = c.getString(c.getColumnIndex("email"));
        email.setText(useremail);
        name.setText(username);
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
                        Intent i = new Intent(getApplicationContext(),homelayout.class);
                        startActivity(i);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.menu_wallet:{
                        Intent i = new Intent(getApplicationContext(),wallet.class);
                        startActivity(i);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.menu_transaction:{
                        Intent i = new Intent(getApplicationContext(),homelayout.class);
                        Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    }
                    case R.id.menu_KYC:{
                        Intent i = new Intent(getApplicationContext(),KYCadhaar.class);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(i);
                        break;
                    }
                    case R.id.menu_logout:{
                         db.logout();
                         Intent i = new Intent(getApplicationContext(),MainActivity.class);
                         startActivity(i);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;}
                    case R.id.menu_sponsermember:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout, new member()).addToBackStack(null).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;}
                    case R.id.menu_downlinemember:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.homeFrameLayout, new member()).addToBackStack(null).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;}
                }
                return true;
            }
        });




    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}