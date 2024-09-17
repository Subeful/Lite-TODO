package com.example.mytodo.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
@SuppressLint("Range")
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

    public void deleteItem(String itemId) {
        database.execSQL("delete from " + Constants.PACkAGE + " where " + Constants.PC_NAME + " = '" + itemId + "'");
    }



    public int getLastPackageId(String constants){
        int x = -1;
        Cursor cursor = database.rawQuery("select * from " + constants, null);
        if(cursor.moveToLast()){
            x = cursor.getCount() + 1;
        }

        cursor.close();
        return x;
    }

    public int getPackageId(String name){
        if (name == null || name.isEmpty()) {
            return -1;
        }
        try (Cursor cursor = database.rawQuery("select * from " + Constants.PACkAGE + " where " + Constants.PC_NAME + " = ?", new String[]{name})) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex(Constants.PC_ID));
            }
        } catch (SQLException e) {
            Toast.makeText(context, "Error getting package ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return -1;
    }

//    public List<String> getCategoryNamesList(String name){
//        List<String> packageList = new ArrayList<>();
//        if (getPackageId(name) != -1) {
//            try (Cursor cursor = database.rawQuery("select * from " + Constants.CATEGORY +
//                    " where " + Constants.PC_ID_R + " = ?", new String[]{String.valueOf(getPackageId(name))})) {
//                if (cursor.moveToFirst()){
//                    while (cursor.moveToNext()) {
//                        packageList.add(cursor.getString(cursor.getColumnIndex(Constants.PC_NAME)));
//                    }
//                }
//            } catch (Exception e) {
//                Toast.makeText(context, "Error getting category names: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        }
//        return packageList;
//    }
//    public List<String> getCategoryNamesList(String name){
//        List<String> packageList = new ArrayList<>();
//        Cursor cursor = database.rawQuery("select * from " + Constants.CATEGORY + " where " + Constants.PC_ID_R.trim() + " = " + String.valueOf(getPackageId(name.trim())), null);
//
//
//        while (cursor.moveToNext())
//            packageList.add(cursor.getString(cursor.getColumnIndex(Constants.CG_NAME)));
//
//        cursor.close();
//        return packageList;
//    }

    public List<String> getCategoryNamesList(int id){
        List<String> packageList = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from " + Constants.CATEGORY + " where " + Constants.PC_ID_R + " = " + String.valueOf(id), null);


        while (cursor.moveToNext())
            packageList.add(cursor.getString(cursor.getColumnIndex(Constants.CG_NAME)));

        cursor.close();
        return packageList;
    }

    public List<String> getPackageNamesList(){
        List<String> packageList = new ArrayList<>();

        Cursor cursor = database.query(Constants.PACkAGE,
                null, null, null, null, null, null);
        while (cursor.moveToNext())
            packageList.add(cursor.getString(cursor.getColumnIndex(Constants.PC_NAME)));

        cursor.close();
        return packageList;
    }

    public String getPackageName(int id){
        Cursor cursor = database.rawQuery("select * from " + Constants.PACkAGE +
                " where " + Constants.PC_ID + " = " + String.valueOf(id) , null);

        while (cursor.moveToNext())
            return cursor.getString(cursor.getColumnIndex(Constants.PC_NAME));

        cursor.close();
        return "null";
    }




    public void insertToPackage(int id, String name){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.PC_ID, id);
        contentValues.put(Constants.PC_NAME, name);
        database.insert(Constants.PACkAGE, null, contentValues);
    }

    public void insertToCategory(int id, String name, int pc_id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.CG_ID, id);
        contentValues.put(Constants.CG_NAME, name);
        contentValues.put(Constants.PC_ID_R, pc_id);
        database.insert(Constants.CATEGORY, null, contentValues);
    }


    public int getCategoryCount(){
        Cursor cursor = database.rawQuery("select count(*) from " + Constants.CATEGORY, null);

        if(cursor.moveToFirst())
            return cursor.getInt(0);

        cursor.close();
        return 100;
    }


}
