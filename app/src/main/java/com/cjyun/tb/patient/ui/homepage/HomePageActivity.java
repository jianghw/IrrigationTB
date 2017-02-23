package com.cjyun.tb.patient.ui.homepage;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseActivity;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.util.ActivityTool;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

/**
 *
 */
public class HomePageActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_fragment);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        String tag = null;
        if (bundle != null) tag = bundle.getString(TbGlobal.JString.FA_BUNDLE_TAG, "xxx");

        if (savedInstanceState == null) {
            createPassedFragment(tag);
        } else {//解决重叠问题
            onSolveOverlapByOrder();
        }
    }
    /**
     * 创造具体指向的fragment
     *
     * @param tag
     */
    private void createPassedFragment(String tag) {
        ActivityTool.addFragmentOnly(
                getSupportFragmentManager(),
                getItem(tag),
                R.id.flay_to_screen_aty,
                tag);
    }
    private Fragment getItem(String tag) {
        Fragment fragment;
        switch (tag) {
            case TbGlobal.JString.TIME_DRUG_TAG://时间及药物
                fragment = DrugFragment.newInstance(mHandler);
                break;
            case TbGlobal.JString.VISIT_EVENT_TAG://访视
                fragment = VisitEventFgt.newInstance(mHandler);
                break;
            case TbGlobal.JString.RETURN_VISIT_TAG://复诊
                fragment = ReturnVisitFgt.newInstance(mHandler);
                break;
            default:
                fragment = DrugFragment.newInstance(mHandler);
                break;
        }
        return fragment;
    }
    private final HomePageHandler mHandler = new HomePageHandler(this);
    static class HomePageHandler extends Handler {
        WeakReference<HomePageActivity> weakReference;
        public HomePageHandler(HomePageActivity activity) {
            weakReference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            HomePageActivity activity = weakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY:
                        activity.finish();
                        break;
                    default:
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }
}