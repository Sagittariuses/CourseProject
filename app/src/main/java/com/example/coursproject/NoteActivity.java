package com.example.coursproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Scroller;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        NoteEt = findViewById(R.id.ContentInput);
        NoteEt.setText(Memory.EditNote);
        NoteEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(NoteEt.getText().toString().length() == 0){
                    chek = false;
                    redoMI.setVisible(false);
                    undoMI.setVisible(false);
                    doneMI.setVisible(false);
                    remindMI.setVisible(true);
                    deleteMI.setVisible(true);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                chek=true;
                redoMI.setVisible(true);
                undoMI.setVisible(true);
                doneMI.setVisible(true);
                remindMI.setVisible(false);
                deleteMI.setVisible(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(NoteEt.getText().toString().length() == 0){
                    chek = false;
                    redoMI.setVisible(false);
                    undoMI.setVisible(false);
                    doneMI.setVisible(false);
                    remindMI.setVisible(true);
                    deleteMI.setVisible(true);
                }
            }
        });
        NoteEt.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                redoMI.setVisible(true);
                undoMI.setVisible(true);
                doneMI.setVisible(true);
                remindMI.setVisible(false);
                deleteMI.setVisible(false);
            }
        });

        TitleEt = findViewById(R.id.TitleInput);
        TitleEt.setText(Memory.EditTitle);
        TitleEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(TitleEt.getText().toString().length() == 0){
                    chek = false;
                    redoMI.setVisible(false);
                    undoMI.setVisible(false);
                    doneMI.setVisible(false);
                    remindMI.setVisible(true);
                    deleteMI.setVisible(true);
                }

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                chek = false;
                redoMI.setVisible(true);
                undoMI.setVisible(true);
                doneMI.setVisible(true);
                remindMI.setVisible(false);
                deleteMI.setVisible(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TitleEt.getText().toString().length() == 0){
                    chek = false;
                    redoMI.setVisible(false);
                    undoMI.setVisible(false);
                    doneMI.setVisible(false);
                    remindMI.setVisible(true);
                    deleteMI.setVisible(true);
                }
            }
        });
        TitleEt.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                redoMI.setVisible(true);
                undoMI.setVisible(true);
                doneMI.setVisible(true);
                remindMI.setVisible(false);
                deleteMI.setVisible(false);
            }
        });

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
    @SuppressLint({"NonConstantResourceId", "WrongConstant"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Date today = new Date();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatForDateNow = new SimpleDateFormat( "yyyy.MM.dd");
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
                    Memory.EditID = Memory.EditID + 1;
                    Memory.EditChek = true;
                    Log.d("mLog",  "Added: title = " + title + ", note = " + note + ", date  = " + date);
                } else
                {
                    String queryEditTitle = "UPDATE notes SET title='" + TitleEt.getText()
                            + "', note='" + NoteEt.getText()
                            + "', created_at='" + formatForDateNow.format(today)
                            +"' WHERE _id="+ Memory.EditID;
                    database.execSQL(queryEditTitle);

                }
                redoMI.setVisible(false);
                undoMI.setVisible(false);
                doneMI.setVisible(false);
                remindMI.setVisible(true);
                deleteMI.setVisible(true);
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
                NoteEt.setFocusable(false);
                TitleEt.setFocusable(false);
                return true;

            case R.id.delete:
                String querydelete = "DELETE " + " FROM " + DBHelper.TABLE_NOTES + " WHERE " + DBHelper.KEY_ID + " = " + (Memory.EditID);
                database.execSQL(querydelete);
                String idGenerate = "UPDATE" + DBHelper.TABLE_NOTES + " SET  _id =  id - " + 1 + " WHERE _id " + " > " + Memory.EditID;
                database.execSQL(idGenerate);
                return true;

            default:
                dbHelper.close();
                return super.onOptionsItemSelected(item);

        }

    }



}
