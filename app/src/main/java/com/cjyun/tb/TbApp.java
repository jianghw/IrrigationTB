package com.cjyun.tb;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import com.cjyun.tb.supervisor.constant.TbGlobal;
import com.cjyun.tb.supervisor.util.SharedUtils;
import com.socks.library.KLog;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.litepal.LitePalApplication;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by jianghw on 2016/3/18 0018.
 * Description
 */
public class TbApp extends LitePalApplication {

    private RefWatcher refWatcher;

    private static Handler mHandler; // 得到主线程

    private static long mMainThreadId; // 得到主线程ID


    private boolean isDebug = true;
    private static TbApp mInstance;
    public static Context mContext;

    private String CUR_USER;
    private String CUR_TOKEN;
    public static volatile Handler applicationHandler;

    public String getCUR_TOKEN() {
        return CUR_TOKEN!=null?CUR_TOKEN:SharedUtils.getString("access_token_"+getCUR_USER(),"");
    }

    public void setCUR_TOKEN(String CUR_TOKEN) {
        this.CUR_TOKEN = CUR_TOKEN;
    }

    public String getCUR_USER() {
        return CUR_USER != null ? CUR_USER : SharedUtils.getString(TbGlobal.JString.CUR_USER, TbGlobal.JString.PATIENT_USER);
    }

    public void setCUR_USER(String CUR_USER) {
        this.CUR_USER = CUR_USER;
    }

    public static TbApp instance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mContext=getApplicationContext();
        applicationHandler = new Handler(mContext.getMainLooper());

        if (isDebug) {
            initLogger();
        } else {
            //关闭本地debug本地调试模式，用于测试人员反馈日志
        }
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //得到主线程
        mHandler = new Handler();
        //得到主线程ID
        mMainThreadId = android.os.Process.myTid();
    }

    //初始化KLog
    private void initLogger() {
        KLog.init(BuildConfig.DEBUG);
        refWatcher = LeakCanary.install(this);
    }

    public static Resources getAppResource(){
        return mContext.getResources();
    }

    public static RefWatcher getRefWatcher() {
        return TbApp.instance().refWatcher;
    }



    public static Context getmContext() {

        return mContext;
    }

    public static Handler getmHandler() {
        return mHandler;
    }

    public static long getmMainThreadId() {
        return mMainThreadId;
    }

    
    //定义一个协议内存缓存的容器/存储结构
    private Map<String, String> mProtocolHashMap = new HashMap<>();

    public Map<String, String> getProtocolHashMap() {
        return mProtocolHashMap;
    }

}
