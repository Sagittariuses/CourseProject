package com.example.coursproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public   static  final  int DATABASE_VERSION=1;
    public   static  final  String DATABASE_NAME="contactDB";
    public   static  final  String TABLE_CONTACTS="contacts";

    public   static  final  String KEY_ID ="_id";
    public   static  final  String KEY_TITLE ="title";
    public   static  final  String KEY_NOTE ="note";
    /*public   static  final  String KEY_DATENT ="datent";*/

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_CONTACTS + "("
                + KEY_ID + " integer primary key,"
                + KEY_TITLE + " text,"
                + KEY_NOTE + " text"
                /*+ KEY_DATENT + " text"*/
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_CONTACTS);

        onCreate(sqLiteDatabase);
    }
}
