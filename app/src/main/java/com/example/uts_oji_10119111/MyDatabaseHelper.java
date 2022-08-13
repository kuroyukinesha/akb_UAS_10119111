package com.example.uts_oji_10119111;
//nim : 10119111
//nama : fauzi fikrinullah
//kelas : if-3

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    //db
    private static final String DATABASE_NAME = "BookLibrary.db";
    private static final int DATABASE_VERSION = 1;

    //users
    private static final String TABLE_USER = "users";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";


    //notes
    private static final String TABLE_NAME = "my_library";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "book_title";
    private static final String COLUMN_AUTHOR = "book_author";
    private static final String COLUMN_PAGES = "book_pages";
    private static final String COLUMN_DATE = "book_date";

    MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //user db
        String queryUser = "CREATE TABLE " + TABLE_USER +
                " (" + COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
                COLUMN_PASSWORD + " TEXT);";
        db.execSQL(queryUser);

        //note db
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_PAGES + " TEXT, " +
                COLUMN_DATE + " TEXT);";
        db.execSQL(query);
    }


    void addBook(String title, String author, String pages){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDate = new SimpleDateFormat("MMM dd, yyyy");
        String created = simpleDate.format(calendar.getTime());

        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);
        cv.put(COLUMN_DATE, created);
        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String title, String author, String pages, String date){


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);
        cv.put(COLUMN_DATE, date);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase noteDB, int i, int i1) {

        noteDB.execSQL(new StringBuilder().append("DROP TABLE IF EXISTS, my_library").append(TABLE_USER).toString());
        onCreate(noteDB);
    }

    public Boolean insertData(String username, String password){
        SQLiteDatabase noteDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        long result = noteDB.insert(TABLE_USER, null, contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }

    public Boolean checkUsername(String username){
        SQLiteDatabase noteDB = this.getWritableDatabase();
        Cursor cursor = noteDB.rawQuery("select * from " + TABLE_USER + " where " + COLUMN_USERNAME +
                " = ?", new String[] {username});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkUsernamePassword(String username, String password){
        SQLiteDatabase noteDB = this.getWritableDatabase();
        Cursor cursor = noteDB.rawQuery("select * from " + TABLE_USER + " where " + COLUMN_USERNAME +
                " = ? and " + COLUMN_PASSWORD + " = ?", new String[] {username, password});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

}
