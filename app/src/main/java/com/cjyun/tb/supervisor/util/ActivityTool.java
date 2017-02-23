package com.cjyun.tb.supervisor.util;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.collectcloud.password.scribe.utils.Preconditions;

/**
 * <b>@Description:</b>TODO<br/>
 * <b>@Author:</b>Administrator<br/>
 * <b>@Since:</b>2016/4/18 0018<br/>
 */
public class ActivityTool {

    public static void addFragmentOnly(@NonNull FragmentManager fragmentManager,
                                       @NonNull Fragment fragment,
                                       int frameId,
                                       String tag) {
        try {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(frameId, fragment, tag);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 不加入回退栈
     *
     * @param fragmentManager
     * @param fragment
     * @param frameId
     * @param tag
     */
    public static void replaceFragmentOnly(@NonNull FragmentManager fragmentManager,
                                           @NonNull Fragment fragment,
                                           int frameId,
                                           String tag) {
        try {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(frameId, fragment, tag);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void replaceFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                                 @NonNull Fragment fragment,
                                                 int frameId,
                                                 String tag) {
        Preconditions.checkNotNull(fragmentManager,"fragmentManager cannot be null~");
        Preconditions.checkNotNull(fragment,"fragment cannot be null~");
        try {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(frameId, fragment, tag);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
