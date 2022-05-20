package com.example.coursproject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import java.util.Calendar;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.security.cert.CertPathBuilder;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class NoteActivity extends AppCompatActivity {

    public DBHelper dbHelper;
    EditText NoteEt;
    EditText TitleEt;
    TextViewUndoRedo helperNote;
    TextViewUndoRedo helperTitle;
    MenuItem doneMI, undoMI, redoMI, remindMI, deleteMI;



    Dialog dlgDelete, dlgRemind, datepicker;

    Calendar myCalendar;
    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 101;
    private static final String CHANNEL_ID = "CHANNEL_ID";


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
                if(TitleEt.hasFocus() == false)
                    helperNote.redo();
                else
                    helperTitle.redo();
                return true;

            case R.id.undo:
                if(TitleEt.hasFocus() == false)
                    helperNote.undo();
                else
                    helperTitle.undo();
                return true;

            case R.id.reminde:
                Button btnCancelRemind, btnRemind, btnSelectDate;
                TimePicker timePicker;

                dlgRemind.setContentView(R.layout.remind_popup);

                btnCancelRemind = (Button) dlgRemind.findViewById(R.id.cancel_remind);
                btnCancelRemind.setOnClickListener(this::onClickRemind);

                btnRemind = (Button) dlgRemind.findViewById(R.id.done_remind);
                btnRemind.setOnClickListener(this::onClickRemind);

                btnSelectDate = (Button) dlgRemind.findViewById(R.id.select_date_remind);
                btnSelectDate.setOnClickListener(this::onClickRemind);
                try{
                    timePicker = (TimePicker) dlgRemind.findViewById(R.id.time_remind);
                    timePicker.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                    timePicker.setIs24HourView(true);
                } catch (Exception exception){
                    Log.d("mLog", exception.toString());
                }

                ViewGroup.LayoutParams params = dlgRemind.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                params.height = WindowManager.LayoutParams.MATCH_PARENT;
                dlgRemind.getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
                dlgRemind.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dlgRemind.show();
                return true;

            case R.id.delete:
                Button btnCancel, btnDelete;

                dlgDelete.setContentView(R.layout.delete_popup);

                btnCancel = (Button) dlgDelete.findViewById(R.id.cancel_delete);
                btnCancel.setOnClickListener(this::onClickDelete);

                btnDelete = (Button) dlgDelete.findViewById(R.id.delete_delete);
                btnDelete.setOnClickListener(this::onClickDelete);

                ViewGroup.LayoutParams paramsDelete = dlgDelete.getWindow().getAttributes();
                paramsDelete.width = WindowManager.LayoutParams.MATCH_PARENT;
                paramsDelete.height = WindowManager.LayoutParams.MATCH_PARENT;
                dlgDelete.getWindow().setAttributes((android.view.WindowManager.LayoutParams) paramsDelete);
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

    public static void createChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }

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

    public void onClickRemind(View v){
        /*SQLiteDatabase database = dbHelper.getWritableDatabase();*/
        switch (v.getId()){
            case R.id.cancel_remind:
                dlgRemind.dismiss();
                break;
            case R.id.done_remind:
                NoteEt.setFocusable(false);
                TitleEt.setFocusable(false);
                notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                .setAutoCancel(false)
                                .setSmallIcon(R.drawable.ic_splash_screen)
                                .setWhen(System.currentTimeMillis())
                                .setContentIntent(pendingIntent)
                                .setContentTitle("Напоминание: " + Memory.EditTitle)
                                .setContentText(Memory.EditNote)
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true);
                createChannelIfNeeded(notificationManager);
                notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
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
                datepicker.show();
                DatePicker datePicker = datepicker.findViewById(R.id.datePickerRemind);
                break;


        }

    }

}
