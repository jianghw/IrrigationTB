package com.cjyun.tb.supervisor.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.cjyun.tb.supervisor.bean.DB_Bean;
import com.cjyun.tb.supervisor.config.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/3 0003.
 */
public class SuPatientNewsDao {

    private SuPatientNewsDBHelper mHelper;


    public SuPatientNewsDao(Context context) {

        if (mHelper == null) {
            mHelper = new SuPatientNewsDBHelper(context);
            mHelper.getWritableDatabase();
        }
    }

    /**
     * 添加数据
     *
     * @param id
     * @param name
     * @param time
     * @return
     */
    public boolean add(int id, String name, String time,String photo) {

        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.SQL.ID, id);
        values.put(Constants.SQL.NAME, name);
        values.put(Constants.SQL.TIME, time);
        values.put(Constants.SQL.PHOTo, photo);

        long insert = db.insert(Constants.SQL.TAB_NAME, null, values);

        db.close();

        if (insert == -1) {
            return true;
        }
        return false;
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



    public List<DB_Bean> inQuery(String name) {

      //  mData = new ArrayList();
       // List<UserEntityBean.PatientsEntity> mDatas = new UserEntityBean().getPatients();

        List<DB_Bean> mDatas = new ArrayList<>();

        Cursor cursor = null;

        SQLiteDatabase db = mHelper.getReadableDatabase();

        boolean isName = !TextUtils.isEmpty(name);
        if (isName) {
            //  cursor = db.query(Constants.SQL.TAB_NAME, null, Constants.SQL.NAME + "=?", new String[]{name}, null, null, null);
            // 采用模糊查找
            cursor = db.query(Constants.SQL.TAB_NAME, null, Constants.SQL.NAME + " LIKE?",
                    new String[]{"%" + name + "%"}, null, null, null);
        } else {
            cursor = db.query(Constants.SQL.TAB_NAME, null, null, null, null, null, null);
        }
        if (cursor != null) {
            while (cursor.moveToNext()) {

                DB_Bean bean = new DB_Bean();


                bean.setId(cursor.getInt(1));
                bean.setName(cursor.getString(2));
                // TODO 时间需要解析
                bean.setUpdated_at(cursor.getString(3));
                bean.setPhoto(cursor.getString(4));

                mDatas.add(bean);
            }
        }
        cursor.close();
        db.close();

        return mDatas;
    }

    /**
     * 清空所有数据
     */
    public void truncate() {


        String sql = "DELETE FROM " + Constants.SQL.TAB_NAME + ";";
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
