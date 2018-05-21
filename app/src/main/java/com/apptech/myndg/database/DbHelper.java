package com.apptech.myndg.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nirob on 5/11/18.
 */

public class DbHelper extends SQLiteOpenHelper implements BaseDbHelper {

    static final String DATABASE_NAME = "myndg.sqlite";
    static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + CarInfoTableEntity.TABLE_NAME + " (" +
            CarInfoTableEntity._ID + INTEGER_PRIMARY + COMMA_SEP +
            CarInfoTableEntity.NAME + TEXT_TYPE + COMMA_SEP +
            CarInfoTableEntity.STATUS + TEXT_TYPE + COMMA_SEP +
            CarInfoTableEntity.DATE_TIME + TEXT_TYPE + COMMA_SEP +
            CarInfoTableEntity.REGION + TEXT_TYPE + COMMA_SEP +
            CarInfoTableEntity.LANGUAGE + TEXT_TYPE + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CarInfoTableEntity.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
}
