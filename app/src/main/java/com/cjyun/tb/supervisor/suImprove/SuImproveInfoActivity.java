package com.cjyun.tb.supervisor.suImprove;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.ui.improve.UserInfoFragment;
import com.cjyun.tb.supervisor.base.BaseActivity;
import com.cjyun.tb.supervisor.base.ConstantAll;
import com.cjyun.tb.supervisor.util.ActivityTool;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

/**
 *
 * 资料完善界面
 */
public class SuImproveInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_fragment);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            createResetPasswordFragment();
        } else {//解决重叠问题
            onSolveOverlapByOrder();
        }
    }

    private void createResetPasswordFragment() {
        SuUserInfoFragment fragment = SuUserInfoFragment.newInstance(mHandler);
        ActivityTool.replaceFragmentOnly(
                getSupportFragmentManager(),
                fragment,
                R.id.flay_to_screen_aty,
                UserInfoFragment.class.getSimpleName());
    }

    private final ImproveInfoHandler mHandler = new ImproveInfoHandler(this);

    static class ImproveInfoHandler extends Handler {
        WeakReference<SuImproveInfoActivity> weakReference;

        public ImproveInfoHandler(SuImproveInfoActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SuImproveInfoActivity activity = weakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case ConstantAll.ConHandlerInt.HANDLER_BACK_ACTIVITY:
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
