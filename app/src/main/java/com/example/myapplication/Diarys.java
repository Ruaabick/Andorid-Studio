package com.example.myapplication;
import android.provider.BaseColumns;

public class Diarys {
    public Diarys() {
    }
    public static abstract class Diary implements BaseColumns {
        public static final String TABLE_NAME="diary";
        public static final String COLUMN_NAME_Title="title";//标题
        public static final String COLUMN_NAME_Date="date";//日期
        public static final String COLUMN_NAME_Content="content";//内容
        public static final String COLUMN_NAME_Author="author";//作者
    }
}
