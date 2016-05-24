package kz.alisher.scheduleapp;

/**
 * Created by Adilet on 12.04.2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    // Table name
    private static final String TABLE_USER = "user";
    private static final String TABLE_SCHEDULE = "schedule";
    private static final String TABLE_GROUP = "groups";
    private static final String TABLE_STUDENT = "student";

    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IMAGE = "image";

    private static final String KEY_SCHEDULE_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_DATE = "date_s";
    private static final String KEY_DAY_WEEK_ID = "day_week_id";
    private static final String KEY_NAME_SUBJECT = "name_subject";
    private static final String KEY_START_TIME = "start_time";
    private static final String KEY_END_TIME = "end_time";
    private static final String KEY_CABINET = "cabinet";
    private static final String KEY_GROUP_ID = "group_id";

    private static final String GROUP_ID = "id";
    private static final String GROUP_NAME = "name";

    private static final String STUDENT_ID = "id";
    private static final String STUDENT_FIRST_NAME = "firstName";
    private static final String STUDENT_LAST_NAME = "lastName";
    private static final String STUDENT_GROUP_ID = "group_id";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableUser(db);
        createTableSchedule(db);
        createTableGroup(db);
        createTableStudent(db);
    }

    private void createTableSchedule(SQLiteDatabase db) {
        String CREATE_SCHEDULE_TABLE = "CREATE TABLE " + TABLE_SCHEDULE + "("
                + KEY_SCHEDULE_ID + " INTEGER PRIMARY KEY," + KEY_USER_ID + " INTEGER,"
                + KEY_DAY_WEEK_ID + " INTEGER,"
                + KEY_NAME_SUBJECT + " TEXT," + KEY_START_TIME + " TEXT,"
                + KEY_END_TIME + " TEXT," + KEY_CABINET + " INTEGER,"
                + KEY_GROUP_ID + " TEXT,"
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_SCHEDULE_TABLE);
        Log.d(TAG, "База данных расписания успешно создана");
    }

    private void createTableUser(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SURNAME + " TEXT," + KEY_EMAIL + " TEXT UNIQUE," + KEY_IMAGE + " BLOB,"
                + KEY_LOGIN + " TEXT UNIQUE," + KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);


        Log.d("LOGIN_TABLE", CREATE_LOGIN_TABLE);
    }

    private void createTableStudent(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_STUDENT + "("
                + STUDENT_ID + " INTEGER PRIMARY KEY,"
                + STUDENT_FIRST_NAME + " TEXT,"
                + STUDENT_LAST_NAME + " TEXT,"
                + STUDENT_GROUP_ID + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "База данных пользователей успешно создана" + CREATE_LOGIN_TABLE);
    }

    private void createTableGroup(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_GROUP + "("
                + GROUP_ID + " INTEGER PRIMARY KEY," + GROUP_NAME + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "База данных пользователей успешно создана");
    }

    public int setImage(int userId, byte[] image) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(KEY_IMAGE, image);
        return sqLiteDatabase.update(TABLE_USER, cv, KEY_ID + "=?", new String[]{String.valueOf(userId)});
    }

    public Bitmap getImage(int userId) {
        String selectQuery = "SELECT " + KEY_IMAGE + " FROM " + TABLE_USER + " WHERE id= " + userId;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            byte[] byteImage = cursor.getBlob(0);
            if (byteImage == null) {
                cursor.close();
            } else {
                cursor.close();
                return BitmapFactory.decodeByteArray(byteImage, 0, byteImage.length);
            }
        }

        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return null;
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
     */
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

    public void addGroup(String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GROUP_NAME, name);

        long id = db.insert(TABLE_GROUP, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "Новая группа добавлен в Базу данных Sqlite: " + id);
    }

    public void addStudent(String fname, String lname, String group_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STUDENT_FIRST_NAME, fname);
        values.put(STUDENT_LAST_NAME, lname);
        values.put(STUDENT_GROUP_ID, group_id);

        long id = db.insert(TABLE_STUDENT, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "Новая группа добавлен в Базу данных Sqlite: " + id);
    }

    public List<String> getGroups() {
        List<String> groups = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_GROUP;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                groups.add(name);
            } while (cursor.moveToNext());
        }
        return groups;
    }

    public List<Student> getStudentsByGroupId(String group_id) {
        List<Student> students = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENT + " where " + STUDENT_GROUP_ID + "='"+group_id+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student();
                student.setfName(cursor.getString(1));
                student.setlName(cursor.getString(2));
                student.setGroupId(cursor.getString(3));
                students.add(student);
            } while (cursor.moveToNext());
        }
        return students;
    }


    public int addSchedule(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, subject.getUserId()); // userId
        values.put(KEY_DAY_WEEK_ID, subject.getDayWeekId()); // dayWeekId
        values.put(KEY_NAME_SUBJECT, subject.getNameSubject()); // subjectName
        values.put(KEY_START_TIME, subject.getStartTime()); // startTime
        values.put(KEY_END_TIME, subject.getEndTime()); // endTime
        values.put(KEY_CABINET, subject.getCabinet()); // cabinet
        values.put(KEY_GROUP_ID, subject.getGroup_id()); // group_id
        values.put(KEY_DATE, subject.getDateS()); // date

        // Inserting Row
        long id = db.insert(TABLE_SCHEDULE, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "Новое расписание добавлено в Базу данных Sqlite: " + id);
        return (int) id;
    }

    /**
     * Getting user data from database
     */
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
            user.put("login", cursor.getString(5));
            user.put("password", cursor.getString(6));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Выбранный пользователь с Базы данных Sqlite: " + user.toString());

        return user;
    }

    public List<Subject> getSubjectsByDay(int userId, int dayWeekId) {
        List<Subject> subjects = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SCHEDULE + " WHERE " + KEY_USER_ID + "=" + userId
                + " AND " + KEY_DAY_WEEK_ID + "=" + dayWeekId;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Subject subject = new Subject();
                subject.setId(Integer.parseInt(cursor.getString(0)));
                subject.setUserId(Integer.parseInt(cursor.getString(1)));
                subject.setDayWeekId(Integer.parseInt(cursor.getString(2)));
                subject.setNameSubject(cursor.getString(3));
                subject.setStartTime(cursor.getString(4));
                subject.setEndTime(cursor.getString(5));
                subject.setCabinet(Integer.parseInt(cursor.getString(6)));
                subject.setGroup_id(cursor.getString(7));
                subject.setDateS(cursor.getString(8));
                subjects.add(subject);
            } while (cursor.moveToNext());
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

        return sqLiteDatabase.update(TABLE_SCHEDULE, values, KEY_SCHEDULE_ID + "=?", new String[]{String.valueOf(subject.getId())});
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
                                KEY_GROUP_ID,
                                KEY_DATE
                        }, KEY_SCHEDULE_ID + "=?",

                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Subject reminder = new Subject(Integer.parseInt(cursor.getString(0)), Integer.parseInt(cursor.getString(1)),
                Integer.parseInt(cursor.getString(2)), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), Integer.parseInt(cursor.getString(6)), cursor.getString(7), cursor.getString(8));

        return reminder;
    }

    public List<Subject> getAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SCHEDULE;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Subject subject = new Subject();
                subject.setId(Integer.parseInt(cursor.getString(0)));
                subject.setUserId(Integer.parseInt(cursor.getString(1)));
                subject.setDayWeekId(Integer.parseInt(cursor.getString(2)));
                subject.setNameSubject(cursor.getString(3));
                subject.setStartTime(cursor.getString(4));
                subject.setEndTime(cursor.getString(5));
                subject.setCabinet(Integer.parseInt(cursor.getString(6)));
                subject.setGroup_id(cursor.getString(7));
                subject.setDateS(cursor.getString(8));
                subjects.add(subject);
            } while (cursor.moveToNext());
        }
        return subjects;
    }
}
