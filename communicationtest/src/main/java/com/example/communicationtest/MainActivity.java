package com.example.communicationtest;

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
        Uri uri = Uri.parse("content://com.example.apple.androidlearn/user");

        ContentResolver contentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("_id", 5);
        values.put("name", "LiLi");
        Uri insert = contentResolver.insert(uri, values);

        Cursor query = contentResolver.query(uri, new String[]{"_id", "name"}, null, null, null);
        while (query.moveToNext()){
            int anInt = query.getInt(0);
            String string = query.getString(1);
            Log.i("ljing", "onCreate: ");
        }

    }
}
