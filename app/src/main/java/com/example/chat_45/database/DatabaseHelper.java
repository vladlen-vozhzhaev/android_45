package com.example.chat_45.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chat45.db";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // вызывается при первом создании БД
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " user_id INTEGER NOT NULL," +
                    " login VARCHAR(255) NOT NULL," +
                    " name VARCHAR(255) NOT NULL," +
                    " lastname VARCHAR(255) NOT NULL" +
                ")");
    }

    // вызывается при изменении версии базы данных
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
