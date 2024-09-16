package com.example.mytodo.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Constants.CREATE_PACKAGE);
        sqLiteDatabase.execSQL(Constants.CREATE_CATEGORY);
        sqLiteDatabase.execSQL(Constants.CREATE_RECORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(Constants.DROP_PACKAGE);
        sqLiteDatabase.execSQL(Constants.DROP_CATEGORY);
        sqLiteDatabase.execSQL(Constants.DROP_RECORD);

        sqLiteDatabase.execSQL(Constants.CREATE_PACKAGE);
        sqLiteDatabase.execSQL(Constants.CREATE_CATEGORY);
        sqLiteDatabase.execSQL(Constants.CREATE_RECORD);
    }

}
