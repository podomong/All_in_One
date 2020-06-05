package com.example.chapterandcontentfragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AllinOneDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    //public static final String DATABASE_NAME = "/data/data/com.example.chapterandcontentfragment/databases/AllinOne.db";
    public static final String DATABASE_NAME = "AllinOne.db";
    public static String DATABASE_PATH;
    private boolean isCreated = false;

    public AllinOneDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //DATABASE_PATH = context.getApplicationInfo().dataDir +"/databases/"+DATABASE_NAME;
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(AllinOneContract.SQL_CREATE_COURSE);
        db.execSQL(AllinOneContract.SQL_CREATE_CHAPTER);
        db.execSQL(AllinOneContract.SQL_CREATE_CONTENT);
        db.execSQL(AllinOneContract.SQL_CREATE_BOARD);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(AllinOneContract.SQL_DELETE_COURSE);
        db.execSQL(AllinOneContract.SQL_DELETE_CHAPTER);
        db.execSQL(AllinOneContract.SQL_DELETE_CONTENT);
        db.execSQL(AllinOneContract.SQL_DELETE_BOARD);
        onCreate(db);
    }

    public boolean isCreated(){ return isCreated;}
    public void setCreatedOn(){isCreated = true;}
    //input data into Course Table
    long insertColumn(SQLiteDatabase db, int courseId, String title, String instruction, String illustration){
        ContentValues values = new ContentValues();
        values.put(AllinOneContract.Course.COURSE_ID, courseId);
        values.put(AllinOneContract.Course.TITLE, title);
        values.put(AllinOneContract.Course.INSTRUCTION, instruction);
        values.put(AllinOneContract.Course.ILLUSTRATION, illustration);
        return db.insert(AllinOneContract.Course.TABLE_NAME, null, values);
    }

    //input data into Chapter Table
    public long insertColumn(SQLiteDatabase db, int courseId, int chapterId, String title, byte[] image){
        ContentValues values = new ContentValues();
        values.put(AllinOneContract.Chapter.COURSE_ID, courseId);
        values.put(AllinOneContract.Chapter.CHPATER_ID, chapterId);
        values.put(AllinOneContract.Chapter.TITLE, title);
        values.put(AllinOneContract.Chapter.IMAGE, image);
        return db.insert(AllinOneContract.Chapter.TABLE_NAME, null, values);
    }

    //input data into Content Table
    public long insertColumn(SQLiteDatabase db, int courseId, int chapterId, int contentId, String instruction){
        ContentValues values = new ContentValues();
        values.put(AllinOneContract.Content.COURSE_ID, courseId);
        values.put(AllinOneContract.Content.CHPATER_ID, chapterId);
        values.put(AllinOneContract.Content.CONTENT_ID, contentId);
        values.put(AllinOneContract.Content.INSTRUCTION, instruction);
        return db.insert(AllinOneContract.Content.TABLE_NAME, null, values);
    }

    //input data into Board Table
    public long insertColumn(SQLiteDatabase db, int courseId, int chapterId, int contentId, int boardRow, int boardCol, int boardType, int guideMode){
        ContentValues values = new ContentValues();
        values.put(AllinOneContract.Board.COURSE_ID, courseId);
        values.put(AllinOneContract.Board.CHPATER_ID, chapterId);
        values.put(AllinOneContract.Board.CONTENT_ID, contentId);
        values.put(AllinOneContract.Board.ROW, boardRow);
        values.put(AllinOneContract.Board.COL, boardCol);
        values.put(AllinOneContract.Board.TYPE, boardType);
        values.put(AllinOneContract.Board.GUIDE_MODE, guideMode);
        return db.insert(AllinOneContract.Board.TABLE_NAME, null, values);
    }
}
