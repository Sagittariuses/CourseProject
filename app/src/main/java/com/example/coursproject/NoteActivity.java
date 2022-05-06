package com.example.coursproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.view.menu.MenuPresenter;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.appcompat.widget.SearchView;
import androidx.gridlayout.widget.GridLayout;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.TextView;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
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
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class NoteActivity extends AppCompatActivity {

    DBHelper dbHelper;
    EditText NoteEt;
    EditText TitleEt;
    TextViewUndoRedo helperNote;
    TextViewUndoRedo helperTitle;
    MenuItem doneMI, backMI, undoMI, redoMI, remindMI, deleteMI;
    boolean chek;

    @SuppressLint("WrongConstant")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);



        dbHelper = new DBHelper(this);

        NoteEt = (EditText) findViewById(R.id.ContentInput);
        NoteEt.setOnClickListener(this::onClick);
        TitleEt = (EditText) findViewById(R.id.TitleInput);

        TitleEt.setText(Memory.EditTitle);
        NoteEt.setText(Memory.EditNote);

        helperNote = new TextViewUndoRedo(NoteEt);
        helperTitle = new TextViewUndoRedo(TitleEt);

        NoteEt.setScroller(new Scroller(getApplicationContext()));
        NoteEt.setVerticalScrollBarEnabled(true);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        doneMI = menu.findItem(R.id.done);
        backMI = menu.findItem(R.id.back);
        undoMI = menu.findItem(R.id.undo);
        redoMI= menu.findItem(R.id.redo);
        remindMI = menu.findItem(R.id.reminde);
        deleteMI = menu.findItem(R.id.delete);

        doneMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        remindMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        deleteMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        undoMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        redoMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        backMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Date today = new Date();
        SimpleDateFormat formatForDateNow = new SimpleDateFormat( "'Date: ' yyyy.MM.dd ' time: ' hh:mm:ss");
        switch  (item.getItemId()){
            case R.id.done:
                if(!Memory.EditChek){
                    String note = NoteEt.getText().toString();
                    String title = TitleEt.getText().toString();
                    String date = formatForDateNow.format(today);
                    contentValues.put(DBHelper.KEY_NOTE, note);
                    contentValues.put(DBHelper.KEY_TITLE, title);
                    contentValues.put(DBHelper.KEY_CREATED_AT, date);
                    database.insert(DBHelper.TABLE_NOTES, null, contentValues);
                    Log.d("mLog",  "Click: title = " + title + ", note = " + note + ", date  = " + date);
                } else
                {
                    String note = NoteEt.getText().toString();
                    String title = TitleEt.getText().toString();
                    String date = formatForDateNow.format(today);
                    contentValues.put(DBHelper.KEY_NOTE, note);
                    contentValues.put(DBHelper.KEY_TITLE, title);
                    contentValues.put(DBHelper.KEY_CREATED_AT, date);

                    String queryedit =
                            "SELECT "
                                    + dbHelper.KEY_ID + ", "
                                    + dbHelper.KEY_TITLE + ", "
                                    + dbHelper.KEY_NOTE + ", "
                                    + dbHelper.KEY_CREATED_AT
                                    + " FROM "
                                    + dbHelper.TABLE_NOTES
                                    + " WHERE "
                                    + dbHelper.KEY_ID
                                    + " = "
                                    + (Memory.EditID);

                    Cursor cursor = database.rawQuery(queryedit, null);

                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int noteIndex = cursor.getColumnIndex(DBHelper.KEY_NOTE);
                    int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                    int dateIndex = cursor.getColumnIndex(DBHelper.KEY_CREATED_AT);

                    String[] values = new String[4];
                    values[0] = cursor.getString(idIndex);
                    values[1] = cursor.getString(titleIndex);
                    values[2] = cursor.getString(noteIndex);
                    values[3] = cursor.getString(dateIndex);

                    database.update(DBHelper.TABLE_NOTES,contentValues,null,values );
                }

                return true;

            case R.id.back:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                dbHelper.close();
                finish();
                return true;

            case R.id.redo:
                if(chek)
                    helperNote.redo();
                else
                    helperTitle.redo();
                return true;

            case R.id.undo:
                if(chek)
                    helperNote.undo();
                else
                    helperTitle.undo();
                return true;

            case R.id.reminde:
                return true;

            case R.id.delete:
                String queryedit =
                        "DELETE " + " FROM "
                                + dbHelper.TABLE_NOTES
                                + " WHERE "
                                + dbHelper.KEY_ID
                                + " = "
                                + (Memory.EditID);
                Cursor cursor = database.rawQuery(queryedit, null);
                int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                int noteIndex = cursor.getColumnIndex(DBHelper.KEY_NOTE);
                int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                int dateIndex = cursor.getColumnIndex(DBHelper.KEY_CREATED_AT);
                String[] values = new String[4];
                values[0] = cursor.getString(idIndex);
                values[1] = cursor.getString(titleIndex);
                values[2] = cursor.getString(noteIndex);
                values[3] = cursor.getString(dateIndex);
                database.delete(DBHelper.TABLE_NOTES,null, values);
                return true;

            default:
                dbHelper.close();
                return super.onOptionsItemSelected(item);

        }

    }


    public  void onClick (View v){
        switch (v.getId()){
            case R.id.ContentInput:
                chek=true;
                doneMI.setShowAsAction(0);
                break;
            case R.id.TitleInput:
                chek = false;
                doneMI.setShowAsAction(0);
                break;
        }
    }
}
