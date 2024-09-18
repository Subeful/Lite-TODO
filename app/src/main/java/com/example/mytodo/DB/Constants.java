package com.example.mytodo.DB;

import com.example.mytodo.Record;

public class Constants {
    public static final String DB_NAME = "todo.db";
    public static final int DB_VERSION = 9;

    public static final String PACkAGE = "Package";

    public static final String PC_ID = "_id_package";
    public static final String PC_NAME = "pc_name";


    public static final String CATEGORY = "Category";

    public static final String CG_ID = "_id_category";
    public static final String CG_NAME = "cg_name";
    public static final String PC_ID_R = "_id_package_r";


    public static final String RECORD = "Record";

    public static final String RC_ID = "_id_record";
    public static final String RC_TEXT = "rc_text";
    public static final String CG_ID_R = "_id_category_r";

    public static final String CREATE_PACKAGE = "create table " + PACkAGE + "(" +
            PC_ID + " INTEGER PRIMARY KEY autoincrement, " +
            PC_NAME + " Text not null)";

    public static final String CREATE_CATEGORY = "create table " + CATEGORY + "(" +
            CG_ID + " INTEGER PRIMARY KEY autoincrement, " +
            CG_NAME + " Text not null, " +
            PC_ID_R + " Integer)";

    public static final String CREATE_RECORD = "create table " + RECORD + "(" +
            RC_ID + " INTEGER PRIMARY KEY autoincrement, " +
            RC_TEXT + " Text not null, " +
            CG_ID_R + " Integer)";

    public static final String DROP_CATEGORY = "drop table if exists " + CATEGORY;
    public static final String DROP_PACKAGE = "drop table if exists " + PACkAGE;
    public static final String DROP_RECORD = "drop table if exists " + RECORD;
}
