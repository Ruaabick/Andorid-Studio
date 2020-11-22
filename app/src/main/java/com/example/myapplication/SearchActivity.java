package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listsearch);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        List<Map<String, String>> items= (List<Map<String, String>>) bundle.getSerializable("result");
        setDiarysListView(items);
    }
    private void setDiarysListView(List<Map<String, String>> items){//适配器，在列表中显示单词
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.item, new String[]{Diarys.Diary._ID,Diarys.Diary.COLUMN_NAME_Title, Diarys.Diary.COLUMN_NAME_Date, Diarys.Diary.COLUMN_NAME_Content, Diarys.Diary.COLUMN_NAME_Author},
                new int[]{R.id.textId,R.id.textViewTitle, R.id.textViewDate, R.id.textViewContent, R.id.textViewAuthor});
        ListView list = (ListView) findViewById(R.id.listView2);
        list.setAdapter(adapter);
    }

}
