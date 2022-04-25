package com.example.olio_ht;

import static com.example.olio_ht.HashFunction.getHash;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Connection;

public class DatabaseHelp extends SQLiteOpenHelper {

    public static final String DBNAME = "Login.db";

    public DatabaseHelp(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT, iscurrentuser TEXT, area1 TEXT, area2 TEXT, area3 TEXT, salt TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.close();
    }

    public Boolean insertData(String username, String password, String iscurrentuser, String area1, String area2, String area3, String salt) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("iscurrentuser", iscurrentuser);
        contentValues.put("area1", area1);
        contentValues.put("area2", area2);
        contentValues.put("area3", area3);
        contentValues.put("salt", salt);
        long result = MyDB.insert("users", null, contentValues);
        MyDB.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[] {username});
        if (cursor.getCount()>0) {
            cursor.close();
            MyDB.close();
            return true;
        } else {
            cursor.close();
            MyDB.close();
            return false;
        }

    }

    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if (cursor.getCount()>0) {
            cursor.close();
            MyDB.close();
            return true;
        } else {
            cursor.close();
            MyDB.close();
            return false;
        }
    }

    public void makeCurrent(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("Update users set iscurrentuser='false' where iscurrentuser='true'", new String[] {});
        MyDB.execSQL("Update users set iscurrentuser='true' where username = ? and password = ?", new String[] {username, password});
        MyDB.close();
        return;
    }

    public String getUsername() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where iscurrentuser='true' ", new String[] {});
        cursor.moveToFirst();
        @SuppressLint("Range") String currentUsername = cursor.getString(cursor.getColumnIndex("username"));
        cursor.close();
        MyDB.close();
        return currentUsername;
    }

    public void changeArea(String area, int i) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (i == 1) {
            cv.put("area1", area);
        } else if (i == 2) {
            cv.put("area2", area);
        } else if (i == 3) {
            cv.put("area3", area);
        }
        int count = MyDB.update("users" , cv, "iscurrentuser = 'true'", new String[]{});
        System.out.println("Count: " + count);
        MyDB.close();
    }

    public String getAll() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where iscurrentuser='true' ", new String[] {});
        cursor.moveToFirst();
        @SuppressLint("Range") String all = cursor.getString(cursor.getColumnIndex("username")) + " "
                + cursor.getString(cursor.getColumnIndex("password")) + " "
                + cursor.getString(cursor.getColumnIndex("iscurrentuser")) + " "
                + cursor.getString(cursor.getColumnIndex("area1")) + " "
                + cursor.getString(cursor.getColumnIndex("area2")) + " "
                + cursor.getString(cursor.getColumnIndex("area3")) + " "
                + cursor.getString(cursor.getColumnIndex("salt")) + " ";
        cursor.close();
        MyDB.close();
        return all;
    }

    public String getArea(int i) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where iscurrentuser='true' ", new String[] {});
        cursor.moveToFirst();
        String areax = "area1";
        if (i == 2) {
            areax = "area2";
        } else if (i == 3) {
            areax = "area3";
        }
        @SuppressLint("Range") String area = cursor.getString(cursor.getColumnIndex(areax));
        cursor.close();
        MyDB.close();
        return area;
    }

    public String getSalt(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[] {username});
        if (!cursor.moveToFirst()) {
            String fail = "";
            return(fail);
        }
        @SuppressLint("Range") String salt = cursor.getString(cursor.getColumnIndex("salt"));
        cursor.close();
        MyDB.close();
        return salt;
    }
}
