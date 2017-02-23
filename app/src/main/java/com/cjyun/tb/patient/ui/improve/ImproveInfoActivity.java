package com.cjyun.tb.patient.ui.improve;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.cjyun.tb.R;
import com.cjyun.tb.patient.base.BaseActivity;
import com.cjyun.tb.patient.constant.TbGlobal;
import com.cjyun.tb.patient.util.ActivityTool;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

/**
 * <b>@Description:</b>TODO<br/>
 */
public class ImproveInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pt_fragment);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            createUserInfoFragment();
        } else {//解决重叠问题
            onSolveOverlapByOrder();
        }
    }

    private void createUserInfoFragment() {
        UserInfoFragment fragment = UserInfoFragment.newInstance(mHandler);
        ActivityTool.replaceFragmentOnly(
                getSupportFragmentManager(),
                fragment,
                R.id.flay_to_screen_aty,
                UserInfoFragment.class.getSimpleName());
    }

    private final ImproveInfoHandler mHandler = new ImproveInfoHandler(this);

    static class ImproveInfoHandler extends Handler {
        WeakReference<ImproveInfoActivity> weakReference;

        public ImproveInfoHandler(ImproveInfoActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ImproveInfoActivity activity = weakReference.get();
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
