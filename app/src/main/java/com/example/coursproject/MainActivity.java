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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import androidx.gridlayout.widget.GridLayout;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd, btnRead, btnClear;
    EditText NoteEt;
    EditText TitleEt;
    DBHelper dbHelper;
    GridLayout gridView;
    List<Note> notes = new ArrayList<Note>();
    private LinearLayout.LayoutParams layoutparams;
    int counter = 0;
    List<Integer> CardIds = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.addNoteButton);
        btnAdd.setOnClickListener(this);

        /*btnRead = (Button) findViewById(R.id.Read);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.Clear);
        btnClear.setOnClickListener(this);*/

        gridView = findViewById(R.id.gridLayout);
        NoteEt = (EditText) findViewById(R.id.SearchInput);
        /*TitleEt = (EditText) findViewById(R.id.TitleInput);*/
        dbHelper = new DBHelper(this);


        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String query = "SELECT " + dbHelper.KEY_ID + ", "
                + dbHelper.KEY_TITLE + ", " + dbHelper.KEY_NOTE + " FROM " + dbHelper.TABLE_CONTACTS;
        Cursor cursor2 = database.rawQuery(query, null);


        if(cursor2.moveToFirst()){
            int idIndex = cursor2.getColumnIndex(DBHelper.KEY_ID);
            int noteIndex = cursor2.getColumnIndex(DBHelper.KEY_NOTE);
            int titleIndex = cursor2.getColumnIndex(DBHelper.KEY_TITLE);
            do{
                CardIds.add(counter);
                Note SomeNote = new Note();
                SomeNote.ID = cursor2.getInt(idIndex);
                SomeNote.Title = cursor2.getString(titleIndex);
                SomeNote.Note = cursor2.getString(noteIndex);
                notes.add(SomeNote);
                Log.d("mLog", "ID = " + cursor2.getInt(idIndex) + ", title = " + cursor2.getString(titleIndex) + ", note = " + cursor2.getString(noteIndex));
                CardView cardView = new CardView(this);
                layoutparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutparams.setMargins(150,50,0,0);
                cardView.setId(counter);
                cardView.setOnClickListener(this::onClick);
                cardView.setLayoutParams(layoutparams);
                cardView.setRadius(15);
                cardView.setPadding(25, 25, 25, 25);
                cardView.setMaxCardElevation(30);
                cardView.setMaxCardElevation(6);
                TextView TitleTV = new TextView(this);
                TextView NoteTV = new TextView(this);
                TitleTV.setText(cursor2.getString(titleIndex));
                TitleTV.setLayoutParams(layoutparams);
                NoteTV.setText(cursor2.getString(noteIndex));
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

       /* String note = NoteEt.getText().toString();
        String title = TitleEt.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();*/

        switch  (v.getId()){
/*            case R.id.counter: добавить id карточки
                break;*/
            case R.id.addNoteButton:
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                finish();
                break;
            /*case R.id.Read:
                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null,null,null,null,null);
                int counter = 0;
                gridView.removeAllViews();
                if(cursor.moveToFirst()){
                    do{
                        Note SomeNote = new Note();
                        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                        int noteIndex = cursor.getColumnIndex(DBHelper.KEY_NOTE);
                        int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                        SomeNote.ID = cursor.getInt(idIndex);
                        SomeNote.Title = cursor.getString(titleIndex);
                        SomeNote.Note = cursor.getString(noteIndex);
                        notes.add(SomeNote);
                        CardView cardView = new CardView(this);
                        layoutparams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutparams.setMargins(150,50,0,0);
                        cardView.setLayoutParams(layoutparams);
                        cardView.setRadius(15);
                        cardView.setPadding(25, 25, 25, 25);
                        cardView.setMaxCardElevation(30);
                        cardView.setMaxCardElevation(6);
                        TextView TitleTV = new TextView(this);
                        TextView NoteTV = new TextView(this);
                        TitleTV.setText(SomeNote.Title);
                        TitleTV.setLayoutParams(layoutparams);
                        NoteTV.setText(SomeNote.Note);
                        cardView.addView(NoteTV);
                        cardView.addView(TitleTV);
                        gridView.setOrientation(1);
                        gridView.addView(cardView);
                        counter++;

                        Log.d("mLog", "ID = " + cursor.getInt(idIndex) + ", title = " + cursor.getString(titleIndex) + ", note = " + cursor.getString(noteIndex));
                    } while (cursor.moveToNext());


                    NoteEt.setText(null);
                    TitleEt.setText(null);
                } else
                    Log.d("mLog", "0 rows");
                cursor.close();
                break;
            case R.id.Clear:
                gridView.removeAllViews();
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                break;*/
        }
        dbHelper.close();
    }
}