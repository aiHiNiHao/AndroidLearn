package com.example.apple.androidlearn;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentResolver contentResolver = getContentResolver();
        Uri uri = Uri.parse("content://com.example.apple.androidlearn/user");
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", 3);
        contentValues.put("name", "Jason");
        contentResolver.insert(uri, contentValues);

        Cursor query = contentResolver.query(uri, new String[]{"_id", "name"}, null, null, null);
        while (query.moveToNext()) {
            String columnName = query.getColumnName(1);
            int anInt = query.getInt(0);
            String string = query.getString(1);
            Log.i("lijing", "columnName: " + columnName);
        }


        ContentValues updateValues = new ContentValues();
        updateValues.put("name", "WangMaZi");
        contentResolver.update(uri, updateValues,"_id = ?", new String[]{"1"});

        Cursor query1 = contentResolver.query(uri, new String[]{"_id", "name"}, null, null, null);
        while (query1.moveToNext()) {
            String columnName = query1.getColumnName(1);
            int anInt = query1.getInt(0);
            String string = query1.getString(1);
            Log.i("lijing", "columnName: " + columnName);
        }
    }
}
