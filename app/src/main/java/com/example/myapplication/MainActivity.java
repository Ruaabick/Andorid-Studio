package com.example.myapplication;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity{
    DiaryDBHelper data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView list = (ListView) findViewById(R.id.listView1);
        registerForContextMenu(list);
        data = new DiaryDBHelper(this);
        List<Map<String, String>> items=getAll();
        setDiarysListView(items);
    }

    protected void onDestroy() {
        super.onDestroy();
        data.close();
    }
    private void setDiarysListView(List<Map<String, String>> items){//适配器，在列表中显示日记
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.item, new String[]{Diarys.Diary._ID,Diarys.Diary.COLUMN_NAME_Title, Diarys.Diary.COLUMN_NAME_Date, Diarys.Diary.COLUMN_NAME_Content,Diarys.Diary.COLUMN_NAME_Author},
                new int[]{R.id.textId,R.id.textViewTitle, R.id.textViewDate, R.id.textViewContent, R.id.textViewAuthor});
        ListView list = (ListView) findViewById(R.id.listView1);
        list.setAdapter(adapter);
    }
    Button Confirm, Cancel;

    public boolean onCreateOptionsMenu(Menu menu) {//创建菜单
        getMenuInflater().inflate(R.menu.find, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {//创建查找和增加选项
        int id = item.getItemId();
        switch (id) {
            case R.id.search:
                SearchDialog();
                return true;
            case R.id.insert:
                InsertDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);//改//
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {//长按删除或修改
        getMenuInflater().inflate(R.menu.modify, menu);
    }
    public boolean onContextItemSelected(MenuItem item) {
        TextView textId=null;
        TextView textTitle=null;
        TextView textDate=null;
        TextView textContent=null;
        TextView textAuthor=null;
        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;
        switch (item.getItemId()){
            case R.id.delete:
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.textId);
                if(textId!=null){
                    String strId=textId.getText().toString();
                    DeleteDialog(strId);
                }
                break;
            case R.id.update:
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;

                textId =(TextView)itemView.findViewById(R.id.textId);
                textTitle =(TextView)itemView.findViewById(R.id.textViewTitle);
                textDate =(TextView)itemView.findViewById(R.id.textViewDate);
                textContent =(TextView)itemView.findViewById(R.id.textViewContent);
                textAuthor =(TextView)itemView.findViewById(R.id.textViewAuthor);
                String textid = textId.getText().toString();
                String texttitle = textTitle.getText().toString();
                String textdate = textDate.getText().toString();
                String textcontent = textContent.getText().toString();
                String textauthor = textAuthor.getText().toString();
                Intent intent = new Intent(MainActivity.this,UpdateActivity.class);
                id = textid;
                intent.putExtra("Title",texttitle);
                intent.putExtra("Date",textdate);
                intent.putExtra("Content",textcontent);
                intent.putExtra("Author",textauthor);
                startActivityForResult(intent,1);


                UpdateUseSql(textid,texttitle,textdate,textcontent,textauthor);
                break;
        }
        return true;
    }
    private String id=null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == 3)
        {
            String t = data.getStringExtra("Title");
            String d = data.getStringExtra("Date");
            String c = data.getStringExtra("Content");
            String a = data.getStringExtra("Author");
            TextView textId = findViewById(R.id.textId);
            TextView textTitle =(TextView)findViewById(R.id.textViewTitle);
            TextView textDate =(TextView)findViewById(R.id.textViewDate);
            TextView textContent =(TextView)findViewById(R.id.textViewContent);
            TextView textAuthor =(TextView)findViewById(R.id.textViewAuthor);
            String textid = textId.getText().toString();
            textTitle.setText(t);
            textDate.setText(d);
            textContent.setText(c);
            textAuthor.setText(a);
            UpdateUseSql(id,t,d,c,a);
            List<Map<String, String>> items=getAll();
            setDiarysListView(items);

        }
    }

    private List<Map<String, String>> getAll(){
        SQLiteDatabase db=data.getReadableDatabase();
        ArrayList<Map<String, String>> result = new ArrayList<>();
        String[] diarys = {
                Diarys.Diary._ID,
                Diarys.Diary.COLUMN_NAME_Title,
                Diarys.Diary.COLUMN_NAME_Date,
                Diarys.Diary.COLUMN_NAME_Content,
                Diarys.Diary.COLUMN_NAME_Author,
        };
        String px =
                Diarys.Diary.COLUMN_NAME_Title + " DESC";
        Cursor cursor = db.query(Diarys.Diary.TABLE_NAME,diarys, null, null, null, null, px);
        while (cursor.moveToNext()) {//将Cursor对象转换为list 显示在listView
            Map<String, String> map = new HashMap<>();
            map.put(Diarys.Diary._ID, String.valueOf(cursor.getInt(0)));
            map.put(Diarys.Diary.COLUMN_NAME_Title, cursor.getString(1));
            map.put(Diarys.Diary.COLUMN_NAME_Date, cursor.getString(2));
            map.put(Diarys.Diary.COLUMN_NAME_Content, cursor.getString(3));
            map.put(Diarys.Diary.COLUMN_NAME_Author, cursor.getString(4));
            result.add(map);
        }
        return result;
    }
    private void InsertDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        new AlertDialog.Builder(this)
                .setTitle("新增日记")//标题
                .setView(tableLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strTitle=((EditText)tableLayout.findViewById(R.id.txtTitle)).getText().toString();
                        String strDate=((EditText)tableLayout.findViewById(R.id.txtDate)).getText().toString();
                        String strContent=((EditText)tableLayout.findViewById(R.id.txtContent)).getText().toString();
                        String strAuthor=((EditText)tableLayout.findViewById(R.id.txtAuthor)).getText().toString();
                        InsertUserSql(strTitle, strDate, strContent,strAuthor);
                        List<Map<String, String>> items=getAll();
                        setDiarysListView(items);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()//创建对话框
                .show();//显示对话框
    }




