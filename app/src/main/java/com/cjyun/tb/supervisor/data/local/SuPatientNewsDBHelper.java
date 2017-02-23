package com.cjyun.tb.supervisor.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cjyun.tb.supervisor.config.Constants;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class SuPatientNewsDBHelper extends SQLiteOpenHelper {


    public SuPatientNewsDBHelper(Context context) {

        super(context, Constants.SQL.SQL_NAME, null, Constants.SQL.DATA_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table " + Constants.SQL.TAB_NAME + "" +
                "(_id integer primary key autoincrement," + Constants.SQL.ID + " integer,"
                + Constants.SQL.NAME + " text," + Constants.SQL.TIME + " text," + Constants.SQL.PHOTo + " text)");


        /*sqLiteDatabase.execSQL("create table " + Constants.SQL.TAB_NAME_JSON + "" +
                "(_id integer primary key autoincrement," + Constants.SQL.FLAG + " Varchar(20),"
                + Constants.SQL.JSON + " text)");*/

        sqLiteDatabase.execSQL("create table " + Constants.SQL.TAB_VISIT_CONTENT +
                "(_id integer primary key autoincrement," + Constants.SQL.SETTING_DATE + " text,"
                + Constants.SQL.VERIFY_DATE + " text,"
                + Constants.SQL.VT_TYPE + " text,"
                + Constants.SQL.VT_CONTENT + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table " + Constants.SQL.SQL_NAME);
        onCreate(sqLiteDatabase);
    }
}
