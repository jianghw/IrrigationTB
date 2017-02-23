package com.cjyun.tb.patient.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.cjyun.tb.R;
import com.cjyun.tb.TbApp;
import com.cjyun.tb.patient.util.PtActivityManager;
import com.cjyun.tb.patient.util.SharedUtils;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * <b>@Description:</b>TODO<br/>
 */
public class BaseActivity extends FragmentActivity {

    private ProgressDialog mProgressDialog;

    @Override
    public void setContentView(int layoutResID) {
        int mode = SharedUtils.getInteger("Theme_Font", 2);
        if (mode == 1) {
            this.setTheme(R.style.Theme_Small);
        } else if (mode == -1 || mode == 2) {
            this.setTheme(R.style.Theme_Medium);
        } else if (mode == 3) {
            this.setTheme(R.style.Theme_Large);
        }
        super.setContentView(layoutResID);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PtActivityManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PtActivityManager.getAppManager().finishActivity(this);
        TbApp.getRefWatcher().watch(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    /**
     * activity 跳转
     *
     * @param context
     * @param cls
     */
    protected void onJumpToNewActivity(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        startActivity(intent);
    }

    /**
     * 解决activity中的顺序出现的fragment的重叠问题
     */
    protected void onSolveOverlapByOrder() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && fragmentList.size() > 0) {
            boolean isShow = false;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            for (int i = fragmentList.size() - 1; i >= 0; i--) {
                if (fragmentList.get(i) != null) {
                    if (!isShow) {
                        transaction.show(fragmentList.get(i));
                        isShow = true;
                    } else {
                        transaction.hide(fragmentList.get(i));
                    }
                }
            }
            transaction.commit();
        }
    }

    protected void createLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.send_net_request));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    protected void dismissLoadingDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }


}
