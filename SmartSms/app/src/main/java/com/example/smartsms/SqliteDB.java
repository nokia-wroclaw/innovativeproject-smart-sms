package com.example.smartsms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class SqliteDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="Database.db";
    private static final String TABLE_NAME1="Priority";
    private static final String COL1_P="ID";
    private static final String COL2_P="name";
    private static final String COL3_P="color";
    private static final String COL4_P="picturePath";
    private static final String COL5_P="musicPath";

    private static final String TABLE_NAME2="Rule";
    private static final String COL1_R="ID";
    private static final String COL2_R="ID_Priority";
    private static final String COL3_R="phrase";
    private static final String COL4_R="name";
    private static final String COL5_R="phone";



    public SqliteDB(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME1 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,COLOR TEXT,PICTUREPATH TEXT,MUSICPATH TEXT)");
        db.execSQL("create table " + TABLE_NAME2 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,ID_PRIORITY INTEGER,PHRASE TEXT,NAME TEXT,PHONE TEXT, FOREIGN KEY(ID_PRIORITY) REFERENCES "+ TABLE_NAME1+"(ID) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        onCreate(db);
    }

    public boolean addPriority(Priority priority) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2_P, priority.name);
        contentValues.put(COL3_P, priority.color);
        contentValues.put(COL4_P, priority.pngPath);
        contentValues.put(COL5_P, priority.musicPath);

        long result = db.insert(TABLE_NAME1, null, contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addRule(Rule rule) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int value = 666;
        String selectQuery = "SELECT ID from PRIORITY where NAME = " + rule.priority.name;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            value = cursor.getInt(0);
        }

        contentValues.put(COL2_R, value);
        contentValues.put(COL3_P, rule.phrase);
        contentValues.put(COL4_P, rule.name);
        contentValues.put(COL5_P, rule.phoneNumber);

        long result = db.insert(TABLE_NAME1, null, contentValues);
        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


}
