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
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import androidx.gridlayout.widget.GridLayout;

import com.google.type.Date;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    Button btnAdd, btnClear;

    DBHelper dbHelper;
    GridLayout gridView;
    List<Note> notes = new ArrayList<Note>();
    private LinearLayout.LayoutParams layoutparams;
    private LinearLayout.LayoutParams TitleParams;
    private LinearLayout.LayoutParams Noteparams;
    private LinearLayout.LayoutParams Dateparams;
    int counter = 1;
    SQLiteDatabase database;
    List<Integer> CardIds = new ArrayList<Integer>();

    @Override
    @SuppressLint("NewApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();


        btnAdd = (Button) findViewById(R.id.addNoteButton);
        btnAdd.setOnClickListener(this::onClick);

        btnClear = (Button) findViewById(R.id.clear);
        btnClear.setOnClickListener(this::onClick);

        gridView = (GridLayout) findViewById(R.id.gridLayout);
        gridView.setOnClickListener(this::onClick);


        String queryload = "SELECT " + dbHelper.KEY_ID + ", "
                + dbHelper.KEY_TITLE + ", " + dbHelper.KEY_NOTE + ", " + dbHelper.KEY_CREATED_AT + " FROM " + dbHelper.TABLE_NOTES;

        Cursor cursor2 = database.rawQuery(queryload, null);

        if (cursor2.moveToFirst()) {
            int idIndex = cursor2.getColumnIndex(DBHelper.KEY_ID);
            int noteIndex = cursor2.getColumnIndex(DBHelper.KEY_NOTE);
            int titleIndex = cursor2.getColumnIndex(DBHelper.KEY_TITLE);
            int dateIndex = cursor2.getColumnIndex(DBHelper.KEY_CREATED_AT);
            do {
                CardIds.add(counter);
                Note SomeNote = new Note();
                SomeNote.ID = cursor2.getInt(idIndex);
                SomeNote.Title = cursor2.getString(titleIndex);
                SomeNote.Note = cursor2.getString(noteIndex);
                SomeNote.Created_at = cursor2.getString(dateIndex);
                notes.add(SomeNote);
                Log.d("mLog",
                        "ID = " + cursor2.getInt(idIndex)
                                + ", title = " + cursor2.getString(titleIndex)
                                + ", note = " + cursor2.getString(noteIndex)
                                + ", date = " + cursor2.getString(dateIndex));
                CardView cardView = new CardView(this);

                layoutparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutparams.setMargins(10, 10, 0, 0);

                TitleParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                TitleParams.setMargins(80, 10, 0, 0);

                Noteparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                Noteparams.setMargins(45, 180, 0, 0);

                Dateparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                Dateparams.setMargins(45, 100, 0, 10);

                cardView.setId(counter);
                cardView.setLayoutParams(layoutparams);
                cardView.setRadius(50);
                cardView.setPadding(25, 25, 25, 25);
                cardView.setMaxCardElevation(30);
                cardView.setMaxCardElevation(6);


                TextView TitleTV = new TextView(this);
                TextView NoteTV = new TextView(this);
                TextView DateTV = new TextView(this);

                DateTV.setLayoutParams(Dateparams);
                TitleTV.setLayoutParams(TitleParams);
                NoteTV.setLayoutParams(Noteparams);

                DateTV.setText(cursor2.getString(noteIndex));
                DateTV.setTextSize(18);

                NoteTV.setText(cursor2.getString(dateIndex));
                NoteTV.setTextSize(18);

                TitleTV.setText(cursor2.getString(titleIndex));
                TitleTV.setTextSize(20);
                TitleTV.setTextColor(Color.rgb(245, 126, 104));

                cardView.addView(TitleTV);
                cardView.addView(NoteTV);
                cardView.addView(DateTV);
                gridView.setOrientation(1);
                gridView.addView(cardView);

                counter++;
            } while (cursor2.moveToNext());





        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.gridLayout:
                int rem = 1;
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
                                + (rem);
                Cursor cursor = database.rawQuery(queryedit, null);
                if (cursor.moveToFirst()) {
                    int editidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int editcreated_atIndex = cursor.getColumnIndex(DBHelper.KEY_CREATED_AT);
                    int editnoteIndex = cursor.getColumnIndex(DBHelper.KEY_NOTE);
                    int edittitleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                    Log.d("mLog",
                            "ID = " + cursor.getInt(editidIndex)
                                    + ", title = " + cursor.getString(edittitleIndex)
                                    + ", note = " + cursor.getString(editnoteIndex)
                                    + ", date = " + cursor.getString(editcreated_atIndex));
                    Memory.EditID = cursor.getString(editidIndex);
                    Memory.EditTitle = cursor.getString(edittitleIndex);
                    Memory.EditNote = cursor.getString(editnoteIndex);
                    Memory.EditChek = true;

                    Intent in = new Intent(MainActivity.this, NoteActivity.class);
                    startActivity(in);

                }
                break;
            case R.id.addNoteButton:
                Memory.EditTitle = "";
                Memory.EditNote = "";
                Intent intent = new Intent(this, NoteActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.clear:
                gridView.removeAllViews();
                database.delete(DBHelper.TABLE_NOTES, null, null);
                break;
                }
                dbHelper.close();
            }
        }

