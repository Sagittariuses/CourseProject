package com.example.coursproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.sql.Date;

public class DBHelper extends SQLiteOpenHelper {

    public   static  final  int DATABASE_VERSION=1;
    public   static  final  String DATABASE_NAME="notesDB";
    public   static  final  String TABLE_NOTES="notes";

    public   static  final  String KEY_ID ="_id";
    public   static  final  String KEY_TITLE ="title";
    public   static  final  String KEY_NOTE ="note";
    public static final String KEY_CREATED_AT = "created_at";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CreateTable =
                "create table "
                        + TABLE_NOTES + "("
                        + KEY_ID + " integer primary key,"
                        + KEY_TITLE + " text,"
                        + KEY_NOTE + " text,"
                        + KEY_CREATED_AT + " text"
                        + ")";

        sqLiteDatabase.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NOTES);

        onCreate(sqLiteDatabase);
    }

}
