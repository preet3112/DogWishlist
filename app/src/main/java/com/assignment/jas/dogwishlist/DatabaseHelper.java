package com.assignment.jas.dogwishlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE= "Likeddog.db";
    public static final String TABLE_NAME="liked_dog";
    public static final String COL_1= "ID";
    public static final String COL_2= "URL";
    public static final String COL_3= "DATE";
    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, 1);
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,URL text,DATE text )");

    }

    public boolean insert(String urL,String date){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_2,urL);
        contentValues.put(COL_3,date);
        long result=sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        if (result==1) {
            return true;
        }
        else {
            return false;

        }

    }
    public Cursor getData(){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor res=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);

    }
}
