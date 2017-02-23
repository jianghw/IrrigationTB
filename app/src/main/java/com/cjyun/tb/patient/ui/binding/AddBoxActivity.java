package com.cjyun.tb.patient.ui.binding;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseActivity;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.ui.improve.ImproveInfoActivity;
import com.cjyun.tb.patient.util.ActivityTool;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

/**
 * 绑定药盒~~
 */
public class AddBoxActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_fragment);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            createScanBoxFragment();
        } else {//解决重叠问题
            onSolveOverlapByOrder();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private final ScanBoxHandler mHandler = new ScanBoxHandler(this);

    static class ScanBoxHandler extends Handler {
        WeakReference<AddBoxActivity> weakReference;

        public ScanBoxHandler(AddBoxActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            AddBoxActivity activity = weakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY:
                        activity.finish();
                        break;
                    case TbGlobal.IntHandler.HANDLER_TO_GSM:
                        activity.createGSMFragment();
                        break;
                    case TbGlobal.IntHandler.HANDLER_TO_WIFI:
                        activity.createWiFiFragment();
                        break;
                    case TbGlobal.IntHandler.HANDLER_BACK_FGT://回退
                        activity.getSupportFragmentManager().popBackStackImmediate();
                        break;
                    case TbGlobal.IntHandler.HANDLER_TO_BINDING://转绑定页面
                        activity.createBindingFragment();
                        break;
                    case TbGlobal.IntHandler.HANDLER_TO_IMPROVE_INFO://完善资料页面
                        activity.onJumpToNewActivity(activity, ImproveInfoActivity.class);
                        break;
                    default:
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }

    private void createBindingFragment() {
        BindingFragment fragment = BindingFragment.newInstance(mHandler);
        ActivityTool.replaceFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.flay_to_screen_aty,
                BindingFragment.class.getSimpleName());
    }

    /**
     * GSM说明页
     */
/*    private void createGSMFragment() {
        GSMFragment fragment = GSMFragment.newInstance(mHandler);
        ActivityTool.replaceFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.flay_to_screen_aty,
                GSMFragment.class.getSimpleName());
    }*/
    private void createGSMFragment() {
        WiFiFragment fragment = WiFiFragment.newInstance(mHandler);
        ActivityTool.replaceFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.flay_to_screen_aty,
                WiFiFragment.class.getSimpleName());
    }


    private void createWiFiFragment() {
        WiFiFragment fragment = WiFiFragment.newInstance(mHandler);
        ActivityTool.replaceFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.flay_to_screen_aty,
                WiFiFragment.class.getSimpleName());
    }

    /**
     * 准备扫描页
     */
    private void createScanBoxFragment() {
        ScanBoxFragment fragment = ScanBoxFragment.newInstance(mHandler);
        ActivityTool.replaceFragmentOnly(
                getSupportFragmentManager(),
                fragment,
                R.id.flay_to_screen_aty,
                ScanBoxFragment.class.getSimpleName());
    }
}
