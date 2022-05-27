package com.example.coursproject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class NoteActivity extends AppCompatActivity {

    public DBHelper dbHelper;
    EditText NoteEt;
    EditText TitleEt;
    TextViewUndoRedo helperNote;
    TextViewUndoRedo helperTitle;
    MenuItem doneMI, undoMI, redoMI, remindMI, deleteMI;
    TimePicker timePicker;


    Dialog dlgDelete, dlgRemind, datepicker;

    Calendar myCalendar;


    @SuppressLint("WrongConstant")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        myCalendar = new GregorianCalendar();


        dlgDelete = new Dialog(this);
        dlgRemind = new Dialog(this);
        datepicker = new Dialog(this);
        dbHelper = new DBHelper(this);

        NoteEt = findViewById(R.id.ContentInput);
        NoteEt.setText(Memory.EditNote);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        NoteEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(NoteEt.getText().toString().length() == 0){
                    HideMenuItems();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ShowMenuItems();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(NoteEt.getText().toString().length() == 0){
                    HideMenuItems();
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
                    HideMenuItems();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ShowMenuItems();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TitleEt.getText().toString().length() == 0){
                    HideMenuItems();
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
        undoMI = menu.findItem(R.id.undo);
        redoMI= menu.findItem(R.id.redo);
        remindMI = menu.findItem(R.id.reminde);
        deleteMI = menu.findItem(R.id.delete);

        doneMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        remindMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        deleteMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        undoMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        redoMI.setOnMenuItemClickListener(this::onOptionsItemSelected);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        setResult(200);
        finish();
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
                    Memory.EditChek = false;

                }
                HideMenuItems();
                return true;

            case R.id.redo:
                if(!TitleEt.hasFocus())
                    helperNote.redo();
                else
                    helperTitle.redo();
                return true;

            case R.id.undo:
                if(!TitleEt.hasFocus())
                    helperNote.undo();
                else
                    helperTitle.undo();
                return true;

            case R.id.reminde:
                Button btnCancelRemind, btnRemind, btnSelectDate;


                dlgRemind.setContentView(R.layout.remind_popup);

                btnCancelRemind = (Button) dlgRemind.findViewById(R.id.cancel_remind);
                btnCancelRemind.setOnClickListener(this::onClickRemind);

                btnRemind = (Button) dlgRemind.findViewById(R.id.done_remind);
                btnRemind.setOnClickListener(this::onClickRemind);

                btnSelectDate = (Button) dlgRemind.findViewById(R.id.select_date_remind);
                btnSelectDate.setOnClickListener(this::onClickRemind);


                WindowManager.LayoutParams params = dlgRemind.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                dlgRemind.getWindow().setAttributes(params);
                dlgRemind.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePicker = dlgRemind.findViewById(R.id.time_remind);
                timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                timePicker.setIs24HourView(true);
                dlgRemind.show();
                return true;

            case R.id.delete:
                Button btnCancel, btnDelete;

                dlgDelete.setContentView(R.layout.delete_popup);

                btnCancel = (Button) dlgDelete.findViewById(R.id.cancel_delete);
                btnCancel.setOnClickListener(this::onClickDelete);

                btnDelete = (Button) dlgDelete.findViewById(R.id.delete_delete);
                btnDelete.setOnClickListener(this::onClickDelete);

                WindowManager.LayoutParams paramsDelete = dlgDelete.getWindow().getAttributes();
                paramsDelete.width = WindowManager.LayoutParams.MATCH_PARENT;
                paramsDelete.height = WindowManager.LayoutParams.MATCH_PARENT;
                dlgDelete.getWindow().setAttributes(paramsDelete);
                dlgDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dlgDelete.show();
                return true;

            default:
                dbHelper.close();
                return super.onOptionsItemSelected(item);
        }
    }

    public  void HideMenuItems(){
        redoMI.setVisible(false);
        undoMI.setVisible(false);
        doneMI.setVisible(false);
        remindMI.setVisible(true);
        deleteMI.setVisible(true);
    }

    public  void ShowMenuItems(){
        redoMI.setVisible(true);
        undoMI.setVisible(true);
        doneMI.setVisible(true);
        remindMI.setVisible(false);
        deleteMI.setVisible(false);
    }


    @SuppressLint("NonConstantResourceId")
    public void onClickDelete (View v){
        switch (v.getId()){
            case R.id.cancel_delete:
                dlgDelete.dismiss();
                break;
            case R.id.delete_delete:
                TitleEt.setText("");
                NoteEt.setText("");
                Memory.DeleteChek = true;
                onSupportNavigateUp();
                dlgDelete.dismiss();
                break;

        }

    }

    @SuppressLint("NonConstantResourceId")
    public  void onClickCalendar (View v){
        switch (v.getId()){
            case R.id.cancel_calendar:
                datepicker.dismiss();
                break;
            case R.id.ok_calendar:
                DatePicker datePickerCalendar = datepicker.findViewById(R.id.datePickerRemind);
                Memory.Year = datePickerCalendar.getYear();
                Memory.Month = datePickerCalendar.getMonth();
                Memory.Day = datePickerCalendar.getDayOfMonth();
                datepicker.dismiss();
                break;

        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onClickRemind(View v){
        switch (v.getId()){
            case R.id.cancel_remind:
                dlgRemind.dismiss();
                break;
            case R.id.done_remind:
                NoteEt.setFocusable(false);
                TitleEt.setFocusable(false);

                Memory.Hour = timePicker.getHour();
                Memory.Minute = timePicker.getMinute();


                if (Memory.Year == 0){
                    Memory.calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Memory.Hour, Memory.Minute-2, 0);
                } else {
                    Memory.calendar.set(Memory.Year,Memory.Month,Memory.Day, Memory.Hour, Memory.Minute-2, 0);
                }
                long milisek = Memory.calendar.getTimeInMillis();
                Log.d("mLog", "Miliseconds: " + milisek );
                reminderNotification();
                dlgRemind.dismiss();
                break;
            case R.id.select_date_remind:
                Locale locale = getResources().getConfiguration().locale;
                Locale.setDefault(locale);
                datepicker.setContentView(R.layout.datepicker_popup);
                ViewGroup.LayoutParams params = datepicker.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                datepicker.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
                datepicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button okdatepicker = datepicker.findViewById(R.id.ok_calendar);
                okdatepicker.setOnClickListener(this::onClickCalendar);
                Button canceldatepicker = datepicker.findViewById(R.id.cancel_calendar);
                canceldatepicker.setOnClickListener(this::onClickCalendar);

                datepicker.show();
                break;


        }

    }
    public void reminderNotification()
    {
        Memory.NoteNotify = Memory.EditNote;
        Memory.TitleNotify = Memory.EditTitle;
        NotificationUtils _notificationUtils = new NotificationUtils(this);
        _notificationUtils.setReminder(Memory.calendar);
    }

}
