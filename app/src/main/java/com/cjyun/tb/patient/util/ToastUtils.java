package com.cjyun.tb.patient.util;

import android.content.Context;
import android.widget.Toast;

import com.cjyun.tb.TbApp;

/**
 * Created by jianghw on 2016/3/26 0026.
 * Description
 */
public class ToastUtils {

    public static boolean isShow = true;

    private ToastUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static Context getContext() {
        return TbApp.mContext;
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(int message, int duration) {
        if (isShow)
            Toast.makeText(getContext(), getContext().getResources().getString(message), duration).show();
    }

    public static void showShort(int message) {
        if (isShow)
            Toast.makeText(getContext(), getContext().getResources().getString(message), Toast.LENGTH_SHORT).show();
    }

    public static void showMessage(String message) {
        if (isShow) Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(int message) {
        if (isShow)
            Toast.makeText(getContext(), getContext().getResources().getString(message), Toast.LENGTH_LONG).show();
    }

}
