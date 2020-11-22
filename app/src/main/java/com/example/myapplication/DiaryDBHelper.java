package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DiaryDBHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "Diarydb";//数据库名字
    private final static int DATABASE_VERSION = 1;//数据库版本

    //建表SQL
    private final static String SQL_CREATE_DATABASE = "CREATE TABLE " + Diarys.Diary.TABLE_NAME + " (" +
            Diarys.Diary._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            Diarys.Diary.COLUMN_NAME_Title + " TEXT" + "," +
            Diarys.Diary.COLUMN_NAME_Date + " TEXT" + ","
            + Diarys.Diary.COLUMN_NAME_Content + " TEXT" + ","
            + Diarys.Diary.COLUMN_NAME_Author + " TEXT" + ")";

    //删表SQL
    private final static String SQL_DELETE_DATABASE = "DROP TABLE IF EXISTS " + Diarys.Diary.TABLE_NAME;

    public DiaryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建数据库
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //当数据库升级时被调用，首先删除旧表，然后调用OnCreate()创建新表
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        onCreate(sqLiteDatabase);
    }
}
