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
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT, " +
                "iscurrentuser TEXT, area1 TEXT, area2 TEXT, area3 TEXT, salt TEXT," +
                " lastActivity TEXT, nightmode TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.close();
    }

    // Takes all user details as parameters and adds as single row to user table
    public Boolean insertData(String username, String password,
                              String iscurrentuser, String area1, String area2, String area3, String salt,
                              String lastActivity, String nightmode) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("iscurrentuser", iscurrentuser);
        contentValues.put("area1", area1);
        contentValues.put("area2", area2);
        contentValues.put("area3", area3);
        contentValues.put("salt", salt);
        contentValues.put("lastActivity", lastActivity);
        contentValues.put("nightmode", nightmode);
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

    // Makes the user defined by parameter the only one in database with 'true' in iscurrentuser-column
    public void makeCurrent(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("Update users set iscurrentuser='false' where iscurrentuser='true'", new String[] {});
        MyDB.execSQL("Update users set iscurrentuser='true' where username = ? and password = ?", new String[] {username, password});
        MyDB.close();
        return;
    }

    // Return the username of the one and only user with iscurrentuser='true'
    public String getUsername() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where iscurrentuser='true' ", new String[] {});
        cursor.moveToFirst();
        @SuppressLint("Range") String currentUsername = cursor.getString(cursor.getColumnIndex("username"));
        cursor.close();
        MyDB.close();
        return currentUsername;
    }

    // Takes an area,e.g. Espoo, and an integer from 1-3 and assigns area to current user's corresponding area column
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

    // Takes integer from 1-3 and returns the current user's corresponding starred area
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
        return area;
    }

    // Takes username and returns their password salt string
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

    // Takes string that indicates an activity and sets the current user's lastActivity column to it
    public void setUserLastActivity(String lastActivity) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("lastActivity", lastActivity);
        MyDB.update("users" , cv, "iscurrentuser = 'true'", new String[]{});
        MyDB.close();
    }

    // Returns string that indicates current user's last activity when application closed
    // This allows the user's last active activity to load on login
    public String getUserLastActivity() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where iscurrentuser='true' ", new String[] {});
        cursor.moveToFirst();
        @SuppressLint("Range") String lastActivity = cursor.getString(cursor.getColumnIndex("lastActivity"));
        cursor.close();
        MyDB.close();
        return lastActivity;
    }

    // Set current user's night mode preference with string parameter
    public void setNightMode(String nightmode) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nightmode", nightmode);
        MyDB.update("users" , cv, "iscurrentuser = 'true'", new String[]{});
        MyDB.close();
    }

    // Get current user's night mode preference
    public String getNightMode() {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where iscurrentuser='true' ", new String[] {});
        cursor.moveToFirst();
        @SuppressLint("Range") String nightmode = cursor.getString(cursor.getColumnIndex("nightmode"));
        cursor.close();
        MyDB.close();
        return nightmode;
    }

    // remove this. Prints all database columns for current user
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
                + cursor.getString(cursor.getColumnIndex("salt")) + " "
                + cursor.getString(cursor.getColumnIndex("lastActivity")) + " "
                + cursor.getString(cursor.getColumnIndex("nightmode")) + " ";
        cursor.close();
        MyDB.close();
        return all;
    }
}
