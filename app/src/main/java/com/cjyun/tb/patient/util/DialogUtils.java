package com.cjyun.tb.patient.util;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.ui.main.PtMainActivity;

import java.util.Random;

/**
 * Created by Administrator on 2016/6/13 0013</br>
 * description:</br>
 */
public class DialogUtils {

    public static void showAlertDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("复诊通知")
                .setMessage(message)
                .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    public static void showTimeDialog(Context context, final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);
        builder.setTitle("服药通知")
                .setMessage(message)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SharedUtils.remove("time");
                    }
                })
                .setNegativeButton("稍后提醒", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String time = message.substring(0, 5);
                        SharedUtils.setString("time", time);
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    public static void setNotification(Context context, String message) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));

        builder.setTicker("新消息");
        //设置通知的题目
        builder.setContentTitle("消息提示");
        //设置通知的内容
        builder.setContentText(message + "~");

        //可以被自动取消
        builder.setAutoCancel(true);
        //Notification按时间排序
        builder.setWhen(System.currentTimeMillis());

        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000});
        builder.setLights(Color.RED, 0, 1);

        //设置该通知点击后将要启动的Intent,这里需要注意PendingIntent的用法,构造方法中的四个参数(context,int requestCode,Intent,int flags);
        Intent intent = new Intent(context, PtMainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        builder.setContentIntent(pi);
        //实例化Notification
        Notification notification = builder.build();//notify(int id,notification对象);id用来标示每个notification
        manager.notify(new Random().nextInt(500), notification);
    }
}
