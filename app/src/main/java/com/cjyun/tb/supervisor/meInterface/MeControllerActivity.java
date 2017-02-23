package com.cjyun.tb.supervisor.meInterface;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.ui.general.FontFragment;
import com.cjyun.tb.supervisor.base.BaseActivity;
import com.cjyun.tb.supervisor.base.ConstantAll;
import com.cjyun.tb.supervisor.util.ActivityTool;

import java.lang.ref.WeakReference;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class MeControllerActivity extends BaseActivity {

    @Bind(R.id.frame_su_fragment)
    FrameLayout frameSuFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setContentView(R.layout.activity_su_fragment);
        // Set up tasks view
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        String tag = null;
        if (bundle != null) tag = bundle.getString(ConstantAll.ConString.FA_BUNDLE_TAG, "xxx");

        if (savedInstanceState == null) {
            createMeFragment(tag);
        } else {//解决重叠问题
            onSolveOverlapByOrder();
        }
    }

    private void createMeFragment(String tag) {

        ActivityTool.addFragmentOnly(
                getSupportFragmentManager(),
                getItem(tag),
                R.id.frame_su_fragment,
                tag);
    }

    private Fragment getItem(String tag) {
        Fragment fragment;
        switch (tag) {
            case ConstantAll.ConString.SU_MY_BASE_INFO_TAG://督导员信息
                fragment = SuBaseInfoFragment.newInstance(mHandler);
                break;
            case ConstantAll.ConString.MY_MESSAGE_TAG://我的消息
                fragment = SuMyMessageFragment.newInstance(mHandler);
                break;
            case ConstantAll.ConString.GENERAL_TAG://通用页
                fragment = SuGeneralFragment.newInstance(mHandler);
                break;
            default:
                fragment = SuGeneralFragment.newInstance(mHandler);
                break;
        }
        return fragment;
    }

    private final MeControllerHandler mHandler = new MeControllerHandler(this);

    static class MeControllerHandler extends Handler {
        WeakReference<MeControllerActivity> weakReference;

        public MeControllerHandler(MeControllerActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        /**
         * 接收消息
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            MeControllerActivity activity = weakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case ConstantAll.ConHandlerInt.HANDLER_BACK_ACTIVITY:
                        activity.finish();
                        break;
                    // 更改字体的页面
                    case TbGlobal.IntHandler.HANDLER_FGT_FONT:
                        activity.createFontFragment();
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
        com.cjyun.tb.patient.util.ActivityTool.replaceFragmentToActivity(
                getSupportFragmentManager(),
                fragment,
                R.id.frame_su_fragment,
                FontFragment.class.getSimpleName());
    }
}
