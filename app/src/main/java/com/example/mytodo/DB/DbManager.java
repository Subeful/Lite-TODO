package com.example.mytodo.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    }
    public void closeDb(){
        database.close();
    }



    public void deletePackageAndHisCategoryAndRecord(String itemId) {
        database.execSQL("delete from " + Constants.PACkAGE + " where " + Constants.PC_NAME + " = '" + itemId + "'");
        database.execSQL("delete from " + Constants.CATEGORY + " where " + Constants.PC_ID_R + " = " + getPackageId(itemId));

        database.execSQL("DELETE FROM " + Constants.RECORD + " WHERE " + Constants.RC_ID + " IN (SELECT "
                + Constants.RC_ID + " FROM " + Constants.CATEGORY + " JOIN " + Constants.RECORD + " ON "
                + Constants.CG_ID + " = " + Constants.CG_ID_R + " WHERE " + Constants.PC_ID_R + " = "
                + getPackageId(itemId) + ")");
    }

    public void deleteCategory(String itemId) {
        database.execSQL("delete from " + Constants.CATEGORY + " where " + Constants.CG_NAME + " = '" + itemId +"'");
        database.execSQL("DELETE FROM " + Constants.RECORD + " WHERE " + Constants.CG_ID_R + " = " + getCategoryId(itemId));

    }

    public void deleteRecord(String itemId) {
        database.execSQL("DELETE FROM " + Constants.RECORD + " WHERE " + Constants.RC_TEXT + " = '" + itemId + "'");

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

    public int getCategoryLastIndex(){
        Cursor cursor = database.rawQuery("select max("+Constants.CG_ID+") from " + Constants.CATEGORY, null);

        if(cursor.moveToFirst())
            return cursor.getInt(0)+1;

        cursor.close();
        return 100;
    }
    public int getRecordLastIndex(){
        Cursor cursor = database.rawQuery("select max("+Constants.RC_ID+") from " + Constants.RECORD, null);

        if(cursor.moveToFirst())
            return cursor.getInt(0)+1;

        cursor.close();
        return 100;
    }



    public int getPackageId(String name){
        if (name == null || name.isEmpty())
            return -1;

        try (Cursor cursor = database.rawQuery("select * from " + Constants.PACkAGE + " where " + Constants.PC_NAME + " = '" + name + "'", null)) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex(Constants.PC_ID));
            }
        } catch (SQLException e) {Toast.makeText(context, "Error getting package ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();}

        return -1;
    }

    public int getCategoryId(String name){
        if (name == null || name.isEmpty())
            return -1;

        try (Cursor cursor = database.rawQuery("select * from " + Constants.CATEGORY + " where " + Constants.CG_NAME + " = '" + name + "'", null)) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex(Constants.CG_ID));
            }
        } catch (SQLException e) {Toast.makeText(context, "Error getting category ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();}

        return -1;
    }

    public int getRecordId(String name){
        if (name == null || name.isEmpty())
            return -1;

        try (Cursor cursor = database.rawQuery("select * from " + Constants.RECORD + " where " + Constants.RC_TEXT + " = '" + name + "'", null)) {
            if (cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex(Constants.CG_ID));
            }
        } catch (SQLException e) {Toast.makeText(context, "Error getting category ID: " + e.getMessage(), Toast.LENGTH_SHORT).show();}

        return -1;
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

    public List<String> getCategoryNamesList(int id){
        List<String> packageList = new ArrayList<>();
        Cursor cursor = database.rawQuery("select * from " + Constants.CATEGORY + " where " + Constants.PC_ID_R + " in " +
                "(select " + Constants.PC_ID + " from " + Constants.PACkAGE + " where " + Constants.PC_NAME + " = '" + getPackageName(id) + "')", null);


        while (cursor.moveToNext())
            packageList.add(cursor.getString(cursor.getColumnIndex(Constants.CG_NAME)));

        cursor.close();
        return packageList;
    }

    public Map<Integer, String> getRecordNamesAndIdList(int id){
        Map<Integer,String> packageList = new HashMap<>();

        Cursor cursor = database.rawQuery("select * from "+Constants.RECORD+" where "+Constants.CG_ID_R+ " = " + id, null);

        while (cursor.moveToNext())
            packageList.put(cursor.getInt(cursor.getColumnIndex(Constants.RC_ID)), cursor.getString(cursor.getColumnIndex(Constants.RC_TEXT)));

        cursor.close();
        return packageList;
    }
    public List<String> getRecordNamesList(int id){
        List<String> packageList = new ArrayList<>();

        Cursor cursor = database.rawQuery("select * from "+Constants.RECORD+" where "+Constants.CG_ID_R+ " = " + id, null);

        while (cursor.moveToNext())
            packageList.add(cursor.getString(cursor.getColumnIndex(Constants.RC_TEXT)));

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

    public String getCategoryName(int id){
        Cursor cursor = database.rawQuery("select * from " + Constants.CATEGORY +
                " where " + Constants.CG_ID + " = " + String.valueOf(id) , null);

        while (cursor.moveToNext())
            return cursor.getString(cursor.getColumnIndex(Constants.PC_NAME));

        cursor.close();
        return "null";
    }

    public String getRecordText(int id){
        Cursor cursor = database.rawQuery("select * from " + Constants.RECORD +
                " where " + Constants.RC_ID + " = " + String.valueOf(id) , null);

        while (cursor.moveToNext())
            return cursor.getString(cursor.getColumnIndex(Constants.RC_TEXT));

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

    public void insertToRecord(int id, String name, int cg_id){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.RC_ID, id);
        contentValues.put(Constants.RC_TEXT, name);
        contentValues.put(Constants.CG_ID_R, cg_id);
        database.insert(Constants.RECORD, null, contentValues);
    }



    public void updateRecordText(int rc_id, String newText){
        try {
            database.execSQL("update " +Constants.RECORD+ " set " +Constants.RC_TEXT+ " = '" +newText+ "' where "+Constants.RC_ID+" = " + rc_id);
        }catch (Exception e){Toast.makeText(context, "Error update record", Toast.LENGTH_SHORT).show();}
    }
}
