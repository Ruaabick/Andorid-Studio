package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class UpdateActivity extends AppCompatActivity {
    DiaryDBHelper data;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bianji);
        data = new DiaryDBHelper(this);
        Intent intent=getIntent();
        Button btn_confirm = findViewById(R.id.Confirm);
        Button btn_cancel = findViewById(R.id.Cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UpdateActivity.this,MainActivity.class);
                startActivity(intent1);
            }
        });
        final EditText eTitle = findViewById(R.id.ETitle);
        final EditText eContent = findViewById(R.id.EContent);
        final EditText eDate = findViewById(R.id.EDate);
        final EditText eAuthor = findViewById(R.id.EAuthor);
        eTitle.setText(intent.getStringExtra("Title"));
        eDate.setText(intent.getStringExtra("Date"));
        eContent.setText(intent.getStringExtra("Content"));
        eAuthor.setText(intent.getStringExtra("Author"));
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UpdateActivity.this,MainActivity.class);
                String etitle = eTitle.getText().toString();
                String edate = eDate.getText().toString();
                String econtent = eContent.getText().toString();
                String eauthor = eAuthor.getText().toString();
                intent1.putExtra("Title",etitle);
                intent1.putExtra("Date",edate);
                intent1.putExtra("Content",econtent);
                intent1.putExtra("Author",eauthor);
                setResult(3,intent1);
                finish();
            }
        });
    }

}
