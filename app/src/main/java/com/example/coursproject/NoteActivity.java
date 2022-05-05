package com.example.coursproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.appcompat.widget.SearchView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteActivity extends AppCompatActivity {

    DBHelper dbHelper;
    EditText NoteEt;
    EditText TitleEt;

    MenuItem doneMI, backMI, undoMI, cancelMI, remindMI, deleteMI;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);




        dbHelper = new DBHelper(this);

        NoteEt = (EditText) findViewById(R.id.ContentInput);
        NoteEt.setOnClickListener(this::onClick);
        TitleEt = (EditText) findViewById(R.id.TitleInput);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        doneMI = menu.findItem(R.id.done);
        backMI = menu.findItem(R.id.back);
        undoMI = menu.findItem(R.id.undoArrow);
        cancelMI = menu.findItem(R.id.cancelArrow);
        remindMI = menu.findItem(R.id.reminde);
        deleteMI = menu.findItem(R.id.delete);

        doneMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        remindMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        deleteMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        undoMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        cancelMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        backMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        switch  (item.getItemId()){
            case R.id.done:
                Date today = new Date();
                SimpleDateFormat formatForDateNow = new SimpleDateFormat("yyyy.MM.dd");

                String note = NoteEt.getText().toString();
                String title = TitleEt.getText().toString();
                String date = formatForDateNow.format(today);

                contentValues.put(DBHelper.KEY_NOTE, note);
                contentValues.put(DBHelper.KEY_TITLE, title);
          /*      contentValues.put(DBHelper.KEY_DATENT, date);*/
                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                Log.d("mLog",  "title = " + title + ", note = " + note + ", date  = " + date);
                return true;

            case R.id.back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                dbHelper.close();
                finish();
                return true;
            case R.id.undoArrow:

                return true;
            case R.id.cancelArrow:

                return true;
            case R.id.reminde:

                return true;
            case R.id.delete:

                return true;
            default:
                dbHelper.close();
                return super.onOptionsItemSelected(item);

        }

    }
    public  void onClick (View v){
        switch (v.getId()){
            case R.id.ContentInput:
                doneMI.setShowAsAction(0);
                break;
            case R.id.TitleInput:
                doneMI.setShowAsAction(0);
                break;
        }
    }
}