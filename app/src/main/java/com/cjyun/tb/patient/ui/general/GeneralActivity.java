package com.cjyun.tb.patient.ui.general;

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
 * 通用基础
 */
public class GeneralActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_fragment);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        String tag = null;
        if (bundle != null) tag = bundle.getString(TbGlobal.JString.FA_BUNDLE_TAG, "xxx");

        if (savedInstanceState == null) {
            createGeneralFragment(tag);
        } else {//解决重叠问题
            onSolveOverlapByOrder();
        }
    }

    private void createGeneralFragment(String tag) {
        ActivityTool.addFragmentOnly(
                getSupportFragmentManager(),
                getItem(tag),
                R.id.flay_to_screen_aty,
                tag);
    }

    private Fragment getItem(String tag) {
        Fragment fragment;
        switch (tag) {
            case TbGlobal.JString.MY_DIRECTOR_TAG://我的督导员
                fragment = MyDirectorFragment.newInstance(mHandler);
                break;
            case TbGlobal.JString.MY_MESSAGE_TAG://我的消息
                fragment = MyMessageFragment.newInstance(mHandler);
                break;
            case TbGlobal.JString.SUGGESTION_BOX_TAG://意见箱
                fragment = SuggestionFragment.newInstance(mHandler);
                break;
            case TbGlobal.JString.GENERAL_TAG://通用页
                fragment = GeneralFragment.newInstance(mHandler);
                break;
            default:
                fragment = GeneralFragment.newInstance(mHandler);
                break;
        }
        return fragment;
    }

    private final GeneralHandler mHandler = new GeneralHandler(this);

    static class GeneralHandler extends Handler {
        WeakReference<GeneralActivity> weakReference;

        public GeneralHandler(GeneralActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            GeneralActivity activity = weakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case TbGlobal.IntHandler.HANDLER_BACK_ACTIVITY:
                        activity.finish();
                        break;
                    case TbGlobal.IntHandler.HANDLER_BACK_FGT:
                        activity.getSupportFragmentManager().popBackStackImmediate();
                        break;
                    case TbGlobal.IntHandler.HANDLER_FGT_FONT:
                        activity.createFontFragment();
                        break;
                    case TbGlobal.IntHandler.HANDLER_FGT_ABOUT_US:
                        activity.createUsFragment();
                        break;
                    case TbGlobal.IntHandler.HANDLER_FGT_DISCLAIMER:
                        activity.DisclaimerFragment();
                        break;
                    default:
                        break;
                }
            }
            super.handleMessage(msg);
        }
    }

    private void createFontFragment() {
        FontFragment fragment = FontFragment.newInstance(mHandler);
        ActivityTool.replaceFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.flay_to_screen_aty,
                FontFragment.class.getSimpleName());
    }

    private void createUsFragment() {
        AboutUsFragment fragment = AboutUsFragment.newInstance(mHandler);
        ActivityTool.replaceFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.flay_to_screen_aty,
                AboutUsFragment.class.getSimpleName());
    }

    private void DisclaimerFragment() {
        DisclaimerFragment fragment = DisclaimerFragment.newInstance(mHandler);
        ActivityTool.replaceFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.flay_to_screen_aty,
                DisclaimerFragment.class.getSimpleName());
    }
}
