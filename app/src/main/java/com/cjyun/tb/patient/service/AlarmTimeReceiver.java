package com.cjyun.tb.patient.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.util.DialogUtils;
import com.cjyun.tb.patient.util.SharedUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by 蒋宏伟 on 2016/1/26.
 */
public class AlarmTimeReceiver extends BroadcastReceiver {
    public static final String ACTION = "com.cjyun.tb.patient.service.AlarmClockService";

    @Override
    public void onReceive(final Context context, Intent intent) {
        boolean isServiceRunning = false;
        ActivityManager manager = (ActivityManager) TbApp.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ACTION.equals(service.service.getClassName())) {
                isServiceRunning = true;
            }
        }

        if (!isServiceRunning) {
            Intent i = new Intent(context, AlarmClockService.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(i);
        }

        final PendingResult result = goAsync();
        AlarmAlertWakeLock.acquireCpuWakeLock(context);
        AsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                result.finish();
                String time = SharedUtils.getString("time", "");
                if (!TextUtils.isEmpty(time)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/HH:mm", Locale.SIMPLIFIED_CHINESE);
                    String[] year = sdf.format(new Date()).split("/");
                    String setTime = year[0] + "/" + time;
                    try {
                        long t = sdf.parse(setTime).getTime();
                        long today = new Date().getTime();
                        if (today - t >= 60 * 1000 * 5 && today - t < 60 * 1000 * 35) {
                            DialogUtils.showTimeDialog(context, time + ", 您的服药时间已到，请按时服药。");
                            DialogUtils.setNotification(context, time + ", 您的服药时间已到，请按时服药。");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                AlarmClockService.scheduleInstanceStateChange(context);
                AlarmAlertWakeLock.releaseCpuLock();
            }
        });
    }

}
