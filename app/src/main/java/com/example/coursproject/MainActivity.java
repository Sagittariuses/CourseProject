package com.example.coursproject;

import static com.example.coursproject.Note.Note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
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
import java.util.List;
import androidx.gridlayout.widget.GridLayout;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd, btnRead, btnClear;
    EditText NoteEt;
    EditText TitleEt;
    DBHelper dbHelper;
    GridLayout gridView;
    Note SomeNote = new Note();
    List<Note> notes = new ArrayList<Note>();
    private LinearLayout.LayoutParams layoutparams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.addNoteButton);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.Read);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.Clear);
        btnClear.setOnClickListener(this);

        gridView = findViewById(R.id.gridLayout);
        NoteEt = (EditText) findViewById(R.id.SearchInput);
        TitleEt = (EditText) findViewById(R.id.TitleInput);
        dbHelper = new DBHelper(this);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v){

        String note = NoteEt.getText().toString();
        String title = TitleEt.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        switch  (v.getId()){
            case R.id.addNoteButton:
                contentValues.put(DBHelper.KEY_NOTE, note);
                contentValues.put(DBHelper.KEY_TITLE, title);

                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                break;
            case R.id.Read:
                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null,null,null,null,null);

                if(cursor.moveToFirst()){
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int noteIndex = cursor.getColumnIndex(DBHelper.KEY_NOTE);
                    int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                    do{
                        SomeNote.ID = cursor.getInt(idIndex);
                        SomeNote.Title = cursor.getString(titleIndex);
                        SomeNote.Note = cursor.getString(noteIndex);
                        notes.add(SomeNote);

                        Log.d("mLog", "ID = " + cursor.getInt(idIndex) + ", title = " + cursor.getString(titleIndex) + ", note = " + cursor.getString(noteIndex));
                    } while (cursor.moveToNext());

                    for(Note nt:notes){
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
                        TitleTV.setText(nt.Title);
                        TitleTV.setLayoutParams(layoutparams);
                        NoteTV.setText(nt.Note);
                        cardView.addView(TitleTV);
                        cardView.addView(NoteTV);
                        gridView.setOrientation(1);
                        gridView.addView(cardView);

                    }
                    NoteEt.setText(null);
                    TitleEt.setText(null);
                } else
                    Log.d("mLog", "0 rows");
                cursor.close();
                break;
            case R.id.Clear:
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                break;
        }
        dbHelper.close();
    }
}