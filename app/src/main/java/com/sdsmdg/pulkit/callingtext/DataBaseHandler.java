package com.sdsmdg.pulkit.callingtext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pulkit on 4/2/17.
 */

public class DataBaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Caller_Details";
    private static final String TABLE_CALLERS= "callers";
    private static final String CALLER_NAME = "name";
    private static final String CALLER_NUMBER="number";
    private static final String CALL_TIME = "time";
    private static final String CALL_TYPE= "type";
    private static final String MESSAGE="message";
    private static final String CALL_DURATION="duration";
    private static final String TABLE_LOGIN= "login";
    private static final String NAME= "name";
    private static final String PASSWORD = "password";
    private static DataBaseHandler instance = null;
    private Context context;

     DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    /**
     * to get the instance of the database handler class
     * @param context to be passed in from the activity
     * @return a databasehandler object
     */
    public static DataBaseHandler getInstance(Context context) {
        if(instance == null) {
            instance = new DataBaseHandler(context);
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table_users = "CREATE TABLE " + TABLE_CALLERS + "(" + CALLER_NAME + " TEXT,"+CALLER_NUMBER +" TEXT," + CALL_TIME + " TEXT," + MESSAGE+" TEXT,"+ CALL_TYPE + " TEXT,"+ CALL_DURATION + " TEXT)";
        db.execSQL(create_table_users);
        String create_table_login = "CREATE TABLE " + TABLE_LOGIN + "(" + NAME + " TEXT,"+ PASSWORD + " TEXT)";
        db.execSQL(create_table_login);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CALLERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        onCreate(db);
    }

    /**
     * Clears the entire sqlite database
     */
    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CALLERS + ";");
        db.execSQL("DELETE FROM " + TABLE_LOGIN + ";");

    }

    /**
     * Adds a caller to the list.
     * @param cd
     */
    void addCaller(CallerDetails cd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CALLER_NAME,cd.caller_name);
        values.put(CALLER_NUMBER,cd.caller_number);
        values.put(CALL_TIME,cd.call_time);
        values.put(MESSAGE,cd.caller_msg);
        values.put(CALL_TYPE,cd.call_type);
        values.put(CALL_DURATION,cd.call_duration);
        db.insert(TABLE_CALLERS, null, values);
        db.close();
    }

    /**
     * Adds a new user to the sqLite database
     * @param name Name of the user
     * @param password of the user
     *                 to be entered in Login Activity
     */
    void addUser(String name, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CALLER_NAME,name);
        values.put(CALLER_NUMBER,password);
        db.insert(TABLE_LOGIN, null, values);
        db.close();
    }

    /**
     * Gets the list of all the callers
     * @return the caller list
     */
    List<CallerDetails> getAllCallers() {
        List<CallerDetails> userList = new ArrayList<>();
       // String query = "SELECT * FROM " + TABLE_CALLERS;
        SQLiteDatabase db = this.getReadableDatabase();
       // Cursor cursor = db.rawQuery(query, null);
        Cursor cursor = db.query(TABLE_CALLERS, null, null, null, null, null, null );
        if (cursor.moveToFirst()) {
            do {
                CallerDetails cd = new CallerDetails(cursor.getString(0), cursor.getString(1), cursor.getString(3), cursor.getString(4),cursor.getString(2),cursor.getString(5));
                userList.add(cd);
            } while (cursor.moveToNext());
        }
        cursor.close();
        Collections.reverse(userList);
        return userList;
    }

    /**
     * Gets the details of the user from the sqLite database
     * @param name of the user
     * @return
     */
    public String getUser(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOGIN, new String[]{NAME,PASSWORD}, NAME + "=?", new String[]{name}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getString(1);
    }

    public List<CallerDetails> getAllCallsByNumber(String number){
        List<CallerDetails> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_CALLERS, new String[]{CALLER_NAME, CALLER_NUMBER, CALL_TIME, MESSAGE, CALL_TYPE, CALL_DURATION}, CALLER_NUMBER + "=?", new String[]{number}, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                CallerDetails cd = new CallerDetails(cursor.getString(0), cursor.getString(1), cursor.getString(3), cursor.getString(4),cursor.getString(2),cursor.getString(5));
                userList.add(cd);
            } while (cursor.moveToNext());
        }
        Collections.reverse(userList);
        return userList;
    }
    public String getName(String number){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CALLERS, new String[] {CALLER_NAME, CALLER_NUMBER}, CALLER_NAME+ "=?", new String[]{number}, null, null, null, null );
        if (cursor != null)
            cursor.moveToFirst();
        return cursor.getString(0);
    }
}
