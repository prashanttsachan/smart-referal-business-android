package com.example.ylifebsb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table user(token Text primary key ,username Text,email Text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("drop table  if exist inventory");

    }
    public Boolean insert(String token,String username,String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("token",token);
        contentValues.put("username",username);
        contentValues.put("email",email);
        long result = db.insert("user",null,contentValues);
        if(result==-1){
            return false;
        }else {
            return true;
        }
    }
    public Cursor getdata()
    {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user",null);
        return cursor;
    }

    public void logout(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("Delete from user");
    }

}