package com.cjyun.tb.patient.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.cjyun.tb.TbApp;

import java.lang.reflect.Method;

/**
 * Created by jianghw on 2016/3/18 0018.
 * Description
 */
public class SharedUtils {
    /**
     * 全局shared preference的名称
     */
    public static final String SHARED_PREFERANCE_NAME = "share_data_tb";

    public SharedUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static Context getContext() {
        return TbApp.instance();
    }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author jianghw
     */
    private static class SharedPreferencesCompat {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod() {
            try {
                Class clz = SharedPreferences.Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        public static void apply(SharedPreferences.Editor editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            editor.commit();
        }
    }


    /**
     * 存档int 类型的信息
     *
     * @param key   用户设置的key值
     * @param value 用户设置的value值
     */
    public static void setInteger(String key, int value) {
        SharedPreferences sp = getContext().getSharedPreferences(SHARED_PREFERANCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    public static int getInteger(String key, int defaultValue) {
        SharedPreferences sp = getContext().getSharedPreferences(SHARED_PREFERANCE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defaultValue);
    }

    public static void setString(String key, String value) {
        SharedPreferences sp = getContext().getSharedPreferences(SHARED_PREFERANCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    public static String getString(String key, String defaultValue) {
        SharedPreferences sp = getContext().getSharedPreferences(SHARED_PREFERANCE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defaultValue);
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences sp = getContext().getSharedPreferences(SHARED_PREFERANCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        SharedPreferencesCompat.apply(editor);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        SharedPreferences sp = getContext().getSharedPreferences(SHARED_PREFERANCE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences sp = getContext().getSharedPreferences(SHARED_PREFERANCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SHARED_PREFERANCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }
}
