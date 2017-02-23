package com.cjyun.tb.supervisor.util;

import android.content.Context;
import android.os.Handler;

import com.cjyun.tb.TbApp;


/**
 * Created by T420 on 2016/4/18.
 */
public class UIUtils {


    /**
     * 获取全局的上下文
     *
     * @return
     */
    public static Context getContext() {

        return TbApp.getmContext();
    }

    /**
     * 得到主线程的线程id
     *
     * @return
     */
    public static long getMainThreadId() {
        return TbApp.getmMainThreadId();
    }

    /**
     * 得到主线程的Handler对象
     */
    public static Handler getMainThreadHandler() {

        return TbApp.getmHandler();
    }

    /**
     * 安全的执行一个任务
     * 1.当前任务所在线程子线程-->使用消息机制发送到主线程执行
     * 2.当前任务所在线程主线程-->直接执行
     */
    public static void postTask(Runnable task) {
        //得到当前线程的线程id
        long curThreadId = android.os.Process.myTid();
        long mainThreadId = getMainThreadId();
        if (curThreadId == mainThreadId) {//主线程
            task.run();
        } else {//子线程

            getMainThreadHandler().post(task);
        }
    }
    /*
    使用方法
    UIUtils.postTask(new Runnable() {
                @Override
                public void run() {

                    initSeleted();
                }
            });*/


    /**
     * 根据手机分辨率从dp转成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f) - 15;
    }

    /**
     * 得到应用程序的包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

}
