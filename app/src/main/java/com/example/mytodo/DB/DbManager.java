package com.example.mytodo.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DbManager {
    Context context;
    DbHelper dbHelper;
    SQLiteDatabase database;

    public DbManager(Context context) {
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    public void openDb(){
        database = dbHelper.getWritableDatabase();
    }public void closeDb(){
        database.close();
    }

    public void insertToPackage(int id, String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.PC_ID, id);
        contentValues.put(Constants.PC_NAME, name);
        database.insert(Constants.PACkAGE, null, contentValues);
    }@SuppressLint("Range")
    public List<String> getPackageNamesList(){
        List<String> packageList = new ArrayList<>();
        Cursor cursor = database.query(Constants.PACkAGE,
                null, null, null, null, null, null);

        while (cursor.moveToNext())
            packageList.add(cursor.getString(cursor.getColumnIndex(Constants.PC_NAME)));

        cursor.close();
        return packageList;
    }public int getLastPackageId(){
        int x = -1;
        Cursor cursor = database.query(Constants.PACkAGE,
                null, null, null, null, null, null);
        if(cursor.moveToLast())
            x = cursor.getColumnIndex(Constants.PC_ID) + 1;

        return x;
    }


}
