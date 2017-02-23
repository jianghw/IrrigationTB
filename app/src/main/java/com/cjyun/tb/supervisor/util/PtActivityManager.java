package com.cjyun.tb.supervisor.util;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by Administrator on 2016/3/18 0018.
 * Description 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class PtActivityManager {
    //单例
    private static Stack<Activity> activityStack;
    private static PtActivityManager instance;

    private PtActivityManager() {
    }

    /**
     * 实例化
     *
     * @return
     */
    public static PtActivityManager getAppManager() {
        if (instance == null) {
            instance = new PtActivityManager();
        }
        return instance;
    }

    /**
     * 添加入栈,
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity = null;
        }
    }

    public void finishAllActivityAndExit() {
        if (null != activityStack) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
            System.exit(0);
        }
    }
}
