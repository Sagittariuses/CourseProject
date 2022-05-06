package com.example.coursproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


// multi-choise
// https://startandroid.ru/ru/uroki/vse-uroki-spiskom/83-urok-43-odinochnyj-i-mnozhestvennyj-vybor-v-list.html

public class MainActivity extends AppCompatActivity implements OnClickListener {
    Button btnAdd;
    EditText searchET;
    DBHelper dbHelper;
    LinearLayout linearLayout;
    SQLiteDatabase database;
    @Override
    @SuppressLint({"NewApi", "SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        linearLayout = findViewById(R.id.LayoutForCard);
        linearLayout.setVerticalScrollBarEnabled(true);

        btnAdd = findViewById(R.id.addNoteButton);
        btnAdd.setOnClickListener(this);

        searchET = findViewById(R.id.SearchInput);
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Log.d("mLog", "Text: " + searchET.getText().toString());
                if(searchET.getText().toString().length() == 0){
                    linearLayout.removeAllViewsInLayout();

                    String queryload = "SELECT " + DBHelper.KEY_ID + ", "
                            + DBHelper.KEY_TITLE + ", " + DBHelper.KEY_NOTE + ", " + DBHelper.KEY_CREATED_AT + " FROM " + DBHelper.TABLE_NOTES;

                    @SuppressLint("Recycle")
                    Cursor cursor2 = database.rawQuery(queryload, null);

                    if (cursor2.moveToFirst()) {
                        int idIndex = cursor2.getColumnIndex(DBHelper.KEY_ID);
                        int noteIndex = cursor2.getColumnIndex(DBHelper.KEY_NOTE);
                        int titleIndex = cursor2.getColumnIndex(DBHelper.KEY_TITLE);
                        int dateIndex = cursor2.getColumnIndex(DBHelper.KEY_CREATED_AT);
                        do {
                            Log.d("mLog",
                                    "ID = " + cursor2.getInt(idIndex)
                                            + ", title = " + cursor2.getString(titleIndex)
                                            + ", note = " + cursor2.getString(noteIndex)
                                            + ", date = " + cursor2.getString(dateIndex));
                            CardView cardView = new CardView(MainActivity.this);

                            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            layoutparams.setMargins(10, 10, 0, 0);

                            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            titleParams.setMargins(80, 10, 0, 0);

                            LinearLayout.LayoutParams noteparams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                            );
                            noteparams.setMargins(45, 180, 0, 0);

                            LinearLayout.LayoutParams dateparams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                            );
                            dateparams.setMargins(45, 100, 0, 10);

                            cardView.setId(cursor2.getInt(idIndex));
                            cardView.setLayoutParams(layoutparams);
                            cardView.setRadius(50);
                            cardView.setPadding(25, 25, 25, 25);
                            cardView.setMaxCardElevation(30);
                            cardView.setMaxCardElevation(6);


                            TextView TitleTV = new TextView(MainActivity.this);
                            TextView NoteTV = new TextView(MainActivity.this);
                            TextView DateTV = new TextView(MainActivity.this);

                            DateTV.setLayoutParams(dateparams);
                            TitleTV.setLayoutParams(titleParams);
                            NoteTV.setLayoutParams(noteparams);

                            if (cursor2.getString(noteIndex).length() > 30)
                                DateTV.setText(cursor2.getString(noteIndex).substring(0, 29) + "...");
                            else
                                DateTV.setText(cursor2.getString(noteIndex));
                            DateTV.setTextSize(16);

                            NoteTV.setText(cursor2.getString(dateIndex));
                            NoteTV.setTextSize(18);

                            TitleTV.setText(cursor2.getString(titleIndex));
                            TitleTV.setTextSize(20);
                            TitleTV.setTextColor(Color.rgb(245, 126, 104));
                            if (cursor2.getString(titleIndex).length() > 30)
                                TitleTV.setText(cursor2.getString(titleIndex).substring(0, 20) + "...");
                            else
                                TitleTV.setText(cursor2.getString(titleIndex));

                            cardView.addView(TitleTV);
                            cardView.addView(NoteTV);
                            cardView.addView(DateTV);

                            cardView.setOnClickListener(v -> {
                                Memory.EditChek = true;
                                String queryedit =
                                        "SELECT "
                                                + DBHelper.KEY_ID + ", "
                                                + DBHelper.KEY_TITLE + ", "
                                                + DBHelper.KEY_NOTE + ", "
                                                + DBHelper.KEY_CREATED_AT
                                                + " FROM "
                                                + DBHelper.TABLE_NOTES
                                                + " WHERE "
                                                + DBHelper.KEY_ID
                                                + " = "
                                                + (cardView.getId());
                                @SuppressLint("Recycle") Cursor cursor = database.rawQuery(queryedit, null);
                                if (cursor.moveToFirst()) {
                                    int editidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                                    int editcreated_atIndex = cursor.getColumnIndex(DBHelper.KEY_CREATED_AT);
                                    int editnoteIndex = cursor.getColumnIndex(DBHelper.KEY_NOTE);
                                    int edittitleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                                    Log.d("mLog",
                                            "Edit: ID = " + cursor.getInt(editidIndex)
                                                    + ", title = " + cursor.getString(edittitleIndex)
                                                    + ", note = " + cursor.getString(editnoteIndex)
                                                    + ", date = " + cursor.getString(editcreated_atIndex));
                                    Memory.EditID = cursor.getString(editidIndex);
                                    Memory.EditTitle = cursor.getString(edittitleIndex);
                                    Memory.EditNote = cursor.getString(editnoteIndex);

                                    Intent in = new Intent(MainActivity.this, NoteActivity.class);
                                    startActivity(in);
                                }
                            });
                            linearLayout.addView(cardView);

                        } while (cursor2.moveToNext());
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                linearLayout.removeAllViewsInLayout();

                String queryfilter = "SELECT " + DBHelper.KEY_ID + ", "
                        + DBHelper.KEY_TITLE + ", " + DBHelper.KEY_NOTE + ", " + DBHelper.KEY_CREATED_AT + " FROM " + DBHelper.TABLE_NOTES + " WHERE title='" + searchET.getText() + "'";
                @SuppressLint("Recycle")
                Cursor cursor2 = database.rawQuery(queryfilter, null);
                if (cursor2.moveToFirst()) {
                    int idIndex = cursor2.getColumnIndex(DBHelper.KEY_ID);
                    int noteIndex = cursor2.getColumnIndex(DBHelper.KEY_NOTE);
                    int titleIndex = cursor2.getColumnIndex(DBHelper.KEY_TITLE);
                    int dateIndex = cursor2.getColumnIndex(DBHelper.KEY_CREATED_AT);
                    do {
                        Log.d("mLog",
                                "ID = " + cursor2.getInt(idIndex)
                                        + ", title = " + cursor2.getString(titleIndex)
                                        + ", note = " + cursor2.getString(noteIndex)
                                        + ", date = " + cursor2.getString(dateIndex));
                        CardView cardView = new CardView(MainActivity.this);

                        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        layoutparams.setMargins(10, 10, 0, 0);

                        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        titleParams.setMargins(80, 10, 0, 0);

                        LinearLayout.LayoutParams noteparams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        noteparams.setMargins(45, 180, 0, 0);

                        LinearLayout.LayoutParams dateparams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        dateparams.setMargins(45, 100, 0, 10);

                        cardView.setId(cursor2.getInt(idIndex));
                        cardView.setLayoutParams(layoutparams);
                        cardView.setRadius(50);
                        cardView.setPadding(25, 25, 25, 25);
                        cardView.setMaxCardElevation(30);
                        cardView.setMaxCardElevation(6);


                        TextView TitleTV = new TextView(MainActivity.this);
                        TextView NoteTV = new TextView(MainActivity.this);
                        TextView DateTV = new TextView(MainActivity.this);

                        DateTV.setLayoutParams(dateparams);
                        TitleTV.setLayoutParams(titleParams);
                        NoteTV.setLayoutParams(noteparams);

                        if (cursor2.getString(noteIndex).length() > 30)
                            DateTV.setText(cursor2.getString(noteIndex).substring(0, 29) + "...");
                        else
                            DateTV.setText(cursor2.getString(noteIndex));
                        DateTV.setTextSize(16);

                        NoteTV.setText(cursor2.getString(dateIndex));
                        NoteTV.setTextSize(18);

                        TitleTV.setText(cursor2.getString(titleIndex));
                        TitleTV.setTextSize(20);
                        TitleTV.setTextColor(Color.rgb(245, 126, 104));
                        if (cursor2.getString(titleIndex).length() > 30)
                            TitleTV.setText(cursor2.getString(titleIndex).substring(0, 20) + "...");
                        else
                            TitleTV.setText(cursor2.getString(titleIndex));

                        cardView.addView(TitleTV);
                        cardView.addView(NoteTV);
                        cardView.addView(DateTV);

                        cardView.setOnClickListener(v -> {
                            Memory.EditChek = true;
                            String queryedit =
                                    "SELECT "
                                            + DBHelper.KEY_ID + ", "
                                            + DBHelper.KEY_TITLE + ", "
                                            + DBHelper.KEY_NOTE + ", "
                                            + DBHelper.KEY_CREATED_AT
                                            + " FROM "
                                            + DBHelper.TABLE_NOTES
                                            + " WHERE "
                                            + DBHelper.KEY_ID
                                            + " = "
                                            + (cardView.getId());
                            @SuppressLint("Recycle") Cursor cursor = database.rawQuery(queryedit, null);
                            if (cursor.moveToFirst()) {
                                int editidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                                int editcreated_atIndex = cursor.getColumnIndex(DBHelper.KEY_CREATED_AT);
                                int editnoteIndex = cursor.getColumnIndex(DBHelper.KEY_NOTE);
                                int edittitleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                                Log.d("mLog",
                                        "Edit: ID = " + cursor.getInt(editidIndex)
                                                + ", title = " + cursor.getString(edittitleIndex)
                                                + ", note = " + cursor.getString(editnoteIndex)
                                                + ", date = " + cursor.getString(editcreated_atIndex));
                                Memory.EditID = cursor.getString(editidIndex);
                                Memory.EditTitle = cursor.getString(edittitleIndex);
                                Memory.EditNote = cursor.getString(editnoteIndex);

                                Intent in = new Intent(MainActivity.this, NoteActivity.class);
                                startActivity(in);
                            }
                        });
                        linearLayout.addView(cardView);

                    } while (cursor2.moveToNext());
                }
            }
        });



        String queryload = "SELECT " + DBHelper.KEY_ID + ", "
                + DBHelper.KEY_TITLE + ", " + DBHelper.KEY_NOTE + ", " + DBHelper.KEY_CREATED_AT + " FROM " + DBHelper.TABLE_NOTES;

        @SuppressLint("Recycle")
        Cursor cursor2 = database.rawQuery(queryload, null);

        if (cursor2.moveToFirst()) {
            int idIndex = cursor2.getColumnIndex(DBHelper.KEY_ID);
            int noteIndex = cursor2.getColumnIndex(DBHelper.KEY_NOTE);
            int titleIndex = cursor2.getColumnIndex(DBHelper.KEY_TITLE);
            int dateIndex = cursor2.getColumnIndex(DBHelper.KEY_CREATED_AT);
            do {
                Log.d("mLog",
                        "ID = " + cursor2.getInt(idIndex)
                                + ", title = " + cursor2.getString(titleIndex)
                                + ", note = " + cursor2.getString(noteIndex)
                                + ", date = " + cursor2.getString(dateIndex));
                CardView cardView = new CardView(this);

                LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                layoutparams.setMargins(10, 10, 0, 0);

                LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                titleParams.setMargins(80, 10, 0, 0);

                LinearLayout.LayoutParams noteparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                noteparams.setMargins(45, 180, 0, 0);

                LinearLayout.LayoutParams dateparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                dateparams.setMargins(45, 100, 0, 10);

                cardView.setId(cursor2.getInt(idIndex));
                cardView.setLayoutParams(layoutparams);
                cardView.setRadius(50);
                cardView.setPadding(25, 25, 25, 25);
                cardView.setMaxCardElevation(30);
                cardView.setMaxCardElevation(6);


                TextView TitleTV = new TextView(this);
                TextView NoteTV = new TextView(this);
                TextView DateTV = new TextView(this);

                DateTV.setLayoutParams(dateparams);
                TitleTV.setLayoutParams(titleParams);
                NoteTV.setLayoutParams(noteparams);

                if (cursor2.getString(noteIndex).length() > 30)
                    DateTV.setText(cursor2.getString(noteIndex).substring(0, 29) + "...");
                else
                    DateTV.setText(cursor2.getString(noteIndex));
                DateTV.setTextSize(16);

                NoteTV.setText(cursor2.getString(dateIndex));
                NoteTV.setTextSize(18);

                TitleTV.setText(cursor2.getString(titleIndex));
                TitleTV.setTextSize(20);
                TitleTV.setTextColor(Color.rgb(245, 126, 104));
                if (cursor2.getString(titleIndex).length() > 30)
                    TitleTV.setText(cursor2.getString(titleIndex).substring(0, 20) + "...");
                else
                    TitleTV.setText(cursor2.getString(titleIndex));

                cardView.addView(TitleTV);
                cardView.addView(NoteTV);
                cardView.addView(DateTV);

                cardView.setOnClickListener(v -> {
                    Memory.EditChek = true;
                    String queryedit =
                            "SELECT "
                                    + DBHelper.KEY_ID + ", "
                                    + DBHelper.KEY_TITLE + ", "
                                    + DBHelper.KEY_NOTE + ", "
                                    + DBHelper.KEY_CREATED_AT
                                    + " FROM "
                                    + DBHelper.TABLE_NOTES
                                    + " WHERE "
                                    + DBHelper.KEY_ID
                                    + " = "
                                    + (cardView.getId());
                    @SuppressLint("Recycle") Cursor cursor = database.rawQuery(queryedit, null);
                    if (cursor.moveToFirst()) {
                        int editidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                        int editcreated_atIndex = cursor.getColumnIndex(DBHelper.KEY_CREATED_AT);
                        int editnoteIndex = cursor.getColumnIndex(DBHelper.KEY_NOTE);
                        int edittitleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
                        Log.d("mLog",
                                "Edit: ID = " + cursor.getInt(editidIndex)
                                        + ", title = " + cursor.getString(edittitleIndex)
                                        + ", note = " + cursor.getString(editnoteIndex)
                                        + ", date = " + cursor.getString(editcreated_atIndex));
                        Memory.EditID = cursor.getString(editidIndex);
                        Memory.EditTitle = cursor.getString(edittitleIndex);
                        Memory.EditNote = cursor.getString(editnoteIndex);

                        Intent in = new Intent(MainActivity.this, NoteActivity.class);
                        startActivity(in);
                    }
                });
                linearLayout.addView(cardView);

            } while (cursor2.moveToNext());


        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addNoteButton) {
            Memory.EditChek = false;
            Memory.EditTitle = "";
            Memory.EditNote = "";
            Intent intent = new Intent(this, NoteActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
