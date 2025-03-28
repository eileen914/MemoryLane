package com.example.memorylane.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoryDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MemoryLane.db";
    private static final int DATABASE_VERSION = 1;

    public MemoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS UserMemory (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userName TEXT NOT NULL, " +
                "question TEXT NOT NULL, " +
                "answer TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS UserMemory");
        onCreate(db);
    }
}