//        new AlertDialog.Builder(this)
//                .setTitle("修改日记")//标题
//                .setView(tableLayout)//设置视图
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        String NewTitle = ((EditText) tableLayout.findViewById(R.id.txtTitle)).getText().toString();
//                        String NewDate = ((EditText) tableLayout.findViewById(R.id.txtDate)).getText().toString();
//                        String NewContent = ((EditText) tableLayout.findViewById(R.id.txtContent)).getText().toString();
//                        String NewAuthor = ((EditText) tableLayout.findViewById(R.id.txtAuthor)).getText().toString();
//                        UpdateUseSql(strId, NewTitle, NewDate, NewContent, NewAuthor);
//                        setDiarysListView(getAll());
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                })
//                .create()
//                .show();



    private void DeleteDialog(final String strId){
        new AlertDialog.Builder(this).setTitle("删除日记").setMessage("是否真的删除日记?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteUseSql(strId);
                setDiarysListView(getAll());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }
    private void SearchDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.search, null);
        new AlertDialog.Builder(this)
                .setTitle("查找日记")//标题
                .setView(tableLayout)//设置视图
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txtSearchDiary=((EditText)tableLayout.findViewById(R.id.Search)).getText().toString();
                        ArrayList<Map<String, String>> items=null;
                       items=SearchUseSql(txtSearchDiary);
                        if(items.size()>0) {
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("result",items);
                            Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        }else
                            Toast.makeText(MainActivity.this,"没有找到", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create()//创建对话框
                .show();//显示对话框

    }
    private void InsertUserSql(String strTitle, String strDate, String strContent, String strAuthor){
        String sql="insert into diary(title,date,content,author) values(?,?,?,?)";
        SQLiteDatabase db = data.getWritableDatabase();
        db.execSQL(sql,new String[]{strTitle,strDate,strContent,strAuthor});
    }
    //使用Sql语句更新单词
    private void UpdateUseSql(String strId,String strTitle, String strDate, String strContent, String strAuthor) {
        SQLiteDatabase db =data.getReadableDatabase();
        String sql="update diary set title=?,date=?,content=?,author=? where _id=?";
        db.execSQL(sql, new String[]{strTitle, strDate, strContent, strAuthor, strId});
    }
    private void DeleteUseSql(String strId) {
        String sql="delete from diary where _id='"+strId+"'";
        SQLiteDatabase db = data.getReadableDatabase();
        db.execSQL(sql);
    }
    private ArrayList<Map<String, String>> SearchUseSql(String strDiarySearch){
        SQLiteDatabase db=data.getReadableDatabase();
        ArrayList<Map<String, String>> result = new ArrayList<>();
        String sql="select * from diary where title like ? order by title desc";
        Cursor cursor=db.rawQuery(sql,new String[]{"%"+strDiarySearch+"%"});
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(Diarys.Diary._ID, String.valueOf(cursor.getInt(0)));
            map.put(Diarys.Diary.COLUMN_NAME_Title, cursor.getString(1));
            map.put(Diarys.Diary.COLUMN_NAME_Date, cursor.getString(2));
            map.put(Diarys.Diary.COLUMN_NAME_Content, cursor.getString(3));
            map.put(Diarys.Diary.COLUMN_NAME_Author, cursor.getString(4));
            result.add(map);
        }
        return  result;
    }
}


