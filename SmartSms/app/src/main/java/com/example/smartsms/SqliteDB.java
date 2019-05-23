package com.example.smartsms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


import android.graphics.Paint;
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

    private static final String TABLE_NAME3="CapturedRules";
    private static final String COL1_C="ID";
    private static final String COL2_C="ruleName";
    private static final String COL3_C="textSMS";
    private static final String COL4_C = "seed";

    private static final String TABLE_NAME4="Color";
    private static final String COL1_CO="ID";
    private static final String COL2_CO="color";
    private static final String COL3_CO="color_priority";

    public static String DB_FILEPATH = "/data/data/com.example.smartsms/databases/Database.db";



    public SqliteDB(Context context)
    {
        super(context, DATABASE_NAME, null, 8);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME1 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,COLOR TEXT,PICTUREPATH TEXT,MUSICPATH TEXT)");
        db.execSQL("create table " + TABLE_NAME2 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,ID_PRIORITY INTEGER,PHRASE TEXT,NAME TEXT,PHONE TEXT, FOREIGN KEY(ID_PRIORITY) REFERENCES "+ TABLE_NAME1+"(ID) )");
        db.execSQL("create table " + TABLE_NAME3 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,RULENAME TEXT,TEXTSMS TEXT,SEED INTEGER)");
        db.execSQL("create table " + TABLE_NAME4 +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,COLOR TEXT,COLOR_PRIORITY INTEGER)");
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4);
        onCreate(db);
    }

    public boolean importDatabase(String dbPath) throws IOException {
        close();
        File newDb = new File(dbPath);
        File oldDb = new File(DB_FILEPATH);
        if (newDb.exists()) {
            FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            getWritableDatabase().close();
            return true;
        }
        return false;
    }

    public boolean exportDatabase(String dbPath) throws IOException
    {
        close();
        File newDb = new File(dbPath);
        File oldDb = new File(DB_FILEPATH);
        if (newDb.exists()) {
            FileUtils.copyFile(new FileInputStream(oldDb), new FileOutputStream(newDb));
            getWritableDatabase().close();
            return true;
        }
        return false;
    }


    public boolean addColorPriority(ColorPriority colorPriority)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2_CO, colorPriority.color);
        contentValues.put(COL3_CO, colorPriority.color_priority);

        long result = db.insert(TABLE_NAME4, null, contentValues);
        (db).close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public ColorPriority getColorPriority(String color)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String color_0=null;
        int color_priority=0;
        String selectQuery = "SELECT * from "+TABLE_NAME4+" where COLOR = '" + color+ "'";
        Cursor cursor=(db).rawQuery(selectQuery,null);
        while (cursor.moveToNext()) {
            color_0=cursor.getString(1);
            color_priority=cursor.getInt(2);
        }
        ColorPriority colorPriority = new ColorPriority(color_0,color_priority);
        cursor.close();
        (db).close();
        return colorPriority;
    }

    public ArrayList<ColorPriority> getAllColor()
    {
        ArrayList<ColorPriority> list= new ArrayList<ColorPriority>();
        SQLiteDatabase db = this.getWritableDatabase();
        String name_0=null;
        int color_1 = 0;
        String selectQuery = "SELECT * from "+TABLE_NAME4;
        Cursor cursor=(db).rawQuery(selectQuery,null);
        while (cursor.moveToNext()) {
            name_0=cursor.getString(1);
            color_1=cursor.getInt(2);
            ColorPriority colorPriority = new ColorPriority(name_0,color_1);
            list.add(colorPriority);
        }
        cursor.close();
        (db).close();
        return list;
    }


    public void deleteColorPriority(String color)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME4, "COLOR = ?", new String[]{color});
    }

    public boolean addCapturedRule(CapturedRule capturedRule)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2_C, capturedRule.nameRule);
        contentValues.put(COL3_C, capturedRule.smsText);
        contentValues.put(COL4_C,capturedRule.seed);

        long result = db.insert(TABLE_NAME3, null, contentValues);
        (db).close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addPriority(Priority priority) {
        if(getPriority(priority.name).name!=null)
        {
            return false;
        }
        else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL2_P, priority.name);
            contentValues.put(COL3_P, priority.color);
            contentValues.put(COL4_P, priority.pngPath);
            contentValues.put(COL5_P, priority.musicPath);

            long result = db.insert(TABLE_NAME1, null, contentValues);
            //if date as inserted incorrectly it will return -1
            (db).close();
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
    }

    public boolean addRule(Rule rule) {
        if(getRule(rule.name).name!=null)
        {
            return false;
        }
        else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            String result_0 = null;
            String selectQuery = "SELECT ID from " + TABLE_NAME1 + " where NAME = '" + rule.priority.name + "'";
            Cursor cursor = (db).rawQuery(selectQuery, null);
            while (cursor.moveToNext()) {
                result_0 = cursor.getString(0);
            }

            Integer ID = Integer.valueOf(result_0);
            contentValues.put(COL2_R, ID);
            contentValues.put(COL3_R, rule.phrase);
            contentValues.put(COL4_R, rule.name);
            contentValues.put(COL5_R, rule.phoneNumber);

            long result = db.insert(TABLE_NAME2, null, contentValues);
            //if date as inserted incorrectly it will return -1
            cursor.close();
            (db).close();
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }
    }

    public CapturedRule getCapturedRule(int seed)
    {
        String str_seed=Integer.toString(seed);
        SQLiteDatabase db = this.getWritableDatabase();
        String nameRule_0 = null;
        String smsText_1 = null;
        int seed_2 = 0;
        String selectQuery = "SELECT * from "+TABLE_NAME3+" where SEED = '" + str_seed+ "'";
        Cursor cursor=(db).rawQuery(selectQuery,null);
        while (cursor.moveToNext()) {
            nameRule_0=cursor.getString(1);
            smsText_1=cursor.getString(2);
            seed_2=cursor.getInt(3);
        }
        CapturedRule capturedRule = new CapturedRule(nameRule_0,smsText_1,seed_2);
        cursor.close();
        (db).close();
        return capturedRule;
    }

    public Priority getPriority(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String name_0=null;
        String color_1=null;
        String pngPath_2=null;
        String musicPath_3=null;
        String selectQuery = "SELECT * from "+TABLE_NAME1+" where NAME = '" + name+ "'";
        Cursor cursor=(db).rawQuery(selectQuery,null);
        while (cursor.moveToNext()) {
            name_0=cursor.getString(1);
            color_1=cursor.getString(2);
            pngPath_2=cursor.getString(3);
            musicPath_3=cursor.getString(4);
        }
        Priority priority = new Priority(name_0,color_1,pngPath_2,musicPath_3);
        cursor.close();
        (db).close();
        return priority;
    }

    public void deletePriority(String name)
    {
        /*String p_id = null;
        String r_id = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * from "+TABLE_NAME1+" where NAME = '" + name+ "'";
        Cursor cursor=(db).rawQuery(selectQuery,null);
        while (cursor.moveToNext()) {
            p_id = cursor.getString(0);
        }
        String selectQuery2 = "SELECT * from "+TABLE_NAME2+" where ID_PRIORITY = '" + p_id + "'";
        cursor=(db).rawQuery(selectQuery2,null);
        {
             r_id = cursor.getString(0);
        }
        db.delete(TABLE_NAME2, "name = ?", new String[]{r_id});
        */
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME1, "name = ?", new String[]{name});
    }
    public void deleteRule(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME2, "name = ?", new String[]{name});
    }

    public void deleteCapturedRule(int seed)
    {
        String str_seed = Integer.toString(seed);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME3, "seed = ?", new String[]{str_seed});
    }

    public ArrayList<Priority> getAllPriority()
    {
        ArrayList<Priority> list= new ArrayList<Priority>();
        SQLiteDatabase db = this.getWritableDatabase();
        String name_0=null;
        String color_1=null;
        String pngPath_2=null;
        String musicPath_3=null;
        String selectQuery = "SELECT * from "+TABLE_NAME1;
        Cursor cursor=(db).rawQuery(selectQuery,null);
        while (cursor.moveToNext()) {
            name_0=cursor.getString(1);
            color_1=cursor.getString(2);
            pngPath_2=cursor.getString(3);
            musicPath_3=cursor.getString(4);
            Priority priority = new Priority(name_0,color_1,pngPath_2,musicPath_3);
            list.add(priority);
        }
        cursor.close();
        (db).close();
        return list;
    }

    public ArrayList<CapturedRule> getAllCapturedRule()
    {
        ArrayList<CapturedRule> list= new ArrayList<CapturedRule>();
        SQLiteDatabase db = this.getWritableDatabase();
        String nameRule_0 = null;
        String smsText_1 = null;
        int seed_2 = 0;
        String selectQuery = "SELECT * from "+TABLE_NAME3;
        Cursor cursor=(db).rawQuery(selectQuery,null);
        while (cursor.moveToNext()) {
            nameRule_0=cursor.getString(1);
            smsText_1=cursor.getString(2);
            seed_2=cursor.getInt(3);

            CapturedRule capturedRule = new CapturedRule(nameRule_0,smsText_1,seed_2);
            list.add(capturedRule);
        }
        cursor.close();
        (db).close();
        return list;
    }

    public ArrayList<Rule> getAllRule()
    {
        ArrayList<Rule> list= new ArrayList<Rule>();
        SQLiteDatabase db = this.getWritableDatabase();
        String r_ID_Priority_0=null;
        String r_phrase_1=null;
        String r_name_2=null;
        String r_phone_3=null;

        String selectQuery1 = "SELECT * from "+TABLE_NAME2;

        String p_name_0=null;
        String p_color_1=null;
        String p_pngPath_2=null;
        String p_musicPath_3=null;
        Cursor cursor=(db).rawQuery(selectQuery1,null);
        while (cursor.moveToNext()) {
            r_ID_Priority_0=cursor.getString(1);
            r_phrase_1=cursor.getString(2);
            r_name_2=cursor.getString(3);
            r_phone_3=cursor.getString(4);

             p_name_0=null;
             p_color_1=null;
             p_pngPath_2=null;
             p_musicPath_3=null;

            String selectQuery2 = "SELECT * from "+TABLE_NAME1+" where ID = '" + r_ID_Priority_0 + "'";
            Cursor cursor2=(db).rawQuery(selectQuery2,null);

            while (cursor2.moveToNext()) {
                p_name_0=cursor2.getString(1);
                p_color_1=cursor2.getString(2);
                p_pngPath_2=cursor2.getString(3);
                p_musicPath_3=cursor2.getString(4);
            }
            Priority priority = new Priority(p_name_0,p_color_1,p_pngPath_2,p_musicPath_3);
            Rule rule = new Rule (r_name_2,r_phrase_1,r_phone_3, priority);
            list.add(rule);
            cursor2.close();
        }
        cursor.close();
        (db).close();
        return list;
    }

    public Rule getRule(String name)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        String r_ID_Priority_0=null;
        String r_phrase_1=null;
        String r_name_2=null;
        String r_phone_3=null;

        String selectQuery1 = "SELECT * from "+TABLE_NAME2+" where NAME = '" + name+ "'";

        Cursor cursor=(db).rawQuery(selectQuery1,null);
        while (cursor.moveToNext()) {
            r_ID_Priority_0=cursor.getString(1);
            r_phrase_1=cursor.getString(2);
            r_name_2=cursor.getString(3);
            r_phone_3=cursor.getString(4);
        }

        String p_name_0=null;
        String p_color_1=null;
        String p_pngPath_2=null;
        String p_musicPath_3=null;

        String selectQuery2 = "SELECT * from "+TABLE_NAME1+" where ID = '" + r_ID_Priority_0 + "'";
        cursor=(db).rawQuery(selectQuery2,null);

        while (cursor.moveToNext()) {
            p_name_0=cursor.getString(1);
            p_color_1=cursor.getString(2);
            p_pngPath_2=cursor.getString(3);
            p_musicPath_3=cursor.getString(4);
        }

        Priority priority = new Priority(p_name_0,p_color_1,p_pngPath_2,p_musicPath_3);
        Rule rule = new Rule (r_name_2,r_phrase_1,r_phone_3, priority);
        cursor.close();
        (db).close();
        return rule;
    }


}
