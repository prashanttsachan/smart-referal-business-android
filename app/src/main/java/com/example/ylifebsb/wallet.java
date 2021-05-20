package com.example.ylifebsb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class wallet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        TextView name = (TextView) findViewById(R.id.userNameWalletTextView);
        DBHelper db = new DBHelper(this);
        Cursor c = db.getdata();
        c.moveToNext();
        name.setText(c.getString(c.getColumnIndex("username")));
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),homelayout.class);
        startActivity(i);
        super.onBackPressed();
    }
}