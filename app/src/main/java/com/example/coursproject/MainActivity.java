package com.example.coursproject;

import static com.example.coursproject.Note.Note;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import androidx.gridlayout.widget.GridLayout;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd,  btnClear;

    DBHelper dbHelper;
    GridLayout gridView;
    List<Note> notes = new ArrayList<Note>();
    private LinearLayout.LayoutParams layoutparams;
    private LinearLayout.LayoutParams TextViewparams;
    int counter = 0;
    List<Integer> CardIds = new ArrayList<Integer>();

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);




        btnAdd = (Button) findViewById(R.id.addNoteButton);
        btnAdd.setOnClickListener(this::onClick);

        btnClear = (Button) findViewById(R.id.clear);
        btnClear.setOnClickListener(this::onClick);

        gridView = findViewById(R.id.gridLayout);

        dbHelper = new DBHelper(this);



        SQLiteDatabase database = dbHelper.getWritableDatabase();



        String query = "SELECT " + dbHelper.KEY_ID + ", "
                + dbHelper.KEY_TITLE + ", " + dbHelper.KEY_NOTE + /*", " + dbHelper.KEY_DATENT +*/ " FROM " + dbHelper.TABLE_CONTACTS;
        Cursor cursor2 = database.rawQuery(query, null);

        if(cursor2.moveToFirst()){
            int idIndex = cursor2.getColumnIndex(DBHelper.KEY_ID);
            int noteIndex = cursor2.getColumnIndex(DBHelper.KEY_NOTE);
            int titleIndex = cursor2.getColumnIndex(DBHelper.KEY_TITLE);
          /*  int dateIndex = cursor2.getColumnIndex(DBHelper.KEY_DATENT);*/
            do{
                CardIds.add(counter);
                Note SomeNote = new Note();
                SomeNote.ID = cursor2.getInt(idIndex);
                SomeNote.Title = cursor2.getString(titleIndex);
                SomeNote.Note = cursor2.getString(noteIndex);
           /*     SomeNote.Date = cursor2.getString(dateIndex);*/
                notes.add(SomeNote);
                Log.d("mLog",
                        "ID = " + cursor2.getInt(idIndex)
                                + ", title = " + cursor2.getString(titleIndex)
                                + ", note = " + cursor2.getString(noteIndex)
                        /*+ ", date = " + cursor2.getString(dateIndex)*/);
                CardView cardView = new CardView(this);
                layoutparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutparams.setMargins(10,10,0,0);
                TextViewparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                TextViewparams.setMargins(10,50,0,0);

                cardView.setId(counter);
                cardView.setLayoutParams(layoutparams);
                cardView.setRadius(15);
                cardView.setPadding(25, 25, 25, 25);
                cardView.setMaxCardElevation(30);
                cardView.setMaxCardElevation(6);
                TextView TitleTV = new TextView(this);
                TextView NoteTV = new TextView(this);
                TextView DateTV = new TextView(this);
                DateTV.setLayoutParams(TextViewparams);
                TitleTV.setLayoutParams(layoutparams);
                NoteTV.setLayoutParams(TextViewparams);
/*                DateTV.setText(cursor2.getString(dateIndex));*/
                NoteTV.setText(cursor2.getString(noteIndex));
                TitleTV.setText(cursor2.getString(titleIndex));
                cardView.addView(TitleTV);
                cardView.addView(NoteTV);
                gridView.setOrientation(1);
                gridView.addView(cardView);
                counter++;
            } while (cursor2.moveToNext());


        }

    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v){


        SQLiteDatabase database = dbHelper.getWritableDatabase();


        switch  (v.getId()){
            case R.id.addNoteButton:
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.clear:
                gridView.removeAllViews();
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                break;
        }
        dbHelper.close();
    }


    public void OnItemClick (AdapterView<?> parent, View v, int position, long id) {
        Note nt = new Note();
        nt.ID = v.getId();


    }
}