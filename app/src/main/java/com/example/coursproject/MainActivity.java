package com.example.coursproject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


// multi-choise
// https://startandroid.ru/ru/uroki/vse-uroki-spiskom/83-urok-43-odinochnyj-i-mnozhestvennyj-vybor-v-list.html
// notify
// https://www.youtube.com/watch?v=tyVaPHv-RGo

public class MainActivity extends AppCompatActivity implements OnClickListener {
    FloatingActionButton btnAdd;
    SearchView searchView;
    DBHelper dbHelper;
    SQLiteDatabase database;
    NoteAdapter adapter;
    private List<Model> mModelList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    View item;

    @Override
    @SuppressLint({"NewApi", "SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        String queryload = "SELECT " + DBHelper.KEY_ID + ", "
                + DBHelper.KEY_TITLE + ", " + DBHelper.KEY_NOTE + ", " + DBHelper.KEY_CREATED_AT + " FROM " + DBHelper.TABLE_NOTES;
        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(queryload, null);
        CreateRecycleView(cursor);




        item = findViewById(R.id.linearlayout);

        btnAdd = findViewById(R.id.addNoteButton);
        btnAdd.setOnClickListener(this);

        searchView = findViewById(R.id.searchSV);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String queryfilter =
                        "SELECT "
                                + DBHelper.KEY_ID + ", "
                                + DBHelper.KEY_TITLE + ", "
                                + DBHelper.KEY_NOTE + ", "
                                + DBHelper.KEY_CREATED_AT
                                + " FROM "
                                + DBHelper.TABLE_NOTES
                                + " WHERE title like'"
                                + newText + "%'";

                @SuppressLint("Recycle")
                Cursor cursor = database.rawQuery(queryfilter, null);
                CreateRecycleView(cursor);
                return true;
            }

            public void callSearch(String query) {

            }

        });
    }



    public void onItemClick(View view, int position) {
        String queryedit =
                "SELECT "
                        + DBHelper.KEY_ID + ", "
                + DBHelper.KEY_TITLE
                        + ", " + DBHelper.KEY_NOTE
                        + ", " + DBHelper.KEY_CREATED_AT
                        + " FROM "
                        + DBHelper.TABLE_NOTES
                + " WHERE _id = "
                + adapter.getItem(position);
        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(queryedit, null);
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
            Memory.EditID = cursor.getInt(editidIndex);
            Memory.EditTitle = cursor.getString(edittitleIndex);
            Memory.EditNote = cursor.getString(editnoteIndex);
            Memory.EditChek = true;
            Intent in = new Intent(MainActivity.this, NoteActivity.class);
            startActivityForResult(in, 200);
        }
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + (position+1), Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Memory.DeleteChek){
            String querydelete = "DELETE " + " FROM " + DBHelper.TABLE_NOTES + " WHERE " + DBHelper.KEY_ID + " = " + (Memory.EditID);
            database.execSQL(querydelete);
            try {
                String idGenerate = "UPDATE " + DBHelper.TABLE_NOTES + " SET  _id =  id - " + 1 + " WHERE _id " + " > " + Memory.EditID;
                Cursor cursor = database.rawQuery(idGenerate, null);
                if (cursor.moveToFirst()) {
                    int editidIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    if (editidIndex != 0){
                        database.execSQL(idGenerate);
                    }
                }
            } catch (Exception exception){
                Log.d("mLog",exception.toString());
            }
            Memory.DeleteChek = false;
        }
        searchView.setQuery("", false);
        String queryload = "SELECT " + DBHelper.KEY_ID + ", "
                + DBHelper.KEY_TITLE + ", " + DBHelper.KEY_NOTE + ", " + DBHelper.KEY_CREATED_AT + " FROM " + DBHelper.TABLE_NOTES;
        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery(queryload, null);
        CreateRecycleView(cursor);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addNoteButton) {


            Memory.EditChek = false;
            Memory.EditTitle = "";
            Memory.EditNote = "";
            Intent intent = new Intent(this, NoteActivity.class);
            startActivityForResult(intent, 200);
        }
    }

    public  void CreateRecycleView(Cursor cursor){

        mModelList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int noteIndex = cursor.getColumnIndex(DBHelper.KEY_NOTE);
            int titleIndex = cursor.getColumnIndex(DBHelper.KEY_TITLE);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_CREATED_AT);
            do {
                Log.d("mLog",
                        "ID = " + cursor.getInt(idIndex)
                                + ", title = " + cursor.getString(titleIndex)
                                + ", note = " + cursor.getString(noteIndex)
                                + ", date = " + cursor.getString(dateIndex));

                int IdI = cursor.getInt(idIndex);
                String TitleS;
                String NoteS;
                String DateS;

                if (cursor.getString(noteIndex).length() > 30)
                    NoteS = cursor.getString(noteIndex).substring(0, 29) + "...";
                else
                    NoteS = cursor.getString(noteIndex);

                DateS = cursor.getString(dateIndex);

                if (cursor.getString(titleIndex).length() > 30)
                    TitleS = cursor.getString(titleIndex).substring(0, 20) + "...";
                else
                    TitleS = cursor.getString(titleIndex);
                mModelList.add(new Model(TitleS,NoteS, DateS, IdI));
                Memory.EditID = cursor.getInt(idIndex);

            } while (cursor.moveToNext());
        }

        RecyclerView recyclerView = findViewById(R.id.LayoutForCard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NoteAdapter(mModelList, item );
        adapter.setClickListener(this::onItemClick);
        recyclerView.setAdapter(adapter);
    }


}

