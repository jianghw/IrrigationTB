package com.cjyun.tb.patient.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cjyun.tb.TbApp;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/12/8 0008.
 */
public class AlarmClockService extends Service {
    public static final String ACTION = "com.cjyun.tb.patient.service.AlarmClockService";
    private AlarmClockService context;
    private TimeReceiver mBroadcastReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        super.onCreate();

        mBroadcastReceiver = new TimeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(mBroadcastReceiver, filter);
    }

    private class TimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            boolean isServiceRunning = false;
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                ActivityManager manager = (ActivityManager) TbApp.getContext().getSystemService(Context.ACTIVITY_SERVICE);
                for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                    if (ACTION.equals(service.service.getClassName())) {
                        isServiceRunning = true;
                    }
                }
                if (!isServiceRunning) {
                    Intent i = new Intent(context,AlarmClockService.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(i);
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //启动闹钟
        scheduleInstanceStateChange(context);
        return START_STICKY;
    }

    public static void scheduleInstanceStateChange(Context context) {
        Calendar mCalendar = Calendar.getInstance();
        long time = mCalendar.getTimeInMillis();
        //获取AlarmManager系统服务
        Intent mAlarmIntent = new Intent(context, AlarmTimeReceiver.class);
        PendingIntent mAlarmPendingIntent = PendingIntent.getBroadcast(context, 0, mAlarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        time += 1000 * 60 * 5;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            manager.setExact(AlarmManager.RTC_WAKEUP, time, mAlarmPendingIntent);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, time, mAlarmPendingIntent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        unregisterReceiver(mBroadcastReceiver);
    }
}
