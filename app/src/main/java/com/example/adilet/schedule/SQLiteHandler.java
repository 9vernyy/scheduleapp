package com.example.adilet.schedule;

/**
 * Created by Adilet on 12.04.2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "schedule";

    // Login table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_SCHEDULE = "schedule";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASSWORD = "password";

    private static final String KEY_SCHEDULE_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_DAY_WEEK_ID = "day_week_id";
    private static final String KEY_NAME_SUBJECT = "name_subject";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_CABINET = "cabinet";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableUser(db);
        createTableSchedule(db);

    }

    private void createTableSchedule(SQLiteDatabase db) {
        String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + TABLE_SCHEDULE + "("
                + KEY_SCHEDULE_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " INTEGER,"
                + KEY_DAY_WEEK_ID + " INTEGER,"
                + KEY_NAME_SUBJECT + " TEXT," + KEY_START_TIME + " TEXT,"
                + KEY_END_TIME + " TEXT," + KEY_CABINET + " INTEGER" + ")";
        db.execSQL(CREATE_SCHEDULE_TABLE);
        Log.d(TAG, "База данных расписания успешно создана");
    }

    private void createTableUser(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SURNAME + " TEXT," + KEY_EMAIL + " TEXT UNIQUE,"
                + KEY_LOGIN + " TEXT UNIQUE," + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "База данных пользователей успешно создана");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String name, String surname, String email, String login, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name); // Name
        values.put(KEY_SURNAME, surname); // Surname
        values.put(KEY_EMAIL, email); // Email
        values.put(KEY_LOGIN, login); // Login
        values.put(KEY_PASSWORD, password); // Password

        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "Новый пользователь добавлен в Базу данных Sqlite: " + id);
    }

    public void addSchedule(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, subject.getUserId()); // userId
        values.put(KEY_DAY_WEEK_ID, subject.getDayWeekId()); // dayWeekId
        values.put(KEY_NAME_SUBJECT, subject.getNameSubject()); // subjectName
        values.put(KEY_START_TIME, subject.getStartTime()); // startTime
        values.put(KEY_END_TIME, subject.getEndTime()); // endTime
        values.put(KEY_CABINET, subject.getCabinet()); // cabinet

        // Inserting Row
        long id = db.insert(TABLE_SCHEDULE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "Новое расписание добавлено в Базу данных Sqlite: " + id);
    }
    /**
     * Getting user data from database
     * */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("id", cursor.getString(0));
            user.put("name", cursor.getString(1));
            user.put("surname", cursor.getString(2));
            user.put("email", cursor.getString(3));
            user.put("login", cursor.getString(4));
            user.put("password", cursor.getString(5));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Выбранный пользователь с Базы данных Sqlite: " + user.toString());

        return user;
    }

    public List<Subject> getSubjectsByDay(int userId, int dayWeekId) {
        List<Subject> subjects = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ TABLE_SCHEDULE + " WHERE " + KEY_USER_ID + "=" + userId
                + " AND " + KEY_DAY_WEEK_ID + "=" + dayWeekId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Subject subject = new Subject();
                subject.setId(Integer.parseInt(cursor.getString(0)));
                subject.setUserId(Integer.parseInt(cursor.getString(1)));
                subject.setDayWeekId(Integer.parseInt(cursor.getString(2)));
                subject.setNameSubject(cursor.getString(3));
                subject.setStartTime(cursor.getString(4));
                subject.setEndTime(cursor.getString(5));
                subject.setCabinet(Integer.parseInt(cursor.getString(6)));
                subjects.add(subject);
            }while (cursor.moveToNext());
        }
        return subjects;
    }

    public int updateSubject(Subject subject) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
//        values.put(KEY_USER_ID, subject.getUserId());
//        values.put(KEY_DAY_WEEK_ID, subject.getDayWeekId());
        values.put(KEY_NAME_SUBJECT, subject.getNameSubject());
        values.put(KEY_START_TIME, subject.getStartTime());
        values.put(KEY_END_TIME, subject.getEndTime());
        values.put(KEY_CABINET, subject.getCabinet());

        return  sqLiteDatabase.update(TABLE_SCHEDULE, values, KEY_SCHEDULE_ID + "=?", new String[]{String.valueOf(subject.getId())});
    }

    public void deleteSubject(Subject subject) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.delete(TABLE_SCHEDULE, KEY_SCHEDULE_ID + "=?", new String[]{String.valueOf(subject.getId())});
        sqLiteDatabase.close();
    }

    public Subject getSubjectById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SCHEDULE, new String[]
                        {
                                KEY_SCHEDULE_ID,
                                KEY_USER_ID,
                                KEY_DAY_WEEK_ID,
                                KEY_NAME_SUBJECT,
                                KEY_START_TIME,
                                KEY_END_TIME,
                                KEY_CABINET,
                        }, KEY_SCHEDULE_ID + "=?",

                new String[] {String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Subject reminder = new Subject(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                Integer.parseInt(cursor.getString(2)), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), Integer.parseInt(cursor.getString(6)));

        return reminder;
    }
}
