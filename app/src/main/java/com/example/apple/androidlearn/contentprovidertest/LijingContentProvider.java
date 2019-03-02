package com.example.apple.androidlearn.contentprovidertest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class LijingContentProvider extends ContentProvider {
    private Context mContext;
    private LijingSqliteOpenHelp mSqlHelper;
    private SQLiteDatabase mDb;

    public static final String AUTOHORITY = "com.example.apple.androidlearn";

    private static final int USER_CODE = 1;
    private static final int JOB_CODE = 2;
    private static final UriMatcher mMatcher;

    static {
        mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mMatcher.addURI(AUTOHORITY, "user", USER_CODE);
        mMatcher.addURI(AUTOHORITY, "job", JOB_CODE);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mSqlHelper = new LijingSqliteOpenHelp(mContext);
        mDb = mSqlHelper.getWritableDatabase();

        mDb.execSQL("delete from "+LijingSqliteOpenHelp.JOB_TABLE_NAME);
        mDb.execSQL("insert into "+LijingSqliteOpenHelp.JOB_TABLE_NAME+" values (1, 'singer')");
        mDb.execSQL("insert into "+LijingSqliteOpenHelp.JOB_TABLE_NAME+" values (2, 'programmer')");

        mDb.execSQL("delete from " + LijingSqliteOpenHelp.USER_TABLE_NAME);
        ContentValues values1 = new ContentValues();
        ContentValues values2 = new ContentValues();
        values1.put("_id", 1);
        values1.put("name", "Lee");

        values2.put("_id", 2);
        values2.put("name", "Alan");
        mDb.insert(LijingSqliteOpenHelp.USER_TABLE_NAME, null, values1);
        mDb.insert(LijingSqliteOpenHelp.USER_TABLE_NAME, null, values2);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = getTableName(uri);

        Cursor query = mDb.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        return query;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        String tableName = getTableName(uri);
        mDb.insert(tableName, null, values);

        mContext.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        int delete = mDb.delete(tableName, selection, selectionArgs);
        mContext.getContentResolver().notifyChange(uri, null);

        return delete;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = getTableName(uri);
        int update = mDb.update(tableName, values, selection, selectionArgs);
        return update;
    }

    private String getTableName(Uri uri) {
        int match = mMatcher.match(uri);
        switch (match) {
            case USER_CODE:
                return LijingSqliteOpenHelp.USER_TABLE_NAME;
            case JOB_CODE:
                return LijingSqliteOpenHelp.JOB_TABLE_NAME;
            default:
                return null;
        }
    }
}
