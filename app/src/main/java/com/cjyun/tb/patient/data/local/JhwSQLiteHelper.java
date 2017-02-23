package com.cjyun.tb.patient.data.local;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cjyun.tb.patient.bean.MyDirectorBean;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2016/5/19 0019</br>
 * description:</br>
 */
public class JhwSQLiteHelper extends SQLiteOpenHelper {
    public JhwSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public JhwSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAllTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MyDirectorBean.class.getSimpleName());
        createAllTable(db);
    }

    private void createAllTable(SQLiteDatabase db) {
        createSingleTable(db, MyDirectorBean.class);
    }

    private void createSingleTable(SQLiteDatabase db, Class<?> beanClass) {
        String sql = getCreateTableSQL(beanClass);
        db.execSQL(sql);
    }

    private String getCreateTableSQL(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" CREATE TABLE IF NOT EXISTS ").append(clazz.getSimpleName())
                .append("(id INTEGER PRIMARY KEY AUTOINCREMENT,");
        //getFields()只能获取public的字段，包括父类的
        //getDeclaredFields()只能获取自己声明的各种字段，包括public，protected，private。
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String fieldType = field.getType().getName();
            if (!fieldName.equalsIgnoreCase("id")) {
                stringBuilder.append(fieldName).append(getColumnType(fieldType)).append(",");
            }
        }
        int len = stringBuilder.length();
        stringBuilder.replace(len - 2, len, ");");
        return stringBuilder.toString();
    }

    private String getColumnType(String fieldType) {
        String type = "TEXT";
        if (fieldType.contains("String")) {
            type = " TEXT ";
        } else if (fieldType.contains("int")) {
            type = "INTEGER";
        } else if (fieldType.contains("boolean")) {
            type = "BOOLEAN";
        } else if (fieldType.contains("float")) {
            type = "FLOAT";
        } else if (fieldType.contains("double")) {
            type = "DOUBLE";
        } else if (fieldType.contains("char")) {
            type = "VARCHAR";
        } else if (fieldType.contains("long")) {
            type = "LONG";
        }
        return type;
    }
}