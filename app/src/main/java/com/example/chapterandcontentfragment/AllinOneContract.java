package com.example.chapterandcontentfragment;

import android.content.Context;
import android.provider.BaseColumns;

public final class AllinOneContract  {
    public static final String SQL_CREATE_COURSE =
            "CREATE TABLE "+Course.TABLE_NAME + " ("+
                    Course._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Course.COURSE_ID+" INTEGER, " +
                    Course.TITLE+" TEXT, "+
                    Course.INSTRUCTION+" TEXT, "+
                    Course.ILLUSTRATION+" BLOB)";
    public static final String SQL_CREATE_CHAPTER=
            "CREATE TABLE "+Chapter.TABLE_NAME+" ("+
                    Chapter._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    Chapter.COURSE_ID+" INTEGER, "+
                    Chapter.CHPATER_ID+" INTEGER, "+
                    Chapter.TITLE+" TEXT, "+
                    Chapter.IMAGE+" BLOB)";
    public static final String SQL_CREATE_CONTENT=
            "CREATE TABLE "+ Content.TABLE_NAME+" ("+
                    Content._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    Content.COURSE_ID+" INTEGER, "+
                    Content.CHPATER_ID+" INTEGER, "+
                    Content.CONTENT_ID+" INTEGER, "+
                    Content.INSTRUCTION+" TEXT)";
    public static final String SQL_CREATE_BOARD=
            "CREATE TABLE "+Board.TABLE_NAME+" ("+
                    Board._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    Board.COURSE_ID+" INTEGER, "+
                    Board.CHPATER_ID+" INTEGER, "+
                    Board.CONTENT_ID+" INTEGER, "+
                    Board.ROW+" INTEGER, "+
                    Board.COL+" INTEGER, "+
                    Board.TYPE+" INTEGER, "+
                    Board.GUIDE_MODE+" INTEGER)";

    public static final String SQL_DELETE_COURSE =
            "DROP TABLE IF EXISTS "+Course.TABLE_NAME;
    public static final String SQL_DELETE_CHAPTER=
            "DROP TABLE IF EXISTS "+Chapter.TABLE_NAME;
    public static final String SQL_DELETE_CONTENT=
            "DROP TABLE IF EXISTS "+Content.TABLE_NAME;
    public static final String SQL_DELETE_BOARD=
            "DROP TABLE IF EXISTS "+Board.TABLE_NAME;

    private AllinOneContract(){}

    public static class Course implements BaseColumns {
        public static final String TABLE_NAME = "COURSE";

        public static final String COURSE_ID = "COURSE_ID";

        public static final String TITLE = "COURSE_TITLE";
        public static final String INSTRUCTION = "COURSE_INSTRUCTION";
        public static final String ILLUSTRATION = "COURSE_ILLUSTRATION";
    }

    public static class Chapter implements BaseColumns{
        public static final String TABLE_NAME = "CHPATER";

        public static final String COURSE_ID = "COURSE_ID";
        public static final String CHPATER_ID = "CHPATER_ID";

        public static final String TITLE = "CHPATER_TITLE";
        public static final String IMAGE = "CHPATER_IMAGE";
    }

    public static class Content implements BaseColumns{
        public static final String TABLE_NAME = "CONTENT";

        public static final String COURSE_ID = "COURSE_ID";
        public static final String CHPATER_ID = "CHPATER_ID";
        public static final String CONTENT_ID = "CONTENT_ID";

        public static final String INSTRUCTION = "CONTENT_INSTRUCTION";
    }

    public static class Board implements BaseColumns{
        public static final String TABLE_NAME = "BOARD";

        public static final String COURSE_ID = "COURSE_ID";
        public static final String CHPATER_ID = "CHPATER_ID";
        public static final String CONTENT_ID = "CONTENT_ID";

        public static final String ROW = "BOARD_ROW";
        public static final String COL = "BOARD_COL";
        public static final String TYPE = "BOARD_TYPE";
        public static final String NUM = "BOARD_NUM";
        public static final String GUIDE_MODE = "BOARD_GUIDE_MODE";
        public static final String COLOR_MODE = "BOARD_COLOR_MODE";

        //columns for detail content
        public static final String TOP_TEXT = "TOP_TEXT";
        public static final String BOTTOM_TEXT = "BOTTOM_TEXT";
    }
}
