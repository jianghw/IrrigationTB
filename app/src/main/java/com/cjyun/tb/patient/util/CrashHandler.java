package com.cjyun.tb.patient.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by Administrator on 2016/5/31 0031</br>
 * description:</br>当程序发生Uncaught异常的时候,有该类来接管程序,并记录 发送错误报告
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static CrashHandler instance;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;

    /**
     * 用来存储设备信息和异常信息
     */
    private Map<String, String> mDeviceCrashInfo = new HashMap<>();

    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";
    private static final String CRASH_REPORTER_EXTENSION = ".log";
    private static final String CRASH_FIRE_PATH = SDCardTool.getSDCardPath() + "TbLog";

    public static synchronized CrashHandler getInstance() {
        if (null == instance) instance = new CrashHandler();
        return instance;
    }

    /**
     * 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
     *
     * @param context
     */
    public void initDefault(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (handleException(ex) && null != mDefaultHandler) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) return true;
        String messageError = ex.getLocalizedMessage();
        Log.i("Crash", messageError);
        collectCrashDeviceInfo();
        String fileName = saveCrashToFile(ex);
        sendCrashToServer(true);
        return !TextUtils.isEmpty(fileName);
    }

    private void collectCrashDeviceInfo() {
        mDeviceCrashInfo.put(VERSION_NAME,
                AppUtils.getVerName(mContext) == null ? "not set" : AppUtils.getVerName(mContext));
        mDeviceCrashInfo.put(VERSION_CODE, String.valueOf(AppUtils.getVerCode()));
        Field[] fieles = Build.class.getDeclaredFields();
        try {
            for (Field field : fieles) {
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), String.valueOf(field.get(null)));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private String saveCrashToFile(Throwable ex) {
        Iterator<Map.Entry<String, String>> iterator = mDeviceCrashInfo.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            String key = iterator.next().getKey();
            String value = iterator.next().getValue();
            sb.append(key).append("===").append(value).append("\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable throwable = ex.getCause();
        while (throwable != null) {
            throwable.printStackTrace(printWriter);
            throwable = throwable.getCause();
        }
        String result = writer.toString();
        sb.append("----------------------------------------------").append("\n");
        sb.append(result);
        printWriter.close();

        CharSequence time = DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis());
        String fileName = time + "cjy_tb" + CRASH_REPORTER_EXTENSION;
        File pathFile = new File(CRASH_FIRE_PATH, fileName);
        if (SDCardTool.isFileExist(pathFile.getPath())) SDCardTool.deleteFile(pathFile.getPath());

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(pathFile);
            fileOutputStream.write(sb.toString().getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return pathFile.getPath();
        }
    }

    private void sendCrashToServer(boolean b) {
        String[] files = getCrashReportFiles();
        if (files != null && files.length > 0) {
            TreeSet<String> treeSetFiles = new TreeSet<>();
            treeSetFiles.addAll(Arrays.asList(files));
            for (String name : treeSetFiles) {
                File file = new File(CRASH_FIRE_PATH, name);
                sendFileToServer(file, b);
            }
        }
    }

    private String[] getCrashReportFiles() {
        File fileDir = new File(CRASH_FIRE_PATH);
        Log.i("APath", fileDir.getAbsolutePath());
        Log.i("Path", fileDir.getPath());
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(CRASH_REPORTER_EXTENSION);
            }
        };
        return fileDir.list(filter);
    }

    private void sendFileToServer(File file, boolean b) {

    }
}
