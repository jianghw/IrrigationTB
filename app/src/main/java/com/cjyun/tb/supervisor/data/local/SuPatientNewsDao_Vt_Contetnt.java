package com.cjyun.tb.supervisor.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cjyun.tb.patient.bean.VisitBean;
import com.cjyun.tb.supervisor.config.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class SuPatientNewsDao_Vt_Contetnt {

    private SuPatientNewsDBHelper mHelper;


    public SuPatientNewsDao_Vt_Contetnt(Context context) {

        if (mHelper == null) {
            mHelper = new SuPatientNewsDBHelper(context);
            mHelper.getWritableDatabase();
        }
    }

    /**
     * 添加数据
     *
     * @param flag
     * @param GsonDate
     * @return
     */
    public boolean add(String flag,String verify, String type, String GsonDate) {

        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.SQL.SETTING_DATE, flag);
        values.put(Constants.SQL.VERIFY_DATE, verify);
        values.put(Constants.SQL.VT_TYPE, type);
        values.put(Constants.SQL.VT_CONTENT, GsonDate);
        long insert = db.insert(Constants.SQL.TAB_VISIT_CONTENT, null, values);

        db.close();
        if (insert == -1) {
            return false;
        }
        return true;
    }

    /**
     * 获取数据库里面一共有多少条记录
     *
     * @return
     */
    public int getTotal() {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query("info", null, null, null, null, null, null);
        int total = cursor.getCount();
        cursor.close();
        db.close();
        return total;
    }


    public List<VisitBean> inQuery(String setting_date) {

        List<VisitBean> list = new ArrayList<>();

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(Constants.SQL.TAB_VISIT_CONTENT, null,
                Constants.SQL.SETTING_DATE + "=?", new String[]{setting_date}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                VisitBean bean = new VisitBean();
                bean.setActually_time(cursor.getString(2));
                bean.setVisit_type(cursor.getString(3));
                bean.setContent(cursor.getString(4));
                list.add(bean);
            }
        }
        cursor.close();
        db.close();

        return list;
    }

    /**
     * 清空所有数据
     */
    public void truncate() {

        String sql = "DELETE FROM " + Constants.SQL.TAB_VISIT_CONTENT + ";";
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL(sql);
        // revertSeq();
        mHelper.close();
        db.close();
    }

    // 让 id恢复自增长
    /*private void revertSeq() {
        String sql = "update sqlite_sequence set seq=0 where name='"+Constants.SQL.TAB_NAME+"'";
        SQLiteDatabase db =  mHelper.getSQLiteDatabase();
        db.execSQL(sql);
        dbHelper.free();
    }*/
}